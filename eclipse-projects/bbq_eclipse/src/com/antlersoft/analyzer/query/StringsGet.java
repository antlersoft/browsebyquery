
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

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.DBStringConstant;

class StringsGet extends SetExpression
{

    public StringsGet()
    {
        super( DBStringConstant.class);
    }

    public Enumeration execute(AnalyzerDB parm1) throws java.lang.Exception
    {
        return parm1.getAll( DBStringConstant.class.getName());
    }
}