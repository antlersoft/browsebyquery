package analyzer.query;

import java.util.HashSet;
import java.util.Collections;
import java.util.Enumeration;

import parser.RuleActionException;

import analyzer.AnalyzerDB;

class SetOpTransform extends TransformImpl
{
	Transform a;
	Transform b;
	boolean canBindApplies;
	boolean canBindResult;
	HashSet a_only;
	HashSet b_only;
	HashSet both;
	QueryParser.ReservedWord op;

	SetOpTransform( QueryParser.ReservedWord setOp, Transform t1, Transform t2)
		throws RuleActionException
	{
		super( null, null);
		op=setOp;
		a=t1;
		b=t2;
		canBindApplies=false;
		canBindResult=false;
		if ( t1.resultClass()==null)
		{
			if ( t2.resultClass()==null)
			{
				canBindResult=true;
			}
			else
			{
				t1.lateBindResult( t2.resultClass());
			}
		}
		else if ( t2.resultClass()==null)
		{
			t2.lateBindResult( t1.resultClass());
		}
		if ( t1.appliesTo()==null)
		{
			if ( t2.appliesTo()==null)
			{
				canBindApplies=true;
			}
			else
			{
				t1.lateBindApplies( t2.appliesTo());
			}
		}
		else if ( t2.appliesTo()==null)
		{
			t2.lateBindApplies( t1.appliesTo());
		}
		if ( ! canBindResult)
			_result=TransformSet.commonSubType( t1.resultClass(), t2.resultClass(),
				true);
		if ( ! canBindApplies)
			_applies=TransformSet.commonSubType( t1.appliesTo(), t2.appliesTo(), false);
	}

	public void startEvaluation( AnalyzerDB db)
	{
		a.startEvaluation( db);
		b.startEvaluation( db);
		a_only=new HashSet();
		b_only=new HashSet();
		both=new HashSet();
	}

	public Enumeration transform( Object o)
		throws Exception
	{
		processEnumeration( a.transform( o), a_only, b_only);
		processEnumeration( b.transform( o), b_only, a_only);
		return EmptyEnumeration.emptyEnumeration;
	}

	public Enumeration finishEvaluation()
		throws Exception
	{
		processEnumeration( a.finishEvaluation(), a_only, b_only);
		processEnumeration( b.finishEvaluation(), b_only, a_only);
		Enumeration result;
		if ( op==QueryParser.union)
		{
			result=new QueryParser.ComboEnumeration( Collections.enumeration( a_only), 
				new QueryParser.ComboEnumeration( Collections.enumeration( both),
				Collections.enumeration( b_only)));
		}
		else if ( op==QueryParser.intersection)
		{
			result=Collections.enumeration( both);
		}
		else if ( op==QueryParser.deintersection)
		{
			result=new QueryParser.ComboEnumeration( Collections.enumeration( a_only),
				Collections.enumeration( b_only));
		}
		// Final case is without
		else result=Collections.enumeration( a_only);

		// Release references to sets so they can be discarded when
		// we are done with the enumerations
		a_only=null;
		b_only=null;
		both=null;

		return result;
	}

	public void lateBindApplies( Class newApplies)
		throws RuleActionException
	{
		if ( canBindApplies)
		{
			a.lateBindApplies( newApplies);
			b.lateBindApplies( newApplies);
			_applies=TransformSet.commonSubType( a.appliesTo(), b.appliesTo(), false);
			canBindApplies=false;
		}
		else
		{
			throw new RuleActionException( "Transform Operator Expression already bound");
		}
	}

	public void lateBindResult( Class newResult)
		throws RuleActionException
	{
		if ( canBindResult)
		{
			a.lateBindResult( newResult);
			b.lateBindResult( newResult);
			_result=TransformSet.commonSubType( a.resultClass(), b.resultClass(), true);
			canBindResult=false;
		}
		else
		{
			throw new RuleActionException( "Transform Operator Expression already bound");
		}
	}

	private void processEnumeration( Enumeration e, HashSet same, HashSet other)
	{
		while (  e.hasMoreElements())
		{
			Object o=e.nextElement();

			if ( ! both.contains( o))
			{
				if ( other.contains( o))
				{
					other.remove( o);
					both.add( o);
				}
				else
					same.add( o);
			}
		}
	}
}