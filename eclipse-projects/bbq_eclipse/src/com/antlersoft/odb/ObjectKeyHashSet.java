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
public class ObjectKeyHashSet<E> extends AbstractSet<ObjectRef<E>> implements Set<ObjectRef<E>>, Serializable {
	private ObjectKeyHashMap<E,ObjectRef<E>> m_map;

    /**
	 * Create an empty set
	 */
	public ObjectKeyHashSet() {
		m_map=new ObjectKeyHashMap<E,ObjectRef<E>>();
	}


	/** Create a set containing a Collection */
	public ObjectKeyHashSet( Collection c)
	{
		//TODO: Optimize the creation of the underlying map
		m_map=new ObjectKeyHashMap<E,ObjectRef<E>>();
		addAll( c);
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	public Iterator<ObjectRef<E>> iterator() {
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
	public boolean add(ObjectRef<E> arg0) {
		return m_map.put(arg0, arg0)==null;
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
	public boolean contains(ObjectRef<E> o) {
		return m_map.containsKey(o);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	public boolean remove(ObjectRef<E> o) {
		return m_map.remove(o)!=null || o==null;
	}

	/**
	 * Remove any items that aren't included in another set from this set.
	 * @param to_retain Set of items to keep
	 * @return Collection of any items that were removed from this set (ObjectRef's), or null if this
	 * set was unchanged.
	 */
	public Collection<ObjectRef<E>> retainMembers(ObjectKeyHashSet<E> to_retain) {
		ArrayList<ObjectRef<E>> removed=null;
		for ( Iterator<ObjectRef<E>> i = iterator(); i.hasNext();)
		{
			ObjectRef<E> o=i.next();
			if ( ! to_retain.contains( o))
			{
				if ( removed==null)
					removed=new ArrayList<ObjectRef<E>>();
				removed.add(o);
				i.remove();
			}
		}
		return removed;
	}
}
