
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

class StringGet extends SetExpression
{
    private String toGet;

    StringGet( String _toGet)
    {
        super( DBStringConstant.class);
        toGet=_toGet;
    }

    public Enumeration execute(AnalyzerDB parm1) throws java.lang.Exception
    {
        Enumeration result;
        Object found=parm1.findWithKey( DBStringConstant.class.getName(),
            toGet);
        if ( found!=null)
            return QueryParser.enumFromObject( found);
        else
            return EmptyEnumeration.emptyEnumeration;
    }
}