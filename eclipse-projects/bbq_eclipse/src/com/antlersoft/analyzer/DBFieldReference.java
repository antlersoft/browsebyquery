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

import com.antlersoft.odb.ObjectDB;

public class DBFieldReference extends DBReference
{
	static final String FRTARGET="FRTARGET";
	
    DBFieldReference( ObjectDB db, DBMethod s, DBField t, int l, boolean write)
    {
		super( s, t, l);
		writeReference=write;
		db.makePersistent(this);
    }

    boolean writeReference;

    public boolean isWrite() { return writeReference; }

	public DBField getTarget()
	{
		return (DBField)target.getReferenced();
	}

    public String toString()
    {
		return ( writeReference ? "Write" : "Read")+" reference to "+getTarget().toString()+" from "+getSource().toString()+" at line "+String.valueOf( lineNumber);
    }

	public int hashCode()
	{
		return getSource().hashCode()^lineNumber^target.hashCode()+( writeReference ? 0 : 63);
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof DBFieldReference)
		{
			DBFieldReference f=(DBFieldReference)toCompare;

			return f.getSource().equals( getSource()) && f.lineNumber==lineNumber &&
				f.writeReference==writeReference &&
				f.getTarget().equals( getTarget());
		}
		return false;
	}
}
