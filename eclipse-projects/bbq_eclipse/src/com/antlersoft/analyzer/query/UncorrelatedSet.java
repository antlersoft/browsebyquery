
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
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