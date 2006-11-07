/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.DBArgument;
import com.antlersoft.analyzer.DBMethod;

import com.antlersoft.query.*;

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

	public Enumeration transformObject( DataSource source, Object toTransform) {
		return ((DBMethod)toTransform).getArguments();
	}

}
