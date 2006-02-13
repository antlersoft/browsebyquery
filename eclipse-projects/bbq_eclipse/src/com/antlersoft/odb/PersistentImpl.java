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

import java.lang.ref.SoftReference;

/**
 * Publicly opaque class that is included in persistent objects to provide
 * support for persistence.
 */
public class PersistentImpl
{
	public PersistentImpl()
	{
		dirtyReference=null;
		cachedReference=null;
		objectKey=null;
        deleted=false;
	}

    public PersistentImpl( Persistent containing)
    {
        this();
        cachedReference=new SoftReference( containing);
    }

	private Persistent dirtyReference;
	ObjectKey objectKey;
    private boolean deleted;
    private SoftReference cachedReference;

    final Object getCanonical( ObjectRef toUpdate)
    {
    	Persistent result=dirtyReference;
    	if ( result==null)
    	{
    		if ( cachedReference!=null)
    			result=(Persistent)cachedReference.get();
    	}
    	
        if ( result==null)
        {
            if ( objectKey==null)
                throw new ObjectDBException( "Internal: obsolete with no object key");
            result=ObjectDB.getObjectDB().
                getObjectByKey( objectKey);
            PersistentImpl newImpl=result._getPersistentImpl();
            synchronized( toUpdate)
            {
                if ( toUpdate.impl==this)
                    toUpdate.impl=newImpl;
            }
        }
        return result;
    }
    
    final boolean isDirty()
    {
    	return dirtyReference!=null;
    }
    
    final void makeDirty( Persistent p)
    {
    	dirtyReference=p;
    	cachedReference=null;
    }
    
    final void setCached( Persistent p)
    {
    	dirtyReference=null;
    	cachedReference=new SoftReference( p);
    }
    
    final void markDeleted( Persistent p)
    {
    	dirtyReference=p;
    	deleted=true;
    	cachedReference=null;
    }
    
    final boolean isDeleted()
    {
    	return deleted;
    }
    
    final void fromDirty()
    {
    	cachedReference=new SoftReference( dirtyReference);
    	dirtyReference=null;
    }
    
    final void makeObsolete()
    {
    	dirtyReference=null;
    	cachedReference=null;
    }
}