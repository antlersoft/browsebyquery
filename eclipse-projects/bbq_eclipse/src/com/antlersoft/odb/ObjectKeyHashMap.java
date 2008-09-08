/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.odb;

import java.io.Serializable;

import java.util.Collection;
import java.util.HashMap;

/**
 * A map using ObjectRef's as keys, with the additional optimization
 * that it generates the hashCode from the underlying ObjectKey, rather
 * than the object itself.  This will prevent the need to pull the whole
 * object into memory to generate the hash for the key or
 * deserialize the map.  It also means that all objects
 * that will serve as keys <i>must be</i> unique
 * with respect to the equals() method for this to work consistently with using
 * the objects themselves as keys.
 * <p>
 * Does not implement all of java.util.Map
 * @author Michael A. MacDonald
 *
 */
public class ObjectKeyHashMap<K,V> implements Serializable {
	
    /**
	 * Map of ObjectKey's to Object; ObjectKey's implementation must have
	 * appropriate hash function.
	 */
	private HashMap<ObjectKey,V> m_backing_map;
	
	/** Create an empty map */
	public ObjectKeyHashMap()
	{
		m_backing_map=new HashMap<ObjectKey,V>();
	}
	
	/** Remove all entries from the map */
	public void clear()
	{
		m_backing_map.clear();
	}
	
	/** Returns true if this map contains a mapping for the specified key. */
	public boolean containsKey( ObjectRef<K> ref)
	{
		return m_backing_map.containsKey( keyFromRef(ref));
	}
	
	/** Returns true if this map contains a mapping for the specified value. */
	public boolean containsValue( V value)
	{
		return m_backing_map.containsValue(value);
	}
	
	/** Returns the object associated with the given key */
	public Object get( ObjectRef<K> ref)
	{
		return m_backing_map.get(keyFromRef(ref));
	}
	
	/** Returns true if the mapping has no entries */
	public boolean isEmpty()
	{
		return m_backing_map.isEmpty();
	}
	
	/**
	 * Adds an object to the map with the key.  Returns the old value associated with the key,
	 * or null if the key was not in the map or if the key was associated with null
	 * @param ref Reference to key object
	 * @param value
	 * @return Old value associated with key, or null if there is non
	 */
	public Object put( ObjectRef<K> ref, V value)
	{
		return m_backing_map.put( keyFromRef(ref), value);
	}
	
	/**
	 * Removes the mapping for the key
	 * @param ref Reference to key object
	 * @return Value that was associated with the key before it was remove, or null if the key was not in the map
	 * (or was associated with null)
	 */
	public Object remove( ObjectRef<K> ref)
	{
		return m_backing_map.remove( keyFromRef(ref));
	}
	
	/**
	 * 
	 * @return The number of entries in the map.
	 */
	public int size()
	{
		return m_backing_map.size();
	}
	
	/**
	 * 
	 * @return Collection of all the values in the map
	 */
	public Collection<V> values()
	{
		return m_backing_map.values();
	}
	
	private ObjectKey keyFromRef( ObjectRef<K> ref)
	{
		return ref.impl==null ? null : ref.impl.objectKey;
	}
}
