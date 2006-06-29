/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;

/**
 * @author mike
 *
 */
class DeprecatedFilter extends FilterOnAccessFlagsTypes {

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.Filter#include(java.lang.Object)
	 */
	protected boolean include(Object toCheck) throws Exception {
		return ((AccessFlags)toCheck).isDeprecated();
	}
}
