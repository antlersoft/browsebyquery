package analyzer.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import analyzer.AnalyzerDB;

import parser.RuleActionException;

class TransformTransform extends TransformImpl
{
	Transform overlay;
	Transform base;

	TransformTransform( Transform o, Transform b)
		throws RuleActionException
	{
		super( b.appliesTo(), o.resultClass());
		overlay=o;
		base=b;
		if ( base.resultClass()!=overlay.appliesTo())
		{
			if ( overlay.appliesTo()==null)
			{
				overlay.lateBindApplies( base.resultClass());
				_result=overlay.resultClass();
			}
			else if ( base.resultClass()==null)
			{
				base.lateBindResult( overlay.appliesTo());
				_applies=base.appliesTo();
			}
			if ( ! overlay.appliesTo().isAssignableFrom( base.resultClass()))
			{
				throw new RuleActionException( "Can not convert "+
					base.resultClass().getName()+" to "+
					overlay.appliesTo().getName());
			}
		}
	}

	public void startEvaluation( AnalyzerDB db)
	{
		base.startEvaluation( db);
		overlay.startEvaluation( db);
	}

	public Enumeration transform( Object toTransform)
		throws Exception
	{
		return new OverlayEnumeration( base.transform( toTransform));
	}

	public Enumeration finishEvaluation()
		throws Exception
	{
		return new QueryParser.ComboEnumeration( new OverlayEnumeration(
			base.finishEvaluation()), new Enumeration() {
				Enumeration finishEnum=null;
				public boolean hasMoreElements()
				{
					try
					{
						if ( finishEnum==null)
							finishEnum=overlay.finishEvaluation();
						return finishEnum.hasMoreElements();
					}
					catch ( Exception e)
					{
						throw new NoSuchElementException( e.getMessage());
					}
				}
				public Object nextElement()
				{
					try
					{
						if ( finishEnum==null)
							finishEnum=overlay.finishEvaluation();
						return finishEnum.nextElement();
					}
					catch ( Exception e)
					{
						throw new NoSuchElementException( e.getMessage());
					}
				}
		});
	}

	public void lateBindResult( Class newResultClass)
		throws RuleActionException
	{
		overlay.lateBindResult( newResultClass);
		if ( overlay.appliesTo()!=null)
		{
			if ( base.resultClass()==null)
			{
				base.lateBindResult( overlay.appliesTo());
				_applies=base.appliesTo();
			}
			if ( ! overlay.appliesTo().isAssignableFrom( base.resultClass()))
			{
				throw new RuleActionException( "Can not convert "+
					base.resultClass().getName()+" to "+
					overlay.appliesTo().getName());
			}
		}
		_result=overlay.resultClass();
	}

	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException
	{
		base.lateBindApplies( newAppliesClass);
		if ( base.resultClass()!=null)
		{
			if ( overlay.appliesTo()==null)
			{
				overlay.lateBindApplies( base.resultClass());
				_result=overlay.resultClass();
			}
			if ( ! overlay.appliesTo().isAssignableFrom( base.resultClass()))
			{
				throw new RuleActionException( "Can not convert "+
					base.resultClass().getName()+" to "+
					overlay.appliesTo().getName());
			}
		}
		_result=overlay.resultClass();
	}

	class OverlayEnumeration extends QueryParser.BiEnumeration
	{
		OverlayEnumeration( Enumeration e)
		{
			super( e);
		}

		public Enumeration getNextCurrent( Object fromBase)
		{
			try
			{
				return overlay.transform( fromBase);
			}
			catch ( Exception e)
			{
				throw new NoSuchElementException( e.getMessage());
			}
		}
	}

}