package odb;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class PersistentHashtable extends Hashtable implements Persistent
{
	private PersistentImpl persistentImpl;

	public PersistentHashtable()
	{
		super();
		persistentImpl=new PersistentImpl();
		ObjectDB.setPersistent( this);
	}

	public PersistentHashtable( int s)
	{
		super( s);
		persistentImpl=new PersistentImpl();
		ObjectDB.setPersistent( this);
	}

	public PersistentHashtable( int s, float f)
	{
		super( s, f);
		persistentImpl=new PersistentImpl();
		ObjectDB.setPersistent( this);
	}

	public PersistentHashtable( java.util.Map m)
	{
		super( m.size());
		persistentImpl=new PersistentImpl();
		putAll( m);
		ObjectDB.setPersistent( this);
	}

	public PersistentImpl _getPersistentImpl()
	{
		return persistentImpl;
	}

	//public synchronized void clear();
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
		return new FromRefEntrySet( super.entrySet()); 
	}

    //public synchronized boolean equals(java.lang.Object);
    public synchronized java.lang.Object get( Object key)
	{
		return ((ObjectRef)super.get( key)).getReferenced();
	}

    //public synchronized int hashCode();
    //public boolean isEmpty();
    public java.util.Set keySet()
	{
		return new FromRefSet( super.keySet());
	}

    public synchronized java.util.Enumeration keys()
	{
		return new FromRefEnumeration( super.keys());
	}

    public synchronized java.lang.Object put( Object key, Object value)
    {
		return ((ObjectRef)super.put( new DualRef( key), new DualRef( value))).
			getReferenced();
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
		return ((ObjectRef)super.get( key)).getReferenced();
	}

    //public int size();
    public java.util.Collection values()
	{
		return new FromRefCollection( super.values());
	}
}