/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.List;

/**
 * @author Michael A. MacDonald
 *
 */
public abstract class CountPreservingFilter extends Filter implements CountPreservingValueContext {

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Filter#booleanValue()
	 */
	public boolean booleanValue() {
		return m_result;
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
	public final List getValueCollection() {
		return NO_SUBS;
	}
	
	public final int getContextType()
	{
		return COUNT_PRESERVING;
	}
	
	public final void inputObject( ValueObject obj, DataSource source, Object input)
	{
		m_result=getCountPreservingFilterValue( source, input);
	}
	
	protected abstract boolean getCountPreservingFilterValue( DataSource source, Object inputObject);

	protected boolean m_result;

}
