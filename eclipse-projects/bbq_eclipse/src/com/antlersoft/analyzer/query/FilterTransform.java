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
