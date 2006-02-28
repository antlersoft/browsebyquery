package com.antlersoft.query.reflect;

import java.util.List;

import com.antlersoft.query.*;

public abstract class CreateExpression implements ValueExpression, CountPreservingValueContext {
	public CreateExpression( Transform transform)
	{
		m_invoker=new Invoker( null, transform);
	}

	public Object getValue()
	{
		return m_result;
	}

	public List getValueCollection()
	{
		return NO_SUBS;
	}

	public ValueContext getContext()
	{
		return this;
	}

	public void lateBindApplies( Class new_applies)
		throws BindException
	{
		m_invoker.m_transform.lateBindApplies( new_applies);
	}
	
	private Invoker m_invoker;
	private Object m_result;
}