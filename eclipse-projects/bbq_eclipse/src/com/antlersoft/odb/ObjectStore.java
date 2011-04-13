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

import java.io.Serializable;

/**
 * Defines an interface between the object database and an object store.
 * An object store does not know about persistent objects, but it knows
 * about serializable objects and ObjectKeys.  The ObjectStore defines
 * an implementation for the ObjectKey interface.
 */
public interface ObjectStore
{
    public ObjectKey insert( Serializable insertObject)
		throws ObjectStoreException;
    public Serializable retrieve( ObjectKey retrieveKey)
		throws ObjectStoreException;
    public void update( ObjectKey replaceKey, Serializable toReplace)
		throws ObjectStoreException;
    public void delete( ObjectKey deleteKey)
		throws ObjectStoreException;
	public Serializable getRootObject()
		throws ObjectStoreException;
	public void updateRootObject( Serializable newRoot)
		throws ObjectStoreException;
    public void sync()
		throws ObjectStoreException;
    public void rollback()
        throws ObjectStoreException;
    public void close()
        throws ObjectStoreException;
    /**
     * Return a string representation of the given key.  If the key was not
     * created by this object store, or is for a deleted object,
     * the implementation may throw an exception or return a string that can not
     * be converted back to an ObjectKey.  The implementation will not return
     * a null string.
     * <p>
     * In general, the returned value would be used for communicating with an
     * external system and not persisted in the database.  Persisting an actual
     * ObjectRef will always be more efficient.
     * @param key Key for an object in this object store
     * @return String representation of the key
     * @throws ObjectStoreException When string representation could not be created, as if the
     * key is not from the store or is for a deleted object. 
     */
    public String keyToString(ObjectKey key)
    	throws ObjectStoreException;
    /**
     * Return a object key for an object in the store for a string that is the same as a string that would be
     * produced by a call to keyToString for that object.  If the supplied string can not be mapped to key
     * for a valid object in the store, an exception is thrown.
     * @param str
     * @return Key for the object
     * @throws ObjectStoreException
     */
    public ObjectKey stringToKey(String str)
    	throws ObjectStoreException;
}
