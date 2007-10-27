/*
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
 */
package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectRef;

public class DBCall extends DBReference
{
    DBCall( DBMethod s, DBMethod t, int l)
    {
        super( s, l);	
        target=new ObjectRef( t);
        
    }

    ObjectRef target;

	public DBMethod getTarget()
	{
		return (DBMethod)target.getReferenced();
	}

    public String toString()
    {
        return "Call to "+getTarget().toString()+" from "+getSource().toString()+" at line "+String.valueOf( lineNumber);
    }

	public int hashCode()
	{
		return getSource().hashCode()^lineNumber^target.hashCode();
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof DBCall)
		{
			DBCall f=(DBCall)toCompare;

			return f.getSource().equals( getSource()) && f.lineNumber==lineNumber &&
				f.getTarget().equals( getTarget());
		}
		return false;
	}
}
