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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Works in concert with ObjectRef and PersistentImpl objects to provide caching,
 * object tracking, and transaction semantics, and all client-visible
 * persistence functionality.   Written to make use of an object that
 * implements the ObjectStore interface.
 */
public class ObjectDB
{
    // Support only one instance per process for now
    private static ObjectDB current=null;
    private ObjectCache cachedObjects;
    private ObjectStore store;
	private ObjectRef rootObjects;
    private ArrayList dirtyObjects;

    public ObjectDB( ObjectStore objectStore)
		throws ObjectStoreException
    {
		store=objectStore;
		cachedObjects=new ObjectCache();
        dirtyObjects=new ArrayList();
		current=this;
		ObjectKey rootKey=(ObjectKey)store.getRootObject();
		if ( rootKey==null)
		{
			rootObjects=new ObjectRef( new PersistentHashtable());
			rootKey=getObjectKey( rootObjects.getReferenced());
			store.updateRootObject( rootKey);
			commit();
		}
		else
		{
			rootObjects=new ObjectRef( (PersistentHashtable)getObjectByKey( rootKey));
		}
    }

    public static ObjectDB getObjectDB()
    {
		return current;
    }

	public static void makeDirty( Persistent toDirty)
	{
        PersistentImpl impl=toDirty._getPersistentImpl();
        if ( ! impl.isDirty())
        {
            ObjectDB db=getObjectDB();
            synchronized(db)
            {
                impl.makeDirty( toDirty);
                db.dirtyObjects.add(toDirty);
            }
        }
	}

    public void makeCurrent()
    {
        current=this;
    }

	public synchronized void setPersistent( Object toStore)
		throws ObjectStoreException
	{
		PersistentImpl impl=((Persistent)toStore)._getPersistentImpl();
		if ( impl.objectKey==null)
		{
			impl.objectKey=store.insert( (Persistent)toStore);
			assert( impl.objectKey!=null);
    		if ( ! impl.isDirty())
            {
                impl.makeDirty( (Persistent)toStore);
                dirtyObjects.add( toStore);
            }
        }
    	cachedObjects.put( impl.objectKey, toStore);
	}

	public static void makePersistent( Object toStore)
	{
		getObjectDB().setPersistent( toStore);
	}

    protected final synchronized Persistent getObjectByKey( ObjectKey key)
		throws ObjectStoreException
    {
		Persistent retVal=(Persistent)cachedObjects.get( key);
		if ( retVal==null)
		{
			retVal=(Persistent)store.retrieve( key);
			PersistentImpl impl=retVal._getPersistentImpl();
            impl.objectKey=key;
            impl.setCached( retVal);
			cachedObjects.put( key, retVal);
		}

		return retVal;
    }

    public Object get( ObjectKey key)
    {
        return getObjectByKey( key);
    }

	public ObjectKey getObjectKey( Object object)
	{
		return ((Persistent)object)._getPersistentImpl().objectKey;
	}

    public void deleteObject( Object object)
    {
        PersistentImpl impl=((Persistent)object)._getPersistentImpl();
        boolean wasDirty=impl.isDirty();
        impl.markDeleted( (Persistent)object);
        if ( ! wasDirty)
        {
            dirtyObjects.add( object);
        }
    }

	public synchronized void makeRootObject( String key, Object object)
	{
		((PersistentHashtable)rootObjects.getReferenced()).put( key, object);
	}

	public synchronized Object getRootObject( String key)
	{
		return ((PersistentHashtable)rootObjects.getReferenced()).get( key);
	}

    public synchronized void uprootObject( String key)
    {
        ((PersistentHashtable)rootObjects.getReferenced()).remove( key);
    }

    synchronized void commitDirty()
    {
        try
        {
    		for ( Iterator i=dirtyObjects.iterator(); i.hasNext();)
    		{
    			Persistent toCommit=(Persistent)i.next();
    			synchronized ( toCommit)
    			{
    				PersistentImpl impl=toCommit._getPersistentImpl();
                    if ( impl.isDeleted())
                        store.delete( impl.objectKey);
    				else
    				{
    	    			store.update( impl.objectKey, toCommit);
    				}
    			}
    		}
        }
        catch ( Exception e)
        {
e.printStackTrace();
            try
            {
                store.rollback();
            }
            catch ( ObjectStoreException ose)
            {
                throw new ObjectStoreException(
                    "Error cleaning up failed commit",
                    new ObjectStoreException( ose.getMessage(), e));
            }
            throw new ObjectDBException( "Commit failed", e);
        }
		store.sync();
	}

    public synchronized void commitAndRetain()
    {
        commitDirty();
        for ( Iterator i=dirtyObjects.iterator(); i.hasNext();)
        {
            PersistentImpl impl=((Persistent)i.next())._getPersistentImpl();
            impl.fromDirty();
        }
        // Remove reference to root objects hash so all unref'd objects
        // are clearable
        PersistentImpl impl=((Persistent)rootObjects.getReferenced()).
            _getPersistentImpl();
        impl.makeObsolete();
        dirtyObjects.clear();
    }

    public synchronized void commit()
    {
        commitDirty();
        for ( Iterator i=cachedObjects.iterator();
            i.hasNext();)
        {
            PersistentImpl impl=((Persistent)i.next())._getPersistentImpl();
            impl.makeObsolete();
        }
		cachedObjects.clear();
        dirtyObjects.clear();
    }

    public synchronized void rollback()
        throws ObjectStoreException
    {
        store.rollback();
        cachedObjects.clear();
        dirtyObjects.clear();
    }

    public synchronized void close()
        throws ObjectStoreException
    {
        commitDirty();
        store.close();
        cachedObjects=null;
        dirtyObjects=null;
        current=null;
        store=null;
        rootObjects=null;
    }
}
