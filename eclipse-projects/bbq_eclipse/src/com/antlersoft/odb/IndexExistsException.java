
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

public class IndexExistsException extends ObjectStoreException
{
    public IndexExistsException( String indexName)
    {
        super( "Index "+indexName+" already exists.");
    }
}