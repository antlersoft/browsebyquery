
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

import java.io.Serializable;

import com.antlersoft.odb.KeyGenerator;

class IndexEntry implements Serializable
{
    String indexName;
    KeyGenerator generator;
    int pageCount;
    int startPageOffset;
    boolean descending;
    boolean unique;

    transient Index index;
}