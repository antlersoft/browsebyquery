/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query;

/**
 * The base class for parser environments for parsers implementing
 * the query language.
 * 
 * @author Michael A. MacDonald
 *
 */
public class ParserEnvironment {

	private SetExpression m_last_expression;

	public SetExpression getLastParsedExpression()
	{
		return m_last_expression;
	}

	public void setLastParsedExpression( SetExpression expr)
	{
		m_last_expression=expr;
	}
}
