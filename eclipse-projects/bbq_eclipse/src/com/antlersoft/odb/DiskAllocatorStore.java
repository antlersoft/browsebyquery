/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.odb;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.antlersoft.util.RandomInputStream;
import com.antlersoft.util.RandomOutputStream;

public class DiskAllocatorStore implements ObjectStore
{
	// TODO: Make synchronization allow more concurrency

	// TODO: Change to make updates to internal state
	// be exception-safe with regard to atomicity

    public DiskAllocatorStore( File file, int initialRegionSize, int chunkSize,
        int incrementSize, int allocatorFlags, ObjectStreamCustomizer c)
		throws ObjectStoreException
	{
		try
		{
            customizer=c;
			RandomOutputStream dataStream=new RandomOutputStream();
			dataOutputStream=new StreamPair( new DataOutputStream( dataStream),
                dataStream);
			RandomOutputStream outStream=new RandomOutputStream();
			outputStreams=new StreamPair(
                customizer.createObjectOutputStream( outStream), outStream);
			RandomInputStream inStream=new RandomInputStream();
			inStream.emptyAddBytes( outputStreams.writeObject( new Integer( 1)));
			inputStreams=new StreamPair(
                customizer.createObjectInputStream( inStream), inStream);

			// Initialize streams with stream header data
			((ObjectInputStream)inputStreams.objectStream).readObject();
			allocator=new SafeDiskAllocator( file, initialRegionSize,
                chunkSize, incrementSize, allocatorFlags);
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

    public DiskAllocatorStore( File file)
    {
        this( file, 4, 504, 102400, 0,
            new ObjectStreamCustomizer.BaseCustomizer());
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
        catch ( DiskAllocatorException dae)
        {
			throw new ObjectStoreException( "Sync error: ", dae);
        }
		catch ( IOException e)
		{
			throw new ObjectStoreException( "Sync error: ", e);
		}
	}

    public void close()
        throws ObjectStoreException
    {
        sync();
        allocator=null;
    }

    public void rollback()
        throws ObjectStoreException
    {
        throw new ObjectStoreException(
            "DiskAllocatorStore does not support rollback");
    }

	private SafeDiskAllocator allocator;
	private StoreState storeState;
	private int stateRegion;
	private StreamPair dataOutputStream;
	private StreamPair inputStreams;
	private StreamPair outputStreams;
    private ObjectStreamCustomizer customizer;

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

	class StreamPair
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
			customizer.resetObjectOutputStream( oos);
			oos.writeObject( toWrite);
			oos.flush();
			return ((RandomOutputStream)randomStream).getWrittenBytes();
		}

		Object readObject( byte[] readBytes)
			throws IOException, ClassNotFoundException
		{
			ObjectInputStream ois=(ObjectInputStream)objectStream;
            customizer.resetObjectInputStream( ois);
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

	@Override
	public String keyToString(ObjectKey key) throws ObjectStoreException {
		return ((DAKey)key).toString();
	}

	@Override
	public ObjectKey stringToKey(String str) throws ObjectStoreException {
		int colonIndex = str.indexOf(':');
		if (colonIndex <= 0)
			throw new ObjectStoreException("Invalid key string: " + str);
		String indexString = str.substring(0, colonIndex);
		String reuseString = str.substring(colonIndex + 1);
		try
		{
			return new DAKey(Integer.parseInt(indexString),
					Integer.parseInt(reuseString));
		}
		catch (NumberFormatException nfe)
		{
			throw new ObjectStoreException("Invalid key string: " + str, nfe);
		}
	}
}
