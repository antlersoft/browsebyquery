package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ParseState;

class ConstExprParserBase extends Parser {
	ConstExprParserBase( ParseState[] states)
	{
		super( states);
	}
}