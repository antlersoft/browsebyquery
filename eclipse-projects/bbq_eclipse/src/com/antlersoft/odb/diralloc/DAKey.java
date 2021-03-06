
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

import com.antlersoft.odb.ObjectKey;
import com.antlersoft.odb.ObjectStoreException;

class DAKey implements ObjectKey, Comparable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2551671276277657088L;
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

	static DAKey fromString(String keyString)
		throws ObjectStoreException
	{
		int colonIndex = keyString.indexOf(':');
		if (colonIndex <= 0)
			throw new ObjectStoreException("Invalid key string: " + keyString);
		String indexString = keyString.substring(0, colonIndex);
		String reuseString = keyString.substring(colonIndex + 1);
		try
		{
			return new DAKey(Integer.parseInt(indexString),
					Integer.parseInt(reuseString));
		}
		catch (NumberFormatException nfe)
		{
			throw new ObjectStoreException("Invalid key string: " + keyString, nfe);
		}
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
