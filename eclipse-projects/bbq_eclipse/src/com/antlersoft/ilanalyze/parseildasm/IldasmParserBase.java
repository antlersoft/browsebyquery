/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.Symbol;

/**
 * @author Michael A. MacDonald
 *
 */
class IldasmParserBase extends Parser {
	static Symbol QSTRING = Symbol.get( "QSTRING");
	static Symbol SQSTRING = Symbol.get( "SQSTRING");
	static Symbol HexByte = Symbol.get("HexByte");
	static Symbol ID = Symbol.get("ID");
	static Symbol RealNumber = Symbol.get("RealNumber");
	static Symbol Int32 = Symbol.get("Int32");
	static Symbol Int64 = Symbol.get("Int64");
	
	IldasmParserBase( ParseState[] states)
	{
		super( states);
	}
}

