package odb;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.Iterator;

class FromRefEntrySet implements java.util.Set 
{
    private Set _fromMap;
    private ObjectRef _containing;

    FromRefEntrySet( Persistent containing, Set fromMap)
    {
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
	ObjectDB.makeDirty( _containing.getReferenced());
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
	return new EntrySetIterator( _containing, _fromMap.iterator());
    }

    public boolean remove(java.lang.Object o)
    {
	ObjectDB.makeDirty( _containing.getReferenced());
	return _fromMap.remove( o);
    }

    public boolean removeAll(java.util.Collection c)
    {
	ObjectDB.makeDirty( _containing.getReferenced());
	return _fromMap.removeAll( c);
    }

    public boolean retainAll(java.util.Collection c)
    {
	ObjectDB.makeDirty( _containing.getReferenced());
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
		o[j]=new Entry( (java.util.Map.Entry)i.next());
	    }
	    if ( j<o.length)
	        o[j]=null;
	}
	return o;
    }

    static class Entry implements java.util.Map.Entry 
    {
	private java.util.Map.Entry _base;
	Entry( java.util.Map.Entry base)
	{ _base=base; }
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
	private Iterator _base;
	private ObjectRef _containing;

	EntrySetIterator( ObjectRef containing, Iterator base)
	{
	    _base=base;
	    _containing=containing;
	}
	public boolean hasNext()
	{ return _base.hasNext(); }
	public java.lang.Object next()
	{ return new Entry( (java.util.Map.Entry)_base.next()); }
	public void remove()
	{
	    ObjectDB.makeDirty( _containing.getReferenced());
	    _base.remove();
	}
    }
}


