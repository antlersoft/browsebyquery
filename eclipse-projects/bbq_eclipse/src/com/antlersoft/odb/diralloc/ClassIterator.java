
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