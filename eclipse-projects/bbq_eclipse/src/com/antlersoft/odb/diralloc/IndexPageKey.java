
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

class IndexPageKey implements Cloneable
{
    Index index;
    int offset;
    IndexPageKey parent;

    IndexPageKey( Index i, int o, IndexPageKey p)
    {
        index=i;
if ( o==0)
throw new com.antlersoft.odb.ObjectStoreException( "Bad offset");
        offset=o;
        parent=p;
    }

    public int hashCode()
    {
        return System.identityHashCode( index)^offset;
    }

    public boolean equals( Object toCompare)
    {
        if ( toCompare==null || ! ( toCompare instanceof IndexPageKey))
            return false;
        IndexPageKey key=(IndexPageKey)toCompare;
        return index==key.index && offset==key.offset;
    }
}