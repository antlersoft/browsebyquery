
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

import com.antlersoft.odb.ObjectKey;

class DAKey implements ObjectKey, Comparable
{
	int index;
	int reuseCount;

	DAKey( int i, int r)
	{
		index=i;
		reuseCount=r;
	}

    public int compareTo( Object toCompare)
    {
        DAKey other=(DAKey)toCompare;
        int r=index-other.index;
        if ( r==0)
            r=reuseCount-other.reuseCount;
        return r;
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
