package odb;

import java.io.Serializable;

public class ObjectRef implements Serializable
{
    ObjectKey objectKey;

	// TODO: Implement (soft?) reference to persistent object to cache it,
	// so we don't have to go back to ObjectDB all the time.  If the
	// reference is already set, provided that the obsolete flag
	// that has to be added to PersistentImpl (so it can be set at
	// object commit time) is not true, you can return it insted
	// of doing the hash lookup all the time.

    public ObjectRef()
    {
        objectKey=null;
    }

	public ObjectRef( Object toReference)
	{
		this();
		setReferenced( toReference);
	}

    public Object getReferenced()
    {
		if ( objectKey==null)
			return null;
		return ObjectDB.getObjectDB().getObjectByKey( objectKey);
    }

    public void setReferenced( Object newValue)
    {
        if ( newValue!=null)
			objectKey=((Persistent)newValue)._getPersistentImpl().objectKey;
		else
			objectKey=null;
    }

	public int hashCode()
	{
		Object referenced=getReferenced();
		if ( referenced==null)
		    return 0;
		return referenced.hashCode();
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof ObjectRef)
		{
			toCompare=((ObjectRef)toCompare).getReferenced();
		}
		Object ours=getReferenced();
		if ( ours==null)
		{
			return toCompare==null;
		}
		return ours.equals( toCompare);
	}
}
