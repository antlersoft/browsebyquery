
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.DBMethod;
import com.antlersoft.analyzer.DBStringReference;

class StringReferenceFrom extends TransformImpl
{
    public StringReferenceFrom()
    {
        super( DBMethod.class, DBStringReference.class);
    }

    public Enumeration transform(Object parm1) throws java.lang.Exception
    {
        return ((DBMethod)parm1).getStringReferences();
    }
}