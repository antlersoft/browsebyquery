package com.antlersoft.query;

import java.util.List;

public abstract class OperatorExpression extends ResolvePairBinding implements ValueExpression {
	protected OperatorExpression( ValueExpression a, ValueExpression b)
	throws BindException
	{
		super( a, b);
		m_context=new ResolvePairContext( a, b);
		m_a=a;
		m_b=b;
	}
    public ValueContext getContext() {
		return m_context;
    }
    public List getValueCollection() {
		return m_context.getValueCollection();
    }

	private ResolvePairContext m_context;
	protected ValueExpression m_a;
	protected ValueExpression m_b;
}