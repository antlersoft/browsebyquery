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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class PersistentHashtable extends Hashtable implements Persistent
{
	private transient PersistentImpl persistentImpl;

	public PersistentHashtable(ObjectDB db)
	{
		super();
		persistentImpl=new PersistentImpl();
		db.makePersistent( this);
	}

	public PersistentHashtable(ObjectDB db, int s)
	{
		super( s);
		persistentImpl=new PersistentImpl();
		db.makePersistent( this);
	}

	public PersistentHashtable( ObjectDB db, int s, float f)
	{
		super( s, f);
		persistentImpl=new PersistentImpl();
		db.makePersistent( this);
	}

	public PersistentHashtable( ObjectDB db, java.util.Map m)
	{
		super( m.size());
		persistentImpl=new PersistentImpl();
		putAll( m);
		db.makePersistent( this);
	}

	public synchronized PersistentImpl _getPersistentImpl()
	{
		if ( persistentImpl==null)
			persistentImpl=new PersistentImpl();
		return persistentImpl;
	}

	public synchronized void clear()
	{
		_getPersistentImpl().db.makeDirty(this);
		super.clear();
	}

    public synchronized java.lang.Object clone()
	{
		return new PersistentHashtable( _getPersistentImpl().db, this);
	}
    //public synchronized boolean contains(java.lang.Object);
    //public synchronized boolean containsKey(java.lang.Object);
    //public boolean containsValue(java.lang.Object);
    public synchronized java.util.Enumeration elements()
	{
		return new FromRefEnumeration( super.elements());
	}

    public java.util.Set entrySet()
	{
		return new FromRefEntrySet( _getPersistentImpl().db,this, super.entrySet());
	}

    //public synchronized boolean equals(java.lang.Object);
    public synchronized java.lang.Object get( Object key)
	{
		ObjectRef ref=(ObjectRef)super.get( key);
		if ( ref==null)
			return null;
		return ref.getReferenced();
	}

    //public synchronized int hashCode();
    //public boolean isEmpty();
    public java.util.Set keySet()
	{
		return new FromRefSet( _getPersistentImpl().db, this, super.keySet());
	}

    public synchronized java.util.Enumeration keys()
	{
		return new FromRefEnumeration( super.keys());
	}

    public synchronized java.lang.Object put( Object key, Object value)
    {
		_getPersistentImpl().db.makeDirty( this);
		if ( ! ( key instanceof ObjectRef))
		    key=new DualRef( key);
		if ( ! ( value instanceof ObjectRef))
			value=new DualRef( value);
		Object putResult=super.put( key, value);
		if ( putResult!=null)
			return ((ObjectRef)putResult).getReferenced();
		return null;
	}

	public synchronized void putAll( Map m)
	{
		synchronized ( m)
		{
			for ( Iterator i=m.entrySet().iterator(); i.hasNext();)
			{
				Map.Entry entry=(Map.Entry)i.next();
				put( entry.getKey(), entry.getValue());
			}
		}
	}

    public synchronized java.lang.Object remove( Object key)
	{
		_getPersistentImpl().db.makeDirty( this);
		return ((ObjectRef)super.get( key)).getReferenced();
	}

    //public int size();
    public java.util.Collection values()
	{
		return new FromRefCollection( _getPersistentImpl().db, this, super.values());
	}
}
