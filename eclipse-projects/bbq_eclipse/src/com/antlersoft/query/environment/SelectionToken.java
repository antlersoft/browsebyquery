/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

/**
 * @author Michael A. MacDonald
 *
 */
public class SelectionToken extends Token {
	static final Symbol SELECTION_SYMBOL = Symbol.get("selection");
	
	String m_className;
	
	SelectionToken(String className)
	{
		super(SELECTION_SYMBOL, SELECTION_SYMBOL.toString());
		m_className = className;
	}
}
