
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.postproc;

import com.antlersoft.odb.ObjectDBException;

public class PostProcessingException extends ObjectDBException
{
    public PostProcessingException()
    {
	    super();
    }

    public PostProcessingException( String msg)
    {
	    super( msg);
    }

    public PostProcessingException( String msg, Exception cause)
    {
    	super( msg, cause);
    }
}