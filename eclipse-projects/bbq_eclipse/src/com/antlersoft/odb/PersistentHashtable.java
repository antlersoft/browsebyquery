package com.antlersoft.odb;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class PersistentHashtable extends Hashtable implements Persistent
{
	private transient PersistentImpl persistentImpl;

	public PersistentHashtable()
	{
		super();
		persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
	}

	public PersistentHashtable( int s)
	{
		super( s);
		persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
	}

	public PersistentHashtable( int s, float f)
	{
		super( s, f);
		persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
	}

	public PersistentHashtable( java.util.Map m)
	{
		super( m.size());
		persistentImpl=new PersistentImpl();
		putAll( m);
		ObjectDB.makePersistent( this);
	}

	public synchronized PersistentImpl _getPersistentImpl()
	{
		if ( persistentImpl==null)
			persistentImpl=new PersistentImpl();
		return persistentImpl;
	}

	public synchronized void clear()
	{
		ObjectDB.makeDirty( this);
		clear();
	}

    public synchronized java.lang.Object clone()
	{
		return new PersistentHashtable( this);
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
		return new FromRefEntrySet( this, super.entrySet());
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
		return new FromRefSet( this, super.keySet());
	}

    public synchronized java.util.Enumeration keys()
	{
		return new FromRefEnumeration( super.keys());
	}

    public synchronized java.lang.Object put( Object key, Object value)
    {
		ObjectDB.makeDirty( this);
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
		ObjectDB.makeDirty( this);
		return ((ObjectRef)super.get( key)).getReferenced();
	}

    //public int size();
    public java.util.Collection values()
	{
		return new FromRefCollection( this, super.values());
	}
}
