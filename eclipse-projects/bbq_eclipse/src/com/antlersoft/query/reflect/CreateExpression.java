package com.antlersoft.query.reflect;

import java.util.List;

import com.antlersoft.query.*;

public class CreateExpression implements ValueExpression, CountPreservingValueContext {
	
	private Class m_result_class;
	
	public CreateExpression( Transform transform)
	{
		m_invoker=new Invoker( null, transform);
		m_result_class=null;
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
	
	public int getContextType()
	{
		return COUNT_PRESERVING;
	}
	
	public void inputObject( ValueObject obj, DataSource source, Object target)
	{
		m_result=m_invoker.invokeConstructor( source, target);
	}
	
	public Class resultClass()
	{
		return m_result_class;
	}
	
	public Class appliesClass()
	{
		return m_invoker.m_transform.appliesClass();
	}
	
	public void lateBindResult( Class new_result)
	{
		m_result_class=new_result;
	}

	public void lateBindApplies( Class new_applies)
		throws BindException
	{
		m_invoker.m_transform.lateBindApplies( new_applies);
	}
	
	private Invoker m_invoker;
	private Object m_result;
}