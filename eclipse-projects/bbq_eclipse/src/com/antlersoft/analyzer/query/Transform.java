package analyzer.query;

import java.util.Enumeration;

import analyzer.AnalyzerDB;

import parser.RuleActionException;

interface Transform
{
    abstract Class appliesTo();
    abstract Class resultClass();
    abstract Enumeration transform( Object toTransform)
		throws Exception;
    abstract void startEvaluation( AnalyzerDB db);
    abstract Enumeration finishEvaluation()
		throws Exception;
	public void lateBindResult( Class newResultClass)
		throws RuleActionException;
	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException;
}
