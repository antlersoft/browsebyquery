package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import com.antlersoft.analyzer.AnalyzerDB;

import com.antlersoft.parser.RuleActionException;

class TransformSet extends SetExpression
{
	private SetExpression _baseSet;
	private Transform _transform;
	TransformSet( SetExpression baseSet, Transform toApply)
		throws RuleActionException
	{
		super( bindTransform( baseSet, toApply));
		_baseSet=baseSet;
		_transform=toApply;
	}

	public Enumeration execute( AnalyzerDB db)
		throws Exception
	{
		_transform.startEvaluation( db);
		return new TransformEnumeration( _baseSet.execute( db));
	}

	private static Class bindTransform( SetExpression baseSet,
		Transform transform)
		throws RuleActionException
	{
		if ( transform.appliesTo()==null)
		{
			transform.lateBindApplies( baseSet.getSetClass());
		}
		if ( ! transform.appliesTo().isAssignableFrom( baseSet.getSetClass()))
		{
			throw new RuleActionException( "Cannot convert "+
				baseSet.getSetClass().getName()+" to "+
				transform.appliesTo().getName());
		}
		return transform.resultClass();
	}

	public static Class commonSubType( Class a, Class b, boolean returnSuper)
		throws RuleActionException
	{
		Class superClass;
		Class subClass;

		if ( a==null || b==null)
			throw new RuleActionException( "Incomplete bind");
		if ( a==b)
			return a;
		if ( a.isAssignableFrom( b))
		{
			superClass=a;
			subClass=b;
		}
		else if ( b.isAssignableFrom( a))
		{
			superClass=b;
			subClass=a;
		}
		else
			throw new RuleActionException(
				"Incompatible object types: expecting"+
					b.getName()+" but getting "+a.getName());

		return returnSuper ? superClass : subClass;
	}

    private class TransformEnumeration implements Enumeration
    {
        private Enumeration _baseEnumeration;
        private Enumeration _currentEnumeration;
		private boolean _finished;

        TransformEnumeration( Enumeration base)
        {
            _baseEnumeration=base;
            _currentEnumeration=null;
			_finished=false;
        }

        private void setCurrentEnumeration()
        {
			try
			{
				while (  _currentEnumeration==null
					|| ! _currentEnumeration.hasMoreElements())
				{
					if ( _baseEnumeration.hasMoreElements())
						_currentEnumeration=_transform.transform( _baseEnumeration.nextElement());
					else if ( ! _finished)
					{
						_finished=true;
						_currentEnumeration=_transform.finishEvaluation();
					}
					else
					{
						_currentEnumeration=null;
						break;
					}
				}
			}
			catch ( Exception e)
			{
	java.io.StringWriter sw=new java.io.StringWriter( 1000);
	java.io.PrintWriter pw=new java.io.PrintWriter( sw);
	e.printStackTrace( pw);

				throw new NoSuchElementException( sw.toString()+" "+e.getMessage());
			}
        }

        public boolean hasMoreElements()
        {
            setCurrentEnumeration();
            return _currentEnumeration!=null;
        }

        public Object nextElement()
        {
            setCurrentEnumeration();
            if ( _currentEnumeration==null)
            {
                throw new NoSuchElementException();
            }
            return _currentEnumeration.nextElement();
        }
    }
}
