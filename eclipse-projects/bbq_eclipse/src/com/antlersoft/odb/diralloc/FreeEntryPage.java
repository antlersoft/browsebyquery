
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
import java.io.Serializable;

import com.antlersoft.odb.DiskAllocatorException;

class FreeEntryPage implements Serializable
{
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
}