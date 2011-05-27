/**
 * Copyright (c) 2006, 2011 Michael A. MacDonald
 */
package com.antlersoft.query;

/**
 * The interface for parser environments for parsers implementing
 * the query language.  Parsers can reference these methods making very few
 * assumptions about the implementation.
 * 
 * @author Michael A. MacDonald
 *
 */
public interface ParserEnvironment {

	public SetExpression getLastParsedExpression();

	public void setLastParsedExpression( SetExpression expr);
	
	/**
	 * Return a set expression that will return a set of objects most
	 * recently explicitly selected by the user
	 * in the environment.
	 * @return set of objects selected by the user
	 */
	public SetExpression getSelection();
}
