/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.DBArgument;
import com.antlersoft.analyzer.DBMethod;

/**
 * @author mike
 *
 */
class ArgumentsOf extends TransformImpl {

	/**
	 * @param applies
	 * @param result
	 */
	public ArgumentsOf() {
		super(DBArgument.class, DBMethod.class);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.TransformImpl#transform(java.lang.Object)
	 */
	public Enumeration transform(Object toTransform) throws Exception {
		return ((DBMethod)toTransform).getArguments();
	}

}
