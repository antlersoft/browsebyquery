
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb;

public class ObjectDBException extends RuntimeException
{
    public ObjectDBException()
    {
	super();
	underlying=null;
    }

    public ObjectDBException( String msg)
    {
	super( msg);
	underlying=null;
    }

    public ObjectDBException( String msg, Exception cause)
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