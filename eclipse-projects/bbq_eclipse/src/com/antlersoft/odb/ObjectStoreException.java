package com.antlersoft.odb;

public class ObjectStoreException extends ObjectDBException
{
    public ObjectStoreException()
    {
	super();
    }

    public ObjectStoreException( String msg)
    {
	super( msg);
    }

    public ObjectStoreException( String msg, Exception cause)
    {
	super( msg, cause);
    }
}
