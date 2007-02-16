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
public interface ParserEnvironment {

	public SetExpression getLastParsedExpression();

	public void setLastParsedExpression( SetExpression expr);
}
