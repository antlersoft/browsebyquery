
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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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

    int first;
    int second;

    DiskAllocator allocator;
    private ObjectStreamCustomizer customizer;
    private int chunkSize;
    private RandomInputStream randomInput;
    private RandomOutputStream randomOutput;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
}