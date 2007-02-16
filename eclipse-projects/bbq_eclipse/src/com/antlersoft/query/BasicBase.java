/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.query;

import com.antlersoft.parser.*;

public class BasicBase extends Parser {
	static public final Symbol literalString=Symbol.get( "literalString");
	static public final Symbol number=Symbol.get( "number");
    static public final Symbol nameSymbol=Symbol.get( "_nameSymbol");

	private ParserEnvironment m_environment;
	
	public BasicBase( ParseState[] states)
	{
		super(states);
	}
	
	public BasicBase( ParseState[] states, ParserEnvironment environment)
	{
		super( states);
		m_environment=environment;
	}
	
	public Object getParserEnvironment()
	{
		return m_environment;
	}
	
	public void setParserEnvironment( ParserEnvironment env)
	{
		m_environment=env;
	}
	
	/**
	 * Anything a sub-class might want to do with a token
	 */
	public void massageToken( Token token)
	{
	
	}
}