
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

import com.antlersoft.parser.RuleActionException;

class Count extends TransformImpl
{
    public Count()
    {
        super( null, Integer.class);
        count=0;
    }

    public Enumeration transform(Object parm1) throws java.lang.Exception
    {
        count++;
        return EmptyEnumeration.emptyEnumeration;
    }

    public void startEvaluation( AnalyzerDB db) { count=0; }

    public Enumeration finishEvaluation()
		throws Exception
	{
		return QueryParser.enumFromObject( new Integer( count));
	}

	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException
	{
        _applies=newAppliesClass;
	}

    private int count;
}