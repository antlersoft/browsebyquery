
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

import com.antlersoft.analyzer.DBStringConstant;
import com.antlersoft.analyzer.DBStringReference;

class StringsOf extends UniqueTransform
{
    public StringsOf()
    {
        super( DBStringReference.class, DBStringConstant.class);
    }

    public Object uniqueTransform(Object parm1)
    {
        return ((DBStringReference)parm1).getTarget();
    }
}