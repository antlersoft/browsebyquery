package com.antlersoft.query;

import java.util.List;

public abstract class GroupExpression implements ValueExpression, GroupValueContext {
	public GroupExpression( Class result, Class applies)
	{
		m_binding=new BindImpl( result, applies);
	}

	public GroupExpression()
	{
		this( null, null);
	}

	public Class resultClass()
	{
		return m_binding.resultClass();
	}

	public Class appliesClass()
	{
		return m_binding.appliesClass();
	}

	public void lateBindResult( Class new_result)
	throws BindException
	{
		m_binding.lateBindResult( new_result);
	}

	public void lateBindApplies( Class new_applies)
	throws BindException
	{
		m_binding.lateBindApplies( new_applies);
	}

	public ValueContext getContext() { return this; }

	public int getContextType() { return GROUP; }

	public List getValueCollection() { return NO_SUBS; }

	private BindImpl m_binding;
}