package analyzer.query;

import java.util.Enumeration;

import analyzer.AnalyzerDB;

public abstract class SetExpression
{
    private Class _setClass;

	SetExpression( Class setClass)
	{
		_setClass=setClass;
	}

	Class getSetClass()
	{
		return _setClass;
	}

	public abstract Enumeration execute( AnalyzerDB db)
		throws Exception;
}
