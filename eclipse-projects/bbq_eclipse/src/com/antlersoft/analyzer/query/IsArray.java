/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBType;

import com.antlersoft.query.BindException;
import com.antlersoft.query.CountPreservingFilter;
import com.antlersoft.query.DataSource;

/**
 * @author Michael A. MacDonald
 *
 */
final class IsArray extends CountPreservingFilter {

	/* (non-Javadoc)
	 * @see com.antlersoft.query.CountPreservingFilter#getCountPreservingFilterValue(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	protected boolean getCountPreservingFilterValue(DataSource source,
			Object inputObject) {
		return ((DBType)inputObject).isArrayRef();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	public Class appliesClass() {
		return DBType.class;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
	public void lateBindApplies(Class new_applies) throws BindException {
		if ( ! appliesClass().isAssignableFrom( new_applies))
			throw new BindException( "IsArray only applies to types");
	}

}
