package odb;

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
}
