package odb;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

class FromRefCollection implements java.util.Collection 
{
    private Collection _base;
    FromRefCollection( Collection base)
    {
	_base=base;
    }

    public boolean add( Object o)
    {
	return _base.add( new DualRef( o));
    }

    public boolean addAll( Collection c)
    {
	boolean retVal=false;
	synchronized ( c)
	{
	    for ( Iterator i=c.iterator(); i.hasNext(); )
	    {
		retVal=( retVal || add( i.next()));
	    }
	}
	return retVal;
    }

    public void clear()
    {
	_base.clear();
    }

    public boolean contains(java.lang.Object o)
    {
	return _base.contains( o);
    }

    public boolean containsAll(java.util.Collection c)
    { return _base.containsAll( c); }
    public boolean equals(java.lang.Object o)
    { return _base.equals( o); }
    public int hashCode()
    { return _base.hashCode(); }
    public boolean isEmpty()
    { return _base.isEmpty(); }
    public java.util.Iterator iterator()
    { return new FromRefIterator( _base.iterator()); }
    public boolean remove(java.lang.Object o)
    { return _base.remove( o); }
    public boolean removeAll(java.util.Collection c)
    { return _base.removeAll( c); }
    public boolean retainAll(java.util.Collection c)
    {
	return _base.retainAll( c);
    }

    public int size()
    {
	return _base.size();
    }

    public java.lang.Object toArray()[]
    {
	return toArray( new Object[0]);
    }

    public java.lang.Object toArray(java.lang.Object[] o)[]
    {
	synchronized ( _base)
	{
	    if ( o.length<_base.size())
	    {
		o=(Object[])Array.newInstance( o.getClass().getComponentType(),
		    _base.size());
	    }
	    int j=0;
	    for ( Iterator i=_base.iterator(); i.hasNext(); j++)
	    {
		o[j]=i.next();
	    }
	    if ( j<o.length)
	        o[j]=null;
	}
	return o;
    }
    static class FromRefIterator implements java.util.Iterator 
    {
	private Iterator _base;

	FromRefIterator( Iterator base)
	{
	    _base=base;
	}
	public boolean hasNext()
	{ return _base.hasNext(); }
	public java.lang.Object next()
	{ return ((ObjectRef)_base.next()).getReferenced(); }
	public void remove()
	{
	    _base.remove();
	}
    }
}
