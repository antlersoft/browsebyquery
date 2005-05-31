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
package com.antlersoft.odb;

/**
 * Publicly opaque class that is included in persistent objects to provide
 * support for persistence.
 */
public class PersistentImpl
{
	public PersistentImpl()
	{
		dirty=false;
		objectKey=null;
        obsolete=false;
        deleted=false;
        cachedReference=null;
	}

    public PersistentImpl( Persistent containing)
    {
        super();
        cachedReference=containing;
    }

	boolean dirty;
	ObjectKey objectKey;
    boolean obsolete;
    boolean deleted;
    Persistent cachedReference;

    Object getCanonical( ObjectRef toUpdate)
    {
        if ( obsolete || cachedReference==null)
        {
            if ( objectKey==null)
                throw new ObjectDBException( "Internal: obsolete with no object key");
            cachedReference=ObjectDB.getObjectDB().
                getObjectByKey( objectKey);
            PersistentImpl newImpl=cachedReference._getPersistentImpl();
            synchronized( toUpdate)
            {
                if ( toUpdate.impl==this)
                    toUpdate.impl=newImpl;
            }
        }
        return cachedReference;
    }
}