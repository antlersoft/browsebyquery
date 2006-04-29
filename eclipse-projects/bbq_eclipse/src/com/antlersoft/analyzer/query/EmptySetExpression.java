/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.AnalyzerDB;

/**
 * @author mike
 *
 */
public class EmptySetExpression extends SetExpression {

	/**
	 * @param setClass
	 */
	public EmptySetExpression() {
		super(SetExpression.class);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.SetExpression#execute(com.antlersoft.analyzer.AnalyzerDB)
	 */
	public Enumeration execute(AnalyzerDB db) throws Exception {
		return EmptyEnumeration.emptyEnumeration;
	}

}
