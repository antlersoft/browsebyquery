/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.odb;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import com.antlersoft.util.Semaphore;


/**
 * <p>Provides thread-safe implementations of most methods in DiskAllocator.
 * </p><p>
 * Note that the iterator returned by iterator() is not thread-safe and will be rendered
 * invalid if any changes are made.
 * </p>
 * @author Michael A. MacDonald
 *
 */
public class SafeDiskAllocator extends DiskAllocator {
	
	private Semaphore fileLock = new Semaphore();

	/**
	 * @param file
	 * @param initialRegion
	 * @param chunkSize
	 * @param sizeIncrement
	 * @param creationFlags
	 * @throws IOException
	 * @throws DiskAllocatorException
	 */
	public SafeDiskAllocator(File file, int initialRegion, int chunkSize,
			int sizeIncrement, int creationFlags) throws IOException,
			DiskAllocatorException {
		super(file, initialRegion, chunkSize, sizeIncrement, creationFlags);
	}

	/**
	 * @param file
	 * @throws IOException
	 * @throws DiskAllocatorException
	 */
	public SafeDiskAllocator(File file) throws IOException,
			DiskAllocatorException {
		super(file);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#allocate(int)
	 */
	@Override
	public int allocate(int size) throws IOException, DiskAllocatorException {
		fileLock.enterCritical();
		try {
			return super.allocate(size);
		}
		finally
		{
			fileLock.leaveCritical();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#close()
	 */
	@Override
	public void close() throws IOException {
		fileLock.enterCritical();
		try {
			super.close();
		}
		finally
		{
			fileLock.leaveCritical();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#free(int)
	 */
	@Override
	public void free(int regionOffset) throws IOException,
			DiskAllocatorException {
		fileLock.enterCritical();
		try {
			super.free(regionOffset);
		}
		finally {
			fileLock.leaveCritical();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#getRegionSize(int)
	 */
	@Override
	public int getRegionSize(int regionOffset) throws IOException,
			DiskAllocatorException {
		fileLock.enterProtected();
		try {
			return super.getRegionSize(regionOffset);
		}
		finally {
			fileLock.leaveProtected();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#iterator()
	 */
	@Override
	public Iterator<Integer> iterator() throws IOException {
		fileLock.enterProtected();
		try {
			return super.iterator();
		}
		finally {
			fileLock.leaveProtected();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#read(int, int)
	 */
	@Override
	public byte[] read(int offset, int size) throws IOException,
			DiskAllocatorException {
		fileLock.enterProtected();
		try {
			return super.read(offset, size);			
		}
		finally {
			fileLock.leaveProtected();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#sync()
	 */
	@Override
	public void sync() throws IOException {
		fileLock.enterCritical();
		try {
			super.sync();
		}
		finally {
			fileLock.leaveCritical();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#walkInternalFreeList(java.io.PrintStream)
	 */
	@Override
	public void walkInternalFreeList(PrintStream ps) {
		fileLock.enterProtected();
		try {
			super.walkInternalFreeList(ps);
		}
		finally {
			fileLock.leaveProtected();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#walkRegions(java.io.PrintStream)
	 */
	@Override
	public void walkRegions(PrintStream ps) throws IOException,
			DiskAllocatorException {
		fileLock.enterProtected();
		try
		{
			super.walkRegions(ps);
		}
		finally {
			fileLock.leaveProtected();
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#write(int, byte[])
	 */
	@Override
	public void write(int offset, byte[] toWrite) throws IOException,
			DiskAllocatorException {
		fileLock.enterCritical();
		try {
			super.write(offset, toWrite);
		}
		finally {
			fileLock.leaveCritical();
		}
	}
}
