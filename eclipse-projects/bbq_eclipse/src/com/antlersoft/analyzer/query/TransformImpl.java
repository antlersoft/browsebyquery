package analyzer.query;

import java.util.Enumeration;

import analyzer.AnalyzerDB;

import parser.RuleActionException;

abstract class TransformImpl implements Transform
{
    protected Class _applies;
    protected Class _result;

    public TransformImpl( Class applies, Class result)
    {
		_applies=applies;
		_result=result;        
    }
    public Class appliesTo() { return _applies; }
    public Class resultClass() { return _result; }
    public abstract Enumeration transform( Object toTransform)
		throws Exception;
    public void startEvaluation( AnalyzerDB db) {}
    public Enumeration finishEvaluation()
		throws Exception
	{
		return EmptyEnumeration.emptyEnumeration;
	}

	public void lateBindResult( Class newResultClass)
		throws RuleActionException
	{
		throw new RuleActionException(
			getClass().getName()+" can not be late bound for result");
	}

	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException
	{
		throw new RuleActionException(
			getClass().getName()+" can not be late bound for applies");
	}
}
