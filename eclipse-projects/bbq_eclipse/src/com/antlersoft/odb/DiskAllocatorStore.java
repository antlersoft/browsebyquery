package odb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

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
			byteOutputStream=new ByteArrayOutputStream();
			objectOutputStream=new ObjectOutputStream( byteOutputStream);
			dataOutputStream=new DataOutputStream( byteOutputStream);

			allocator=new DiskAllocator( file);
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
				storeState=(StoreState)(new ObjectInputStream( new ByteArrayInputStream( indexBuffer)).readObject());
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
			objectOutputStream.writeObject( insertObject);
			objectOutputStream.flush();
			byte[] insertBuffer=byteOutputStream.toByteArray();
			objectOutputStream.reset();
			byteOutputStream.reset();
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
			return (Serializable)( new ObjectInputStream(
				new ByteArrayInputStream( allocator.read( entry.region,
				entry.size)))).readObject();
		}
		catch ( Exception dae)
		{
			throw new ObjectStoreException( "Retrieving object", dae);
		}
	}

    public synchronized void update( ObjectKey replaceKey, Serializable toReplace)
		throws ObjectStoreException
	{
		Entry entry=getEntry( replaceKey);
		try
		{
			objectOutputStream.writeObject( toReplace);
			objectOutputStream.flush();
			byte[] replaceBuffer=byteOutputStream.toByteArray();
			objectOutputStream.reset();
			byteOutputStream.reset();
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
		catch ( Exception e)
		{
			throw new ObjectStoreException( "Update: ", e);
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
				allocator.free( stateRegion);
				objectOutputStream.writeObject( storeState);
				objectOutputStream.flush();
				byte[] storeBuffer=byteOutputStream.toByteArray();
				stateRegion=allocator.allocate( storeBuffer.length);
				allocator.write( stateRegion, storeBuffer);
				objectOutputStream.reset();
				byteOutputStream.reset();
				dataOutputStream.writeInt( stateRegion);
				dataOutputStream.flush();
				allocator.write( allocator.getInitialRegion(), byteOutputStream.toByteArray());
				byteOutputStream.reset();
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
	private ObjectOutputStream objectOutputStream;
	private ByteArrayOutputStream byteOutputStream;
	private DataOutputStream dataOutputStream;

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
	}
}