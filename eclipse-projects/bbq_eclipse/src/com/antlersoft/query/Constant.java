package com.antlersoft.query;

import java.util.List;

public class Constant extends BindImpl implements ValueExpression {
	public Constant( Object o)
	throws BindException
	{
		super( o.getClass(), Object.class);
		m_value=o;
	}

	public Object getValue()
	{
		return m_value;
	}

	public List getValueCollection()
	{
		return NO_SUBS;
	}

	public ValueContext getContext()
	{
		return m_context;
	}

	private Object m_value;
	private static ScalarValueContextImpl m_context=new ScalarValueContextImpl();
}