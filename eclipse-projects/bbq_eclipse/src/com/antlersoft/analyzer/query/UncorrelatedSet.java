
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

/**
 * A transform that returns a whole set for each object transformed, ignoring
 * the object transformed.  With this you can include a SetExpression in
 * correlated expressions.  For efficiency, you might want this to be an
 * execute SetExpression.
 * @see #ExecuteExpression
 */
class UncorrelatedSet extends TransformImpl
{
    UncorrelatedSet( SetExpression baseSet)
    {
        super( null, baseSet.getSetClass());
        setExpression=baseSet;
    }

    public Enumeration transform(Object toTransform) throws Exception
    {
        return setExpression.execute( db);
    }

    public void startEvaluation(AnalyzerDB _db)
    {
        db=_db;
    }

    public void lateBindApplies(Class newAppliesClass) throws RuleActionException
    {
        _applies=newAppliesClass;
    }

    private SetExpression setExpression;
    private AnalyzerDB db;
}