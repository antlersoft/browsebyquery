
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.diralloc;

import com.antlersoft.odb.ObjectStoreException;

public class NoSuchClassException extends ObjectStoreException
{
    public NoSuchClassException()
    {
        super();
    }

    public NoSuchClassException( Class notFound)
    {
        super( "Class "+notFound.getName()+" not found");
    }
}