package odb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

public class DiskAllocatorStore implements ObjectStore
{
	// TODO: Make synchronization allow more concurrency

	// TODO: Change to make updates to internal state
	// be exception-safe with regard to atomicity

    public DiskAllocatorStore( File file)
		throws ObjectStoreException
	{
		try
		{
			RandomOutputStream dataStream=new RandomOutputStream();
			dataOutputStream=new StreamPair( new DataOutputStream( dataStream), dataStream);
			RandomOutputStream outStream=new RandomOutputStream();
			outputStreams=new StreamPair( new ObjectOutputStream( outStream), outStream);
			RandomInputStream inStream=new RandomInputStream();
			inStream.addBytes( outputStreams.writeObject( new Integer( 1)));
			inputStreams=new StreamPair( new ObjectInputStream( inStream), inStream);

			// Initialize streams with stream header data
			((ObjectInputStream)inputStreams.objectStream).readObject();
			allocator=new DiskAllocator( file, 4, 504, 102400, 0);
		}
		catch ( Exception e)
		{
			throw new ObjectStoreException( "Error creating allocator", e);
		}
		try
		{
			int initialRegion=allocator.getInitialRegion();
			if ( allocator.isNewFile())
			{
				stateRegion=0;
				storeState=new StoreState();
			}
			else
			{
				byte[] indexBuffer=allocator.read( initialRegion,
					allocator.getRegionSize( initialRegion));
				stateRegion=new DataInputStream( new ByteArrayInputStream( indexBuffer)).readInt();
				indexBuffer=allocator.read( stateRegion,
					allocator.getRegionSize( stateRegion));
				storeState=(StoreState)inputStreams.readObject( indexBuffer);
				storeState.dirty=false;
			}
		}
		catch ( Exception retrievingState)
		{
			throw new ObjectStoreException( "Error retrieving state", retrievingState);
		}
	}

	public synchronized ObjectKey insert( Serializable insertObject)
		throws ObjectStoreException
	{
		try
		{
			storeState.dirty=true;
			return getNewObjectKey( outputStreams.writeObject( insertObject));
		}
		catch ( DiskAllocatorException dae)
		{
			throw new ObjectStoreException( "Insert", dae);
		}
		catch ( IOException ioe)
		{
			throw new ObjectStoreException( "Insert", ioe);
		}
	}

    public synchronized Serializable retrieve( ObjectKey retrieveKey)
		throws ObjectStoreException
	{
		Entry entry=getEntry( retrieveKey);
		try
		{
			return (Serializable)( inputStreams.readObject(
				allocator.read( entry.region, entry.size)));
		}
		catch ( DiskAllocatorException dae)
		{
			throw new ObjectStoreException( "Retrieving object", dae);
		}
		catch ( IOException ioe)
		{
			throw new ObjectStoreException( "Retrieving object", ioe);
		}
		catch ( ClassNotFoundException cnf)
		{
			throw new ObjectStoreException( "Retrieving object", cnf);
		}
	}

    public synchronized void update( ObjectKey replaceKey, Serializable toReplace)
		throws ObjectStoreException
	{
		Entry entry=getEntry( replaceKey);
		try
		{
			byte[] replaceBuffer=outputStreams.writeObject( toReplace);
			if ( replaceBuffer.length>entry.size ||
				replaceBuffer.length<=entry.size/2
				&& entry.size>=32)
			{
				allocator.free( entry.region);
				entry.region=allocator.allocate( replaceBuffer.length);
				entry.size=replaceBuffer.length;
				storeState.dirty=true;
			}
			allocator.write( entry.region, replaceBuffer);
		}
		catch ( IOException ioe)
		{
			throw new ObjectStoreException( "Update: ", ioe);
		}
		catch ( DiskAllocatorException dae)
		{
			throw new ObjectStoreException( "Update: ", dae);
		}
	}

    public synchronized void delete( ObjectKey deleteKey)
		throws ObjectStoreException
	{
		Entry entry=getEntry( deleteKey);
		try
		{
			allocator.free( entry.region);
		}
		catch ( Exception e)
		{
			throw new ObjectStoreException( "Free", e);
		}
		entry.region=0;
		storeState.freeEntries.add( new Integer( ((DAKey)deleteKey).index));
	}

    public Serializable getRootObject()
    	throws ObjectStoreException
	{
		return storeState.rootObject;
	}

	public void updateRootObject( Serializable newRoot)
		throws ObjectStoreException
	{
		storeState.rootObject=newRoot;
		storeState.dirty=true;
	}

    public synchronized void sync()
		throws ObjectStoreException
	{
		try
		{
			if ( storeState.dirty)
			{
				if ( stateRegion!=0)
					allocator.free( stateRegion);
				byte[] storeBuffer=outputStreams.writeObject( storeState);
				stateRegion=allocator.allocate( storeBuffer.length);
				allocator.write( stateRegion, storeBuffer);

				((DataOutputStream)dataOutputStream.objectStream).writeInt( stateRegion);
				((DataOutputStream)dataOutputStream.objectStream).flush();
				allocator.write( allocator.getInitialRegion(),
					((RandomOutputStream)dataOutputStream.randomStream).getWrittenBytes());
				storeState.dirty=false;
			}
			allocator.sync();
		}
		catch ( Exception e)
		{
			throw new ObjectStoreException( "Sync error: ", e);
		}
	}

	private DiskAllocator allocator;
	private StoreState storeState;
	private int stateRegion;
	private StreamPair dataOutputStream;
	private StreamPair inputStreams;
	private StreamPair outputStreams;
	private static final int SIZE_INCREMENT=1024;

	private Entry getEntry( ObjectKey ok)
		throws ObjectStoreException
	{
		if ( ! ( ok instanceof DAKey))
		    throw new ObjectStoreException( "Bad key type");
		DAKey key=(DAKey)ok;
		if ( key.index<0 || key.index>storeState.entries.size())
			throw new ObjectStoreException( "Bad key");
		Entry entry=(Entry)storeState.entries.get( key.index);
		if ( entry==null || entry.reuseCount!=key.reuseCount ||
			entry.region==0)
			throw new ObjectStoreException( "Deleted object");
		return entry;
	}

	private DAKey getNewObjectKey( byte[] insertBuffer)
		throws ObjectStoreException, DiskAllocatorException, IOException
	{
		int region=allocator.allocate( insertBuffer.length);
		allocator.write( region, insertBuffer);

		Entry entry;
		int index;
		if ( storeState.freeEntries.size()>0)
		{
			index=((Integer)storeState.freeEntries.getFirst()).intValue();
			storeState.freeEntries.removeFirst();
			entry=(Entry)storeState.entries.get( index);
			if ( entry.region>0)
				throw new ObjectStoreException( "Bad entry free list");
			entry.size=insertBuffer.length;
			entry.region=region;
			entry.reuseCount++;
		}
		else
		{
			entry=new Entry( region, insertBuffer.length);
			index=storeState.entries.size();
			storeState.entries.add( entry);
		}
		return new DAKey( index, entry.reuseCount);
	}

	static class StreamPair
	{
		Object objectStream;
		Object randomStream;

		StreamPair( Object o, Object r)
		{
			objectStream=o;
			randomStream=r;
		}

		byte[] writeObject( Object toWrite)
			throws IOException
		{
			ObjectOutputStream oos=(ObjectOutputStream)objectStream;
			oos.reset();
			oos.writeObject( toWrite);
			oos.flush();
			return ((RandomOutputStream)randomStream).getWrittenBytes();
		}

		Object readObject( byte[] readBytes)
			throws IOException, ClassNotFoundException
		{
			ObjectInputStream ois=(ObjectInputStream)objectStream;
			((RandomInputStream)randomStream).emptyAddBytes( readBytes);
			return ois.readObject();
		}
	}

	static class StoreState implements Serializable
	{
		// TODO: Support multiple entries lists, and ideally
		// they don't all always have to be in RAM
		ArrayList entries;
		LinkedList freeEntries;
		boolean dirty;
		Serializable rootObject;

		StoreState()
		{
			entries=new ArrayList( 2000);
			freeEntries=new LinkedList();
			dirty=true;
			rootObject=null;
		}
	}

    // Entry in the object array
	static class Entry implements Serializable
	{
		int region;
		int size;
		int reuseCount;

		Entry( int r, int s)
		{
			region=r;
			size=s;
			reuseCount=0;
		}
	}

	// DiskAllocatorStore's implementation of ObjectKey
	static class DAKey implements ObjectKey
	{
		int index;
		int reuseCount;

		DAKey( int i, int r)
		{
			index=i;
			reuseCount=r;
		}

		public String toString()
		{
			return Integer.toString( index)+":"+Integer.toString( reuseCount);
		}

		public int hashCode()
		{
			return index;
		}

		public boolean equals( Object t)
		{
			return ( ( t instanceof DAKey) && ((DAKey)t).index==index && ((DAKey)t).reuseCount==reuseCount);
		}
	}

	// DiskAllocatorStore's implementation of InputStream
	static class RandomInputStream extends InputStream
	{
		RandomInputStream()
		{
			position=0;
			count=0;
			buffer=new byte[SIZE_INCREMENT];
		}

		synchronized void emptyAddBytes( byte[] toAdd)
		{
			position=0;
			count=0;
			addBytes( toAdd);
		}

		synchronized void addBytes( byte[] toAdd)
		{
			if ( toAdd.length+count>buffer.length)
			{
				int newSize=( ( ( toAdd.length+count)/SIZE_INCREMENT)+1)*SIZE_INCREMENT;
				byte[] newBuffer=new byte[newSize];
				System.arraycopy( buffer, 0, newBuffer, 0, count);
				buffer=newBuffer;
			}
			System.arraycopy( toAdd, 0, buffer, count, toAdd.length);
			count+=toAdd.length;
		}

		private void packBuffer()
		{
			if ( position==count)
			{
				position=0;
				count=0;
			}
		}

		synchronized public int read()
			throws IOException
		{
			int retVal= -1;
			if ( position<count)
			{
				retVal=buffer[position++];
				if ( retVal<0)
					retVal+=256;
				packBuffer();
			}

			return retVal;
		}

		synchronized public int read( byte[] dest, int offset, int len)
			throws IOException
		{
			int retval= -1;
			if ( position<count)
			{
				if ( len>count-position)
					len=count-position;
				retval=len;
				System.arraycopy( buffer, position, dest, offset, len);
				position+=len;
				packBuffer();
			}
			return retval;
		}

		synchronized public long skip( long n)
			throws IOException
		{
			if ( n>count-position)
				n=count-position;
			position+=n;
			packBuffer();

			return n;
		}

		public int available()
		    throws IOException
		{
			return count-position;
		}

		private int position;
		private int count;
		private byte[] buffer;
	}

	// DiskAllocatorStore's implementation of OutputStream
	static class RandomOutputStream extends ByteArrayOutputStream
	{
		RandomOutputStream()
		{
			super( SIZE_INCREMENT);
		}

		byte[] getWrittenBytes()
			throws IOException
		{
			byte[] retVal=toByteArray();
			reset();
			return retVal;
		}
	}
}
