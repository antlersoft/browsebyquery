package odb;

import java.util.HashMap;
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
    private HashMap cachedObjects;
    private ObjectStore store;
	private ObjectRef rootObjects;

    public ObjectDB( ObjectStore objectStore)
		throws ObjectStoreException
    {
		store=objectStore;
		cachedObjects=new HashMap();
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

	public static void makeDirty( Object toDirty)
	{
		((Persistent)toDirty)._getPersistentImpl().dirty=true;
	}

	public synchronized void makePersistent( Object toStore)
		throws ObjectStoreException
	{
		synchronized ( toStore)
		{
			PersistentImpl impl=((Persistent)toStore)._getPersistentImpl();
			if ( impl.objectKey==null)
			{
				impl.objectKey=store.insert( new Integer(1));
				impl.dirty=true;
			}
			cachedObjects.put( impl.objectKey, toStore);
		}
	}

	public static void setPersistent( Object toStore)
	{
		getObjectDB().makePersistent( toStore);
	}

    public synchronized Object getObjectByKey( ObjectKey key)
		throws ObjectStoreException
    {
		Persistent retVal=(Persistent)cachedObjects.get( key);
		if ( retVal==null)
		{
			retVal=(Persistent)store.retrieve( key);
			retVal._getPersistentImpl().objectKey=key;
			cachedObjects.put( key, retVal);
		}

		return retVal;
    }

	public ObjectKey getObjectKey( Object object)
	{
		return ((Persistent)object)._getPersistentImpl().objectKey;	
	}

	public synchronized void makeRootObject( String key, Object object)
	{
		((PersistentHashtable)rootObjects.getReferenced()).put( key, object);
	}

	public synchronized Object getRootObject( String key)
	{
		return ((PersistentHashtable)rootObjects.getReferenced()).get( key);
	}

    public synchronized void commit()
    {
		for ( Iterator i=cachedObjects.values().iterator(); i.hasNext();)
		{
			Persistent toCommit=(Persistent)i.next();
			synchronized ( toCommit)
			{
				PersistentImpl impl=toCommit._getPersistentImpl();
				if ( impl.dirty)
				{
	    			store.update( impl.objectKey, toCommit);
					impl.dirty=false;
				}
			}
		}
		store.sync();
		cachedObjects.clear();
	}
}
