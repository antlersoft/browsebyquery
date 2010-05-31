
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
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
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.diralloc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.concurrent.locks.ReentrantLock;

import com.antlersoft.odb.DiskAllocator;
import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.ObjectStreamCustomizer;
import com.antlersoft.util.NetByte;
import com.antlersoft.util.RandomInputStream;
import com.antlersoft.util.RandomOutputStream;

class StreamPair
{
    StreamPair( DiskAllocator a, ObjectStreamCustomizer c)
        throws IOException, ClassNotFoundException
    {
        super();
        allocator=a;
        allocatorLock = new ReentrantLock();
        chunkSize=allocator.getChunkSize();
        customizer=c;
        randomOutput=new RandomOutputStream();
        objectOutput=customizer.createObjectOutputStream( randomOutput);
        randomInput=new RandomInputStream();
        // Get past header portion of object stream
        objectOutput.writeObject( new Integer( 1));
        randomInput.emptyAddBytes( randomOutput.getWrittenBytes());
        objectInput=customizer.createObjectInputStream( randomInput);
        objectInput.readObject();
    }

    StreamPair( DiskAllocator a)
        throws IOException, ClassNotFoundException
    {
        this( a, ObjectStreamCustomizer.BASE_CUSTOMIZER);
    }

    int read2Ints( int offset)
        throws IOException, DiskAllocatorException
    {
        byte[] intBuffer=allocator.read( offset, 8);
        first=NetByte.quadToInt( intBuffer, 0);
        second=NetByte.quadToInt( intBuffer, 4);
        return first;
    }

    void write2Ints( int offset, int a, int b)
        throws IOException, DiskAllocatorException
    {
        byte[] intBuffer=new byte[8];
        NetByte.intToQuad( a, intBuffer, 0);
        NetByte.intToQuad( b, intBuffer, 4);
        allocator.write( offset, intBuffer);
    }

    int writeObject( Object toWrite, int offset)
        throws IOException, DiskAllocatorException
    {
        byte[] writeBuffer=writeObject( toWrite);
        if ( offset!=0)
        {
            int oldSize=allocator.getRegionSize( offset);
            if ( oldSize<writeBuffer.length ||
                oldSize-writeBuffer.length>=chunkSize)
            {
                allocator.free( offset);
                offset=allocator.allocate( writeBuffer.length);
            }
        }
        else
            offset=allocator.allocate( writeBuffer.length);

        allocator.write( offset, writeBuffer);
        return offset;
    }

    int writeObjectWithPrefix( Object toWrite, int offset, int a, int b)
        throws IOException, DiskAllocatorException
    {
        byte[] intBuffer=new byte[8];
        NetByte.intToQuad( a, intBuffer, 0);
        NetByte.intToQuad( b, intBuffer, 4);
        randomOutput.write( intBuffer);
        byte[] writeBuffer=writeObject( toWrite);
        if ( offset!=0)
        {
            int oldSize=allocator.getRegionSize( offset);
            if ( oldSize<writeBuffer.length ||
                oldSize-writeBuffer.length>=chunkSize)
            {
                allocator.free( offset);
                offset=allocator.allocate( writeBuffer.length);
            }
        }
        else
            offset=allocator.allocate( writeBuffer.length);
        allocator.write( offset, writeBuffer);
        return offset;
    }

    Object readObject( int offset)
        throws IOException, DiskAllocatorException, ClassNotFoundException
    {
        byte[] readBuffer=allocator.read( offset,
            allocator.getRegionSize( offset));
        return readObject( readBuffer, 0, readBuffer.length);
    }

    Object readObjectWithPrefix( int offset)
        throws IOException, DiskAllocatorException, ClassNotFoundException
    {
        byte[] objectBuffer=allocator.read( offset, allocator.getRegionSize(
            offset));
        first=NetByte.quadToInt( objectBuffer, 0);
        second=NetByte.quadToInt( objectBuffer, 4);
        return readObject( objectBuffer, 8, objectBuffer.length-8);
    }
    
    /**
     * Read an "immovable" object; one that might vary in size, but once created
     * will always have the same object address.  Objects that are too big for the original
     * allocation will automatically be extended onto an overflow page, at the cost
     * of some efficiency.
     * @param offset
     * @return Object read
     * @throws DiskAllocatorException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Object readImmovableObject( int offset)
    throws DiskAllocatorException, IOException, ClassNotFoundException
    {
    	byte[] readBuffer=allocator.read( offset, allocator.getRegionSize( offset));
    	customizer.resetObjectInputStream( objectInput);
    	randomInput.emptyAddBytes( readBuffer, 4, readBuffer.length-4);
    	int secondOffset=NetByte.quadToInt( readBuffer, 0);
    	if ( secondOffset!=0)
    	{
    		readBuffer=allocator.read( secondOffset, allocator.getRegionSize( secondOffset));
    		randomInput.addBytes( readBuffer, 0, readBuffer.length);
    	}
    	return objectInput.readObject();
    }

    /**
     * Write an "immovable" object; one that might vary in size, but once created
     * will always have the same object address.  Objects that are too big for the original
     * allocation will automatically be extended onto an overflow page, at the cost
     * of some efficiency.
     * @param toWrite
     * @param offset
     * @return
     * @throws IOException
     * @throws DiskAllocatorException
     */
    int writeImmovableObject( Object toWrite, int offset)
    throws IOException, DiskAllocatorException
	{
	    byte[] writeBuffer=writeObject( toWrite);
	    byte[] offsetBuffer;
	    int overflowOffset=0;
	    if ( offset==0)
	    {
	    	offset=allocator.allocate( DirectoryAllocator.FAVORED_CHUNK_SIZE);
	    	offsetBuffer=new byte[4];
	    }
	    else
	    {
	    	offsetBuffer=allocator.read( offset, 4);
	    	overflowOffset=NetByte.quadToInt( offsetBuffer, 0);
	    }
	    int firstSize=allocator.getRegionSize( offset);
	    if ( firstSize-4>=writeBuffer.length)
	    {
	    	NetByte.intToQuad( 0, offsetBuffer, 0);
	    	allocator.write( offset, offsetBuffer);
	    	allocator.write( offset+4, writeBuffer);
	    	if ( overflowOffset!=0)
	    		free( overflowOffset);
	    }
	    else
	    {
	    	int overflowSize=writeBuffer.length-( firstSize-4);
	    	if ( overflowOffset==0)
	    	{
	    		overflowOffset=allocator.allocate( overflowSize);
	    	}
	    	else
	    	{
	    		int oldSize=allocator.getRegionSize( overflowOffset);
		        if ( oldSize<overflowSize ||
			            oldSize-overflowSize>=chunkSize)
			    {
		            allocator.free( overflowOffset);
		            overflowOffset=allocator.allocate( writeBuffer.length);
		        }
	    	}
	    	NetByte.intToQuad( overflowOffset, offsetBuffer, 0);
	    	byte[] firstBuffer=new byte[firstSize-4];
	    	byte[] secondBuffer=new byte[overflowSize];
	    	System.arraycopy( writeBuffer, 0, firstBuffer, 0, firstSize-4);
	    	System.arraycopy( writeBuffer, firstSize-4, secondBuffer, 0, overflowSize);
	    	allocator.write( offset, offsetBuffer);
	    	allocator.write( offset+4, firstBuffer);
	    	allocator.write( overflowOffset, secondBuffer);
	    }
	    return offset;
	}

    /**
     * Read an "immovable" object; one that might vary in size, but once created
     * will always have the same object address.
     * @param offset
     * @throws IOException
     * @throws DiskAllocatorException
     */
    void freeImmovableObject( int offset)
    throws IOException, DiskAllocatorException
    {
    	byte[] secondBuffer=allocator.read( offset, 4);
    	allocator.free( offset);
    	offset=NetByte.quadToInt( secondBuffer, 0);
    	if ( offset!=0)
    		allocator.free( offset);
    }

	byte[] writeObject( Object toWrite)
		throws IOException
	{
		customizer.resetObjectOutputStream( objectOutput);
		objectOutput.writeObject( toWrite);
		objectOutput.flush();
		return randomOutput.getWrittenBytes();
	}

	Object readObject( byte[] readBytes, int offset, int length)
		throws IOException, ClassNotFoundException
	{
        customizer.resetObjectInputStream( objectInput);
		randomInput.emptyAddBytes( readBytes, offset, length);
		return objectInput.readObject();
	}

    void free( int offset)
        throws IOException, DiskAllocatorException
    {
        allocator.free( offset);
    }

    void sync()
        throws IOException
    {
        allocator.sync();
    }

    void close()
        throws IOException
    {
        objectInput.close();
        objectOutput.close();
        allocator.close();
    }
    
    final void enterProtected()
    {
    	allocatorLock.lock();
    }
    
    final void leaveProtected()
    {
    	allocatorLock.unlock();
    }
    
    final void enterCritical()
    {
    	allocatorLock.lock();
    }
    
    final void leaveCritical()
    {
    	allocatorLock.unlock();
    }
    
    int first;
    int second;

    private ReentrantLock allocatorLock;
    DiskAllocator allocator;
    private ObjectStreamCustomizer customizer;
    private int chunkSize;
    private RandomInputStream randomInput;
    private RandomOutputStream randomOutput;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
}