package com.antlersoft.odb;

/**
 * Publicly opaque class that is included in persistent objects to provide
 * support for persistence.
 */
public class PersistentImpl
{
	public PersistentImpl()
	{
		dirty=false;
		objectKey=null;
        obsolete=false;
        deleted=false;
        cachedReference=null;
	}

    public PersistentImpl( Persistent containing)
    {
        super();
        cachedReference=containing;
    }

	boolean dirty;
	ObjectKey objectKey;
    boolean obsolete;
    boolean deleted;
    Persistent cachedReference;

    Object getCanonical( ObjectRef toUpdate)
    {
        if ( obsolete || cachedReference==null)
        {
            if ( objectKey==null)
                throw new ObjectDBException( "Internal: obsolete with no object key");
            cachedReference=ObjectDB.getObjectDB().
                getObjectByKey( objectKey);
            PersistentImpl newImpl=cachedReference._getPersistentImpl();
            synchronized( toUpdate)
            {
                if ( toUpdate.impl==this)
                    toUpdate.impl=newImpl;
            }
        }
        return cachedReference;
    }
}