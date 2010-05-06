
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
import java.io.Serializable;

import java.util.Set;

import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.ObjectStoreException;

class FreeEntryPage implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1308309403287688763L;
	int nextPageOffset;
    int[] freeArray;
    int size;
    transient boolean modified;

    final static int FREE_PAGE_SIZE=1024;

    public FreeEntryPage( int no)
    {
        nextPageOffset=no;
        size=0;
        modified=true;
        freeArray=new int[FREE_PAGE_SIZE];
    }

    int sync( StreamPair streams, int offset)
        throws DiskAllocatorException, IOException
    {
        if ( modified)
        {
            offset=streams.writeObject( this, offset);
            modified=false;
        }
        return offset;
    }
    
    /**
     * Used by validation code to read the whole set of free object indices
     * @param streams Overhead stream pair
     * @param freeSet Set of free object indices
     */
    void populateFreeSet(StreamPair streams, Set<Integer> freeSet)
    {
    	for (int i=0; i<size; i++)
    	{
    		Integer free = freeArray[i];
    		if (! freeSet.add(free))
    		{
    			throw new ObjectStoreException("Getting set of free object indices: duplicate entry " + free);
    		}
    		if (nextPageOffset != 0)
    		{
    			try
    			{
    				((FreeEntryPage)streams.readObject(nextPageOffset)).populateFreeSet(streams, freeSet);
    			}
    			catch (Exception e)
    			{
    				throw new ObjectStoreException("Getting free set of free object indices: getting page at offset "+nextPageOffset, e);
    			}
    		}
    	}
    }
}