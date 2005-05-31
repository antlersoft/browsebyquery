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
}
