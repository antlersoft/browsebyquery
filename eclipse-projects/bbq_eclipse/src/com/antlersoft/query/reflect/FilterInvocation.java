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
public class FilterInvocation extends CountPreservingFilter {

	public FilterInvocation( String method_name, Transform argument_transform) {
		m_invoker=new Invoker( method_name, argument_transform);
	}

	protected boolean getCountPreservingFilterValue( DataSource source, Object input)
	{
		return ((Boolean)m_invoker.invokeItemReturn( source, input)).booleanValue();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	public Class appliesClass() {
		return m_invoker.m_transform.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
	public void lateBindApplies(Class new_applies) throws BindException {
		m_invoker.m_transform.lateBindApplies( new_applies);
	}

	private Invoker m_invoker;
}
