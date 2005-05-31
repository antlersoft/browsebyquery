/*
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
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.AnalyzerDB;

import com.antlersoft.parser.RuleActionException;

class FilterTransform extends TransformImpl
{
	private Filter _filter;

	FilterTransform( Filter baseFilter)
	{
		super( baseFilter.getFilterClass(), baseFilter.getFilterClass());
		_filter=baseFilter;
	}

	public void startEvaluation( AnalyzerDB db)
	{
		_filter.startEvaluation( db);
	}

	public Enumeration transform( Object toTransform)
		throws Exception
	{
		if ( _filter.isIncluded( toTransform))
		{
			return QueryParser.enumFromObject( toTransform);
		}
		return EmptyEnumeration.emptyEnumeration;
	}

	public void lateBindResult( Class newResultClass)
		throws RuleActionException
	{
		_filter.lateBind( newResultClass);
		_result=_filter.getFilterClass();
		_applies=_result;
	}

	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException
	{
		_filter.lateBind( newAppliesClass);
		_result=_filter.getFilterClass();
		_applies=_result;

	}
}
