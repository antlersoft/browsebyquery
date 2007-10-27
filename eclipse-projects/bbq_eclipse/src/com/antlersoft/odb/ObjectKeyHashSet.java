/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.odb;

import java.io.Serializable;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * A set of unique ObjectRef's based on ObjectKeyHashMap, so that it can be serialized/deserialized
 * efficiently.
 * The objects contained must obey the restrictions on keys for ObjectKeyHashMap:
 * they must be ObjectRef and the referenced objects must not compare .equals unless
 * they are the same object in the database.
 * @author Michael A. MacDonald
 *
 */
public class ObjectKeyHashSet extends AbstractSet implements Set, Serializable {
	private ObjectKeyHashMap m_map;

    static final long serialVersionUID = 4308976621096444042L;
    
    /**
	 * Create an empty set
	 */
	public ObjectKeyHashSet() {
		m_map=new ObjectKeyHashMap();
	}


	/** Create a set containing a Collection */
	public ObjectKeyHashSet( Collection c)
	{
		//TODO: Optimize the creation of the underlying map
		m_map=new ObjectKeyHashMap();
		addAll( c);
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	public Iterator iterator() {
		return m_map.values().iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	public int size() {
		return m_map.size();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	public boolean add(Object arg0) {
		return m_map.put((ObjectRef)arg0, arg0)==null;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#clear()
	 */
	public void clear() {
		m_map.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return m_map.containsKey((ObjectRef)o);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return m_map.remove((ObjectRef)o)!=null || o==null;
	}

	/**
	 * Remove any items that aren't included in another set from this set.
	 * @param to_retain Set of items to keep
	 * @return Collection of any items that were removed from this set (ObjectRef's), or null if this
	 * set was unchanged.
	 */
	public Collection retainMembers(ObjectKeyHashSet to_retain) {
		ArrayList removed=null;
		for ( Iterator i = iterator(); i.hasNext();)
		{
			Object o=i.next();
			if ( ! to_retain.contains( o))
			{
				if ( removed==null)
					removed=new ArrayList();
				removed.add(o);
				i.remove();
			}
		}
		return removed;
	}
}
