package com.antlersoft.query;

import java.util.Enumeration;
import java.util.List;

public class GroupOfTransformExpression extends ResolveCompoundBinding
implements ValueExpression, CountPreservingValueContext
{
	public GroupOfTransformExpression( ValueExpression expr, Transform transform)
	throws BindException
	{
		super( transform, expr);
		m_transform=transform;
		m_expr=expr;
	}

	public List getValueCollection()
	{
		return NO_SUBS;
	}

	public Object getValue()
	{
		return m_expr.getValue();
	}

	public ValueContext getContext()
	{
		return this;
	}

	public void inputObject( ValueObject vobj, DataSource source, Object next)
	{
		m_transform.startEvaluation( source);
		GroupValueContext context=(GroupValueContext)m_expr.getContext();
		Enumeration e=m_transform.transformObject( source, next);
		boolean need_more=true;
		while ( need_more && e.hasMoreElements())
		{
			need_more=context.addObject(
						 m_expr, source, e.nextElement());
		}
		e=m_transform.finishEvaluation( source);
		while ( need_more && e.hasMoreElements())
		{
			need_more=context.addObject(
						 m_expr, source, e.nextElement());
		}
		context.finishGroup( m_expr, source);
	}

	public int getContextType()
	{
		return COUNT_PRESERVING;
	}

	private ValueExpression m_expr;
	private Transform m_transform;
}

