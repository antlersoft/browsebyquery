package com.antlersoft.odb;

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

    public void printStackTrace( java.io.PrintWriter pw)
    {
        super.printStackTrace( pw);
        if ( underlying!=null)
            underlying.printStackTrace( pw);
    }

    public void printStackTrace( java.io.PrintStream ps)
    {
        printStackTrace( new java.io.PrintWriter( ps));
    }

    public Exception getUnderlying()
    {
	return underlying;
    }

    private Exception underlying;
}
