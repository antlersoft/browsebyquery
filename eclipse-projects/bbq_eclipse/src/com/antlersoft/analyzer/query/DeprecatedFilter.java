/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;

import com.antlersoft.query.DataSource;

/**
 * @author mike
 *
 */
class DeprecatedFilter extends FilterOnAccessFlagsTypes {

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.Filter#include(java.lang.Object)
	 */
	protected boolean getCountPreservingFilterValue(DataSource source, Object toCheck) {
		return ((AccessFlags)toCheck).isDeprecated();
	}
}
