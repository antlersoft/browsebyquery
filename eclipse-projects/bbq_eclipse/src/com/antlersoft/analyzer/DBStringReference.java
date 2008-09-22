
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
package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectDB;

public class DBStringReference extends DBReference
{
	final static String SRTARGET="SRTARGET";
	
    DBStringReference( DBMethod method, DBStringConstant constant, int l)
    {
        super( method, constant, l);
        ObjectDB.makePersistent( this);
     }

    public DBStringConstant getTarget()
    {
        return (DBStringConstant)target.getReferenced();
    }

    public String toString()
    {
		return "Reference to "+getTarget().toString()+" from "+getSource().toString()+" at line "+String.valueOf( lineNumber);
    }

	public int hashCode()
	{
		return getSource().hashCode()^lineNumber^target.hashCode();
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof DBStringReference)
		{
			DBStringReference f=(DBStringReference)toCompare;

			return f.getSource().equals( getSource()) && f.lineNumber==lineNumber &&
				f.getTarget().equals( getTarget());
		}
		return false;
	}
}