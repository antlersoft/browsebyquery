package odb;

public class ObjectStoreException extends RuntimeException
{
    public ObjectStoreException()
    {
	super();
	underlying=null;
    }

    public ObjectStoreException( String msg)
    {
	super( msg);
	underlying=null;
    }

    public ObjectStoreException( String msg, Exception cause)
    {
	super( msg+":"+cause.getMessage());
	underlying=cause;
    }

    public Exception getUnderlying()
    {
	return underlying;
    }

    private Exception underlying;
}
