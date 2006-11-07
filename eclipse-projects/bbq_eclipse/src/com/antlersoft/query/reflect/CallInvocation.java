/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.reflect;

import java.util.List;

import com.antlersoft.query.*;

/**
 * @author Michael A. MacDonald
 *
 */
class CallInvocation implements ValueExpression, CountPreservingValueContext {
	
	CallInvocation( String method_name, Transform argument_transform)
	{
		m_invoker=new Invoker( method_name, argument_transform);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueExpression#getValue()
	 */
	public Object getValue() {
		return m_target;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getContext()
	 */
	public ValueContext getContext() {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getValueCollection()
	 */
	public List getValueCollection() {
		return NO_SUBS;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#resultClass()
	 */
	public Class resultClass() {
		return m_invoker.m_transform.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	public Class appliesClass() {
		return m_invoker.m_transform.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindResult(java.lang.Class)
	 */
	public void lateBindResult(Class new_result) throws BindException {
		m_invoker.m_transform.lateBindApplies( new_result);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
	public void lateBindApplies(Class new_applies) throws BindException {
		m_invoker.m_transform.lateBindApplies( new_applies);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.CountPreservingValueContext#inputObject(com.antlersoft.query.ValueObject, com.antlersoft.query.DataSource, java.lang.Object)
	 */
	public void inputObject(ValueObject value, DataSource source, Object input) {
		m_invoker.invokeItemReturn( source, input);
		m_target=input;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueContext#getContextType()
	 */
	public int getContextType() {
		return COUNT_PRESERVING;
	}
	
	private Object m_target;
	private Invoker m_invoker;
}
