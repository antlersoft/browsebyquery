
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

class Existing extends Filter
{
    Existing( Transform _transform)
    {
        super( null);
        transform=_transform;
    }

    protected boolean include(Object parm1) throws java.lang.Exception
    {
        transform.startEvaluation( db);
        Enumeration transformEnumeration=transform.transform( parm1);
        Enumeration finishEnumeration=transform.finishEvaluation();
        return transformEnumeration.hasMoreElements() ||
            finishEnumeration.hasMoreElements();
    }

	public void startEvaluation( AnalyzerDB _db)
	{
        db=_db;
	}

	public void lateBind( Class _newFilterClass)
		throws RuleActionException
	{
        Class appliesTo=transform.appliesTo();
        if ( appliesTo==null)
        {
            transform.lateBindApplies( _newFilterClass);
        }
        else
        {
            if ( ! appliesTo.isAssignableFrom( _newFilterClass))
            {
                throw new RuleActionException(
                    "Existing type mismatch: expecting "+appliesTo.getName()+
                    " but has "+_newFilterClass.getName());
            }
        }
        _filterClass=transform.appliesTo();
	}

    private Transform transform;
    private AnalyzerDB db;
}