package com.antlersoft.query;

import java.util.List;

public abstract class CountPreservingValueExpression extends BindImpl implements
	ValueExpression, CountPreservingValueContext
{
	public CountPreservingValueExpression( Class a, Class b)
	throws BindException
	{
		super( a, b);
	}

	public void inputObject( ValueObject obj, DataSource source, Object to_transform)
	{
		m_result=transformSingleObject( source, to_transform);
	}

	public ValueContext getContext()
	{
		return this;
	}

	public int getContextType()
	{
		return COUNT_PRESERVING;
	}

	public Object getValue()
	{
		return m_result;
	}

	public List getValueCollection()
	{
		return NO_SUBS;
	}

	protected abstract Object transformSingleObject( DataSource source,
											Object to_transform);
	private Object m_result;
}