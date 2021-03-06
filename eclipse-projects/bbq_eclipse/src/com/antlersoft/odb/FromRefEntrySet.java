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

import java.lang.reflect.Array;
import java.util.Set;
import java.util.Iterator;

class FromRefEntrySet implements java.util.Set
{
	private ObjectDB _db;
    private Set _fromMap;
    private ObjectRef _containing;

    FromRefEntrySet( ObjectDB db, Persistent containing, Set fromMap)
    {
    	_db = db;
	_fromMap=fromMap;
	_containing=new ObjectRef( containing);
    }

    // Not supported so we don't have to change
    public boolean add( Object toAdd)
    {
	return _fromMap.add( toAdd);
    }

    // Not supported so we don't have to change
    public boolean addAll(java.util.Collection toAdd)
    {
	return _fromMap.addAll( toAdd);
    }

    public void clear()
    {
	_db.makeDirty( (Persistent)_containing.getReferenced());
	_fromMap.clear();
    }

    public boolean contains(java.lang.Object o)
    {
	return _fromMap.contains( o);
    }

    public boolean containsAll(java.util.Collection c)
    {
	return _fromMap.containsAll( c);
    }

    public boolean equals(java.lang.Object o)
    {
	return _fromMap.equals( o);
    }

    public int hashCode()
    {
	return _fromMap.hashCode();
    }

    public boolean isEmpty()
    {
	return _fromMap.isEmpty();
    }

    public java.util.Iterator iterator()
    {
	return new EntrySetIterator( _db, _containing, _fromMap.iterator());
    }

    public boolean remove(java.lang.Object o)
    {
	_db.makeDirty( (Persistent)_containing.getReferenced());
	return _fromMap.remove( o);
    }

    public boolean removeAll(java.util.Collection c)
    {
	_db.makeDirty( (Persistent)_containing.getReferenced());
	return _fromMap.removeAll( c);
    }

    public boolean retainAll(java.util.Collection c)
    {
	_db.makeDirty( (Persistent)_containing.getReferenced());
	return _fromMap.retainAll( c);
    }

    public int size()
    {
	return _fromMap.size();
    }

    public java.lang.Object toArray()[]
    {
	return toArray( new Object[0]);
    }

    public java.lang.Object toArray(java.lang.Object[] o)[]
    {
	synchronized ( _fromMap)
	{
	    if ( o.length<_fromMap.size())
	    {
		o=(Object[])Array.newInstance( o.getClass().getComponentType(),
		    _fromMap.size());
	    }
	    int j=0;
	    for ( Iterator i=_fromMap.iterator(); i.hasNext(); j++)
	    {
		o[j]=new Entry( _db, (java.util.Map.Entry)i.next());
	    }
	    if ( j<o.length)
	        o[j]=null;
	}
	return o;
    }

    static class Entry implements java.util.Map.Entry
    {
    	private ObjectDB _db;
	private java.util.Map.Entry _base;
	Entry( ObjectDB db, java.util.Map.Entry base)
	{
		_db = db;
		_base=base;
	}
	public boolean equals(java.lang.Object o)
	{ return _base.equals( o); }
	public java.lang.Object getKey()
	{ return ((ObjectRef)_base.getKey()).getReferenced(); }
	public java.lang.Object getValue()
	{ return ((ObjectRef)_base.getValue()).getReferenced(); }
	public int hashCode()
	{ return _base.hashCode(); }
	public java.lang.Object setValue(java.lang.Object o)
	{ return _base.setValue( new DualRef( o)); }
    }
    static class EntrySetIterator implements java.util.Iterator
    {
    	private ObjectDB _db;
	private Iterator _base;
	private ObjectRef _containing;

	EntrySetIterator( ObjectDB db, ObjectRef containing, Iterator base)
	{
		_db = db;
	    _base=base;
	    _containing=containing;
	}
	public boolean hasNext()
	{ return _base.hasNext(); }
	public java.lang.Object next()
	{ return new Entry( _db, (java.util.Map.Entry)_base.next()); }
	public void remove()
	{
	    _db.makeDirty( (Persistent)_containing.getReferenced());
	    _base.remove();
	}
    }
}


