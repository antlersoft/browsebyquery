
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.diralloc;

import java.io.IOException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.antlersoft.util.NetByte;

import com.antlersoft.odb.DiskAllocator;
import com.antlersoft.odb.DiskAllocatorException;

class ClassIterator implements Iterator
{
    private DiskAllocator allocator;
    private Iterator allocatorIterator;

    ClassIterator( DiskAllocator a)
        throws IOException
    {
        allocator=a;
        allocatorIterator=allocator.iterator();
    }

    public boolean hasNext()
    {
        return allocatorIterator.hasNext();
    }

    public Object next()
        throws NoSuchElementException
    {
        synchronized ( allocator)
        {
            int offset=((Integer)allocatorIterator.next()).intValue();
            byte[] prefix;
            try
            {
                prefix=allocator.read( offset, 8);
            }
            catch ( IOException ioe)
            {
                throw new IllegalStateException(
                    "ClassIterator: Allocator I/O error");
            }
            catch ( DiskAllocatorException dae)
            {
                throw new IllegalStateException(
                    "ClassIterator: Allocator error");
            }
            return new DAKey( NetByte.quadToInt( prefix, 0),
                NetByte.quadToInt( prefix, 4));
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}