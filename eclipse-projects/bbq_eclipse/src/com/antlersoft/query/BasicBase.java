package com.antlersoft.query;

import com.antlersoft.parser.*;

public class BasicBase extends Parser {
	static Symbol literalString=Symbol.get( "literalString");
	static Symbol number=Symbol.get( "number");

	protected SetExpression m_last_expression;

	BasicBase( ParseState[] states)
	{
		super( states);
	}
}