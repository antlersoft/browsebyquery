
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