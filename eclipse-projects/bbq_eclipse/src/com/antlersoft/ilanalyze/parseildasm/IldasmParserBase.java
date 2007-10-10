/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import com.antlersoft.ilanalyze.DBDriver;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.Symbol;

/**
 * @author Michael A. MacDonald
 *
 */
class IldasmParserBase extends Parser {
	
	DBDriver m_driver;
	
	IldasmParserBase( ParseState[] states)
	{
		super( states);
	}
	
	void setDriver( DBDriver driver)
	{
		m_driver=driver;
	}
}

