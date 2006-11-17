
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

import java.io.Serializable;

class UniqueKey implements Comparable, Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6081208245894853943L;
	static DAKey largestDAKey=new DAKey( Integer.MAX_VALUE, Integer.MAX_VALUE);
    static DAKey smallestDAKey=new DAKey( -1, -1);

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