package odb;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class allocates regions within a RandomAccessFile much like
 * malloc allocates regions in memory.  The caller can request a 
 * region of arbitrary size; the class will extend the file if necessary
 * to accomodate it.  A caller can free a region, making the space inside
 * available for re-allocation.  Adjacent free regions are coalesced when
 * the second is freed.
 *
 * This allocator makes no attempt to take into account the performance
 * properties of different random access operations on different architectures.
 *
 * When a new allocator in a new file is created, it is created with an
 * initial region.  This initial region is made available to the user as
 * getInitialRegion, but may not be freed or re-allocated.
 *
 * The allocator is initially created with three parameters: the first
 * is the size of the initial region.  The second is the minimum client visible
 * portion of a region; as discussed below, this must be at least 4.  It
 * can be made larger to reduce fragmentation in the allocator.  The
 * final parameter is the minimum amount to be added to a file when a region
 * that is larger than any available free region is requested.  This can
 * reduce fragmentation of the file in the file system(?).
 *
 * Default values are available for all the parameters.
 *
 * Implementation Details:
 *
 * All numbers are stored in Java-serialization standard format as 32-bit
 * integers.  Sizes and offsets are stored as signed integers; the absolute
 * value is taken when the values are interpreted.  If the integers is
 * negative, that provides additional information about the associated region.
 *
 * Each region is 8 bytes longer than the client visible portion.
 * The first and last four bytes of a region are the size of the region in
 * the file.  If the first four bytes are
 * negative, it means that the region is free.  For free regions, the first
 * four bytes of what was the client visible portion of the region is
 * converted to the file offset to the next free region, or 0 if this is
 * the last free region.  The next four bytes refer to the 
 *
 * This implies that the smallest allowable client part of a region is 4
 * bytes, and therefore the smallest allowable region is 12 bytes.  Callers
 * can request smaller regions, but the region size will silently be increased
 * to the minimum.  The allocator does not keep track of what the originally
 * requested size of the region was.
 *
 * If there are free regions when an allocation call is made, the first free
 * region large enough to contain the requested allocation is used.  If this
 * free region is big enough so that more than the minimum region size is
 * left over, the region is split.
 *
 * Free regions are kept in a linked list that starts with the last free
 * region in the file.  When looking for a free region for an allocation,
 * this linked list is traversed towards the front of the file.
 *
 * In addition to the regions, the file uses some overhead space at the
 * front of the file.
 * 4 bytes - Offset of initial region
 * 4 bytes - Smallest allocatable region size (internal size,
 * not client visible size)
 * 4 bytes - Size increment when file is expanded
 * 4 bytes - Total size of file
 * 4 bytes - Offset of last free region in file (start of free region
 * list).  If there are no free regions, this value is 0.
 */
public class DiskAllocator
{
    public static final int FORCE_CREATE=1;
    public static final int FORCE_EXIST=2;
    public static final int FORCE_MATCHING=4;
    public static final int OVERWRITE_PARAMS=8;

    public static final int DEFAULT_CHUNK_SIZE=8;
    public static final int MIN_CHUNK_SIZE=8;
    public static final int DEFAULT_INCREMENT_SIZE=2048;

    public DiskAllocator( File file, int initialRegion, int chunkSize,
		int sizeIncrement, int creationFlags)
		throws IOException, DiskAllocatorException
    {
		// Normalize the parameters
		if ( chunkSize<MIN_CHUNK_SIZE)
			chunkSize=MIN_CHUNK_SIZE;
		if ( sizeIncrement<MIN_SIZE_INCREMENT)
			sizeIncrement=MIN_SIZE_INCREMENT;
		if ( sizeIncrement<chunkSize+REGION_OVERHEAD_SIZE)
			sizeIncrement=chunkSize+REGION_OVERHEAD_SIZE;
			
		if ( file.exists())
			initializeFromExisting( file, initialRegion, chunkSize, sizeIncrement,
				creationFlags);
		else
			initializeNew( file, initialRegion, chunkSize, sizeIncrement,
				creationFlags);
    }

    public DiskAllocator( File file)
		throws IOException, DiskAllocatorException
    {
		this( file, DEFAULT_CHUNK_SIZE, DEFAULT_CHUNK_SIZE, DEFAULT_INCREMENT_SIZE, 0);
    }

	public synchronized int allocate( int size)
		throws IOException, DiskAllocatorException
	{
		size=normalizeRegionSize( size);
		// Walk free list to see if there is an available free region
		int freeOffset=lastFreeRegionOffset;
		int freeSize=0;
		int prevOffset=0;
		for ( ; freeOffset!=0; freeOffset=randomFile.readInt())
		{
			randomFile.seek( freeOffset);
			freeSize= -randomFile.readInt();
			if ( freeSize<=0)
				throw new DiskAllocatorException( "Corrupted free list");
			if ( freeSize>=size)
				break;
			prevOffset=freeOffset;
		}
		/* If no free regions were big enough, region will be added to end of
		 * file */
		if ( freeOffset==0)
		{
			freeSize=extendFile( size);
			prevOffset=0;
			freeOffset=lastFreeRegionOffset;
		}

		/* Get offset of next free region */
		randomFile.seek( freeOffset+REGION_FREE_PTR_OFFSET);
		int nextFreeRegionOffset=randomFile.readInt();
		/* If the free region is big enough, we have to split it */
		if ( freeSize-size>=minimumRegionSize)
		{
			int newFreeOffset=freeOffset+size;
			randomFile.seek( freeOffset);
			randomFile.writeInt( size);	
			randomFile.seek( newFreeOffset-REGION_END_OFFSET);
			randomFile.writeInt( size);
			randomFile.writeInt( -( freeSize-size));
			randomFile.writeInt( nextFreeRegionOffset);
			randomFile.writeInt( prevOffset);
			randomFile.seek( freeOffset+freeSize-REGION_END_OFFSET);
			randomFile.writeInt( freeSize-size);
			if ( prevOffset==0)
				lastFreeRegionOffset=newFreeOffset;
			else
			{
				randomFile.seek( prevOffset+REGION_FREE_PTR_OFFSET);
				randomFile.writeInt( newFreeOffset);
			}
			if ( nextFreeRegionOffset!=0)
			{
				randomFile.seek( nextFreeRegionOffset+REGION_PREV_FREE_OFFSET);
				randomFile.writeInt( newFreeOffset);
			}
		}
		/* Else mark the region as no longer free */
		{
			randomFile.seek( freeOffset);
			randomFile.writeInt( freeSize);
			if ( prevOffset==0)
				lastFreeRegionOffset=nextFreeRegionOffset;
			else
			{
				randomFile.seek( prevOffset+REGION_FREE_PTR_OFFSET);
				randomFile.writeInt( nextFreeRegionOffset);
			}
			if ( nextFreeRegionOffset!=0)
			{
				randomFile.seek( nextFreeRegionOffset+REGION_PREV_FREE_OFFSET);
				randomFile.writeInt( prevOffset);
			}
		}

		return freeOffset+REGION_START_OFFSET;
	}

	public synchronized void free( int regionOffset)
		throws IOException, DiskAllocatorException
	{
		regionOffset-=REGION_START_OFFSET;
		if ( regionOffset<initialRegionOffset || regionOffset>
			fileSize-REGION_OVERHEAD_SIZE-MIN_CHUNK_SIZE)
			throw new DiskAllocatorException( "Not a region");
		if ( regionOffset==initialRegionOffset)
			throw new DiskAllocatorException( "Can't free initial region");
		randomFile.seek( regionOffset-REGION_END_OFFSET);
		int previousRegionSize=randomFile.readInt();
		int previousRegionOffset=regionOffset-previousRegionSize;
		int regionSize=randomFile.readInt();
		int nextRegionOffset=regionOffset+regionSize;
		if ( previousRegionOffset<initialRegionOffset ||
			previousRegionOffset>=regionOffset ||
			previousRegionSize<REGION_OVERHEAD_SIZE+MIN_CHUNK_SIZE ||
			regionSize<REGION_OVERHEAD_SIZE+MIN_CHUNK_SIZE ||
			nextRegionOffset<=regionOffset ||
			nextRegionOffset>fileSize ||
			    ( nextRegionOffset<fileSize && nextRegionOffset>
				fileSize-MIN_CHUNK_SIZE-REGION_OVERHEAD_SIZE))
			throw new DiskAllocatorException( "Not a region or corrupt file");
		/* If this is going to be the only free region in the file,
		 * take a shortcut */
		if ( lastFreeRegionOffset==0)
		{
			lastFreeRegionOffset=regionOffset;
			randomFile.seek( regionOffset);
			randomFile.writeInt( -regionSize);
			randomFile.writeInt( 0);
			randomFile.writeInt( 0);
			return;
		}
		int previousFreeOffset= -1;
		int nextFreeOffset= -1;
		boolean changeSize=false;
		// See if you can coalesce with the preceding region in the file
		if ( previousRegionOffset>initialRegionOffset && previousRegionOffset<=
			lastFreeRegionOffset)
		{
			randomFile.seek( previousRegionOffset);
			if ( randomFile.readInt()<0)
			{
				nextFreeOffset=randomFile.readInt();
				previousFreeOffset=randomFile.readInt();
				regionOffset=previousRegionOffset;
				regionSize+=previousRegionSize;
				changeSize=true;
			}
		}
		int nextRegionSize=0;
		// See if you can coalesce with the following region in the file
		if ( nextRegionOffset<fileSize)
		{
			randomFile.seek( nextRegionOffset);
			nextRegionSize=randomFile.readInt();
			if ( nextRegionSize<0)
			{
				nextRegionSize= -nextRegionSize;
				regionSize+=nextRegionSize;
				changeSize=true;
				int possibleNextFreeOffset=randomFile.readInt();
				if ( nextFreeOffset== -1)
				{
					nextFreeOffset=possibleNextFreeOffset;
				}
				previousFreeOffset=randomFile.readInt();
			}
		}
		
		// If we have coalesced, all the parameters are set;
		// If we did not, scan towards the end of the file until we hit a free region
		if ( ! changeSize)
		{
			// If this is going to be the last free region in the file, things are easy
			if ( regionOffset>lastFreeRegionOffset)
			{
				previousFreeOffset=0;
				nextFreeOffset=lastFreeRegionOffset;
			}
			else
			{
				// Scan towards the end of the file
				for ( previousFreeOffset=nextRegionOffset+nextRegionSize;
					previousFreeOffset<lastFreeRegionOffset;
					previousFreeOffset=previousFreeOffset+nextRegionSize)
				{
					randomFile.seek( previousFreeOffset);
					nextRegionSize=randomFile.readInt();
					if ( nextRegionSize<0)
						break;
				}
				if ( nextFreeOffset>lastFreeRegionOffset)
				{
					throw new DiskAllocatorException(
						"Corrupt file walking region list to next free");
				}
				nextFreeOffset=randomFile.readInt();
			}
		}
		/* All information is ready to update the free list for the previous
		 * and following blocks, and to set the info for this block */		
		if ( previousFreeOffset== -1 || nextFreeOffset== -1)
		{
			throw new DiskAllocatorException( "Logic error maintaining free list");
		}
		// Update the free block earlier in the file (next in the list)
		if ( nextFreeOffset!=0)
		{
			randomFile.seek( nextFreeOffset+REGION_PREV_FREE_OFFSET);
			randomFile.writeInt( regionOffset);
		}
		// Write the info for the block we are freeing
		randomFile.seek( regionOffset);
		randomFile.writeInt( -regionSize);
		randomFile.writeInt( nextFreeOffset);
		randomFile.writeInt( previousFreeOffset);
		if ( changeSize)
		{
			randomFile.seek( regionOffset+regionSize-REGION_END_OFFSET);
			randomFile.writeInt( regionSize);
		}
		// Update the free block later in the file (earlier in the list)
		if ( previousFreeOffset==0)
		{
			lastFreeRegionOffset=regionOffset;
		}
		else
		{
			randomFile.seek( previousFreeOffset+REGION_FREE_PTR_OFFSET);
			randomFile.writeInt( regionOffset);
		}
	}

	public int getInitialRegion()
	{
		return initialRegionOffset;
	}

	public int getRegionSize( int regionOffset)
		throws IOException, DiskAllocatorException
	{
		regionOffset-=REGION_START_OFFSET;
		if ( regionOffset<initialRegionOffset || regionOffset>fileSize-
			MIN_CHUNK_SIZE+REGION_OVERHEAD_SIZE)
			throw new DiskAllocatorException( "Invalid region");
		randomFile.seek( regionOffset);
		return randomFile.readInt();
	}

	public byte[] read( int offset, int size)
		throws IOException, DiskAllocatorException
	{
		if ( offset<initialRegionOffset || offset>fileSize)
			throw new DiskAllocatorException( "Not a region");
		if ( size+offset<offset || size+offset>fileSize)
			throw new DiskAllocatorException( "Reading past end of region");
		byte[] retVal=new byte[size];
		randomFile.seek( offset);
		randomFile.readFully( retVal);

		return retVal;
	}

	public void write( int offset, byte[] toWrite)
		throws IOException, DiskAllocatorException
	{
		int size=toWrite.length;
		if ( offset<initialRegionOffset || offset>fileSize)
			throw new DiskAllocatorException( "Not a region");
		if ( size+offset<offset || size+offset>fileSize)
			throw new DiskAllocatorException( "Writing past end of region");
		randomFile.seek( offset);
		randomFile.write( toWrite);
	}

	public synchronized void sync()
		throws IOException
	{
		writeOverhead();
		randomFile.getFD().sync();
	}

	public synchronized void close()
		throws IOException
	{
		sync();
		randomFile.close();
	}

	public boolean isNewFile()
	{
		return newFile;
	}

	private static final int OVERHEAD_SIZE=20;
	private static final int REGION_OVERHEAD_SIZE=8;
	private static final int MIN_SIZE_INCREMENT=OVERHEAD_SIZE+REGION_OVERHEAD_SIZE+
		MIN_CHUNK_SIZE;
	private static final int REGION_START_OFFSET=4;
	private static final int REGION_END_OFFSET=4;
	private static final int REGION_FREE_PTR_OFFSET=4;
	private static final int REGION_PREV_FREE_OFFSET=REGION_FREE_PTR_OFFSET+4;

    private RandomAccessFile randomFile;
	private boolean newFile;
	private int initialRegionOffset;
	private int minimumRegionSize;
	private int fileIncrementSize;
	private int fileSize;
	private int lastFreeRegionOffset;

	private void initializeFromExisting( File file, int initialRegionSize, int chunkSize,
		int sizeIncrement, int creationFlags)
		throws IOException, DiskAllocatorException
	{
		if ( ( creationFlags & FORCE_CREATE)!=0)
		{
			throw new DiskAllocatorException( "File already exists");
		}
		randomFile=new RandomAccessFile( file, "rw");
		newFile=false;
		randomFile.seek(0);
		initialRegionOffset=randomFile.readInt();
		if ( initialRegionOffset!=OVERHEAD_SIZE)
			throw new DiskAllocatorException( "Invalid or corrupt file");
		minimumRegionSize=randomFile.readInt();
		fileIncrementSize=randomFile.readInt();
		fileSize=randomFile.readInt();
		lastFreeRegionOffset=randomFile.readInt();

		if ( ( creationFlags & FORCE_MATCHING)!=0)
		{
			int firstSize=randomFile.readInt();
			if ( minimumRegionSize!=chunkSize+REGION_OVERHEAD_SIZE ||
				fileIncrementSize!=sizeIncrement ||
				firstSize!=normalizeRegionSize( initialRegionSize))
			{
				throw new DiskAllocatorException( "Parameter mismatch");
			}
		}
		if ( ( creationFlags & OVERWRITE_PARAMS)!=0)
		{
			minimumRegionSize=chunkSize+REGION_OVERHEAD_SIZE;
			fileIncrementSize=sizeIncrement;
			writeOverhead();
		}
	}

	private void initializeNew( File file, int initialRegionSize, int chunkSize,
		int sizeIncrement, int creationFlags)
		throws IOException, DiskAllocatorException
	{
		if ( ( creationFlags & FORCE_EXIST)!=0)
		{
			throw new DiskAllocatorException( "File does not exist");
		}
		randomFile=new RandomAccessFile( file, "rw");
		newFile=true;
		initialRegionOffset=OVERHEAD_SIZE;
		minimumRegionSize=chunkSize+REGION_OVERHEAD_SIZE;
		initialRegionSize=normalizeRegionSize( initialRegionSize);
		fileIncrementSize=sizeIncrement;
		int usedLength=initialRegionSize+OVERHEAD_SIZE;
		if ( usedLength<=fileIncrementSize)
			fileSize=fileIncrementSize;
		else
			fileSize=( usedLength/fileIncrementSize)*fileIncrementSize+
				( ( usedLength%fileIncrementSize)!=0 ? fileIncrementSize :0);
		// If there is space at the end of the file, make sure it is at least as big
		// as minimumRegion
		if ( fileSize!=usedLength && fileSize-usedLength<minimumRegionSize)
			fileSize+=fileIncrementSize;
		if ( fileSize<fileIncrementSize)
			throw new DiskAllocatorException( "Initial size overflow");
		if ( fileSize==usedLength)
		{
			lastFreeRegionOffset=0;
		}
		else
		{
			lastFreeRegionOffset=usedLength;
		}
		writeOverhead();
		randomFile.seek( OVERHEAD_SIZE);
		randomFile.writeInt( initialRegionSize);
		/* Only initialization of initial region is to write 0 in the first four bytes */
		randomFile.writeInt( 0);
		randomFile.seek( usedLength-REGION_END_OFFSET);
		randomFile.writeInt( initialRegionSize);
		if ( lastFreeRegionOffset!=0)
		{
			randomFile.writeInt( fileSize-usedLength);
			/* No other free regions */
			randomFile.writeInt( 0);
			randomFile.writeInt( 0);
			randomFile.seek( fileSize-REGION_END_OFFSET);
			randomFile.writeInt( fileSize-usedLength);
		}
	}

	private int normalizeRegionSize( int userRegionSize)
		throws DiskAllocatorException
	{
		if ( userRegionSize<0)
			throw new DiskAllocatorException( "Can't allocate negative space");
		userRegionSize+=REGION_OVERHEAD_SIZE;
		if ( ( userRegionSize%minimumRegionSize)!=0)
			userRegionSize=( userRegionSize/minimumRegionSize+1)*minimumRegionSize;
		if ( userRegionSize<minimumRegionSize)
			throw new DiskAllocatorException( "Size calculation overflow");
		return userRegionSize;
	}

	private void writeOverhead()
		throws IOException
	{
		randomFile.seek(0);
		randomFile.writeInt( initialRegionOffset);
		randomFile.writeInt( minimumRegionSize);
		randomFile.writeInt( fileIncrementSize);
		randomFile.writeInt( fileSize);
		randomFile.writeInt( lastFreeRegionOffset);
	}

	/**
	 * This function extends the allocator file so that there is a free region
	 * at the end with at least requestedRegionSize bytes in it.  The file is
	 * extended in multiples of the file increment.
	 * If the last region in the file before it was extended is free, that
	 * region is coalesced with the region added by extending the file.
	 */
	private int extendFile( int requestedRegionSize)
		throws IOException, DiskAllocatorException
	{
		int endRegionSize=0;
		if ( lastFreeRegionOffset!=0)
		{
			randomFile.seek( lastFreeRegionOffset);
			endRegionSize= -randomFile.readInt();
			/* If last region is not free, we don't care about it */
			if ( lastFreeRegionOffset+endRegionSize!=fileSize)
				endRegionSize=0;
		}
		// Determine how many bytes to add to the file
		int addedSize=(( requestedRegionSize-endRegionSize)/fileIncrementSize+
			( ( requestedRegionSize-endRegionSize)%fileIncrementSize==0 ? 0 : 1))*
			fileIncrementSize;
		// If added size would make file too big, throw an exception
		if ( addedSize<0 || (long)addedSize+(long)fileSize>(long)Integer.MAX_VALUE)
			throw new DiskAllocatorException( "File extension overflow");

		// Size of last region in file is the size of end region + added size
		int newLastRegionSize=endRegionSize+addedSize;
		// Do case when we are coalescing last free region
		int newRegionOffset;
		if ( endRegionSize!=0)
		{
			newRegionOffset=lastFreeRegionOffset;
			randomFile.seek( lastFreeRegionOffset+REGION_FREE_PTR_OFFSET);
			lastFreeRegionOffset=randomFile.readInt();
		}
		// else last free region remains unchanged (unless we split added region)
		else
		{
			newRegionOffset=fileSize;
		}
		if ( fileSize+addedSize!=newRegionOffset+newLastRegionSize)
			throw new DiskAllocatorException( "File size mismatch");
		fileSize=fileSize+addedSize;
		randomFile.seek( newRegionOffset);
		randomFile.writeInt( -newLastRegionSize);
		randomFile.writeInt( lastFreeRegionOffset);
		randomFile.writeInt( 0);
		lastFreeRegionOffset=newRegionOffset;
		randomFile.seek( fileSize-REGION_END_OFFSET);
		randomFile.writeInt( newLastRegionSize);

		return newLastRegionSize;
	}
}
