package analyzer.query;

import analyzer.AnalyzerDB;

import parser.RuleActionException;

public abstract class Filter
{
    protected Class _filterClass;
	private boolean _not;
	Filter( Class filterClass)
	{
		_filterClass=filterClass;
		_not=false;
	}
	public void setNot()
	{
		_not= ! _not;
	}
	public Class getFilterClass()
	{
		return _filterClass;
	}
	public void startEvaluation( AnalyzerDB db)
	{
	}
    protected abstract boolean include( Object toCheck)
		throws Exception;
    public boolean isIncluded( Object toCheck)
		throws Exception
	{
		return _not ? ( ! include( toCheck)) : include( toCheck);
    }

	public void lateBind( Class _newFilterClass)
		throws RuleActionException
	{
		throw new RuleActionException(
			getClass().getName()+" can not be late bound");
	}
}
