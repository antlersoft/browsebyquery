
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

import java.io.Serializable;

class UniqueKey implements Comparable, Serializable
{
    static DAKey largestDAKey=new DAKey( Integer.MAX_VALUE, Integer.MAX_VALUE);
    static DAKey smallestDAKey=new DAKey( Integer.MIN_VALUE, Integer.MIN_VALUE);

    UniqueKey( Comparable base, DAKey ref)
    {
        this.base=base;
        this.index=ref.index;
        this.reuseCount=ref.reuseCount;
    }

    public int compareTo(Object o)
    {
        UniqueKey other=(UniqueKey)o;

        int result=base.compareTo( other.base);
        if ( result==0)
        {
            result=index-other.index;
            if ( result==0)
                result=reuseCount-other.reuseCount;
        }
        return result;
    }

    Comparable base;
    private int index;
    private int reuseCount;
}