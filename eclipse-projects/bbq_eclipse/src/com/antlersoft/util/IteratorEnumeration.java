
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.util;

import java.util.Enumeration;
import java.util.Iterator;


/**
 * Create an Enumeration from an Iterator
 */
public class IteratorEnumeration implements Enumeration
{
    public IteratorEnumeration( Iterator i)
    {
        base=i;
    }

    public boolean hasMoreElements()
    {
        return base.hasNext();
    }

    public Object nextElement()
    {
        return base.next();
    }

    private Iterator base;
}