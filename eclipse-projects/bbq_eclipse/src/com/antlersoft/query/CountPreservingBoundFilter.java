/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query;

/**
 * A count preserving filter that manages its own binding
 * @author Michael A. MacDonald
 *
 */
public abstract class CountPreservingBoundFilter extends CountPreservingFilter {
	
	protected BindImpl m_bind;

	/**
	 * @param applies Class to bind as the applies class (may be null, to allow flexible binding)
	 */
	public CountPreservingBoundFilter( Class applies) {
		m_bind=new BindImpl( BOOLEAN_CLASS, applies);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	public Class appliesClass() {
		return m_bind.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
	public void lateBindApplies(Class new_applies) throws BindException {
		m_bind.lateBindApplies(new_applies);
	}

}
