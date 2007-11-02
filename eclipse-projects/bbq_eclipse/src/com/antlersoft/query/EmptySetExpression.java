/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.Enumeration;


/**
 * @author mike
 *
 */
public class EmptySetExpression extends SetExpression {

	public Class getResultClass()
	{
		return getClass();
	}
	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.SetExpression#execute(com.antlersoft.analyzer.AnalyzerDB)
	 */
	public Enumeration evaluate(DataSource db) {
		return EmptyEnum.empty;
	}

}
