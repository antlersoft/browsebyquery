package com.antlersoft.query;

import com.antlersoft.parser.*;

public class BasicBase extends Parser {
	static protected final Symbol literalString=Symbol.get( "literalString");
	static protected final Symbol number=Symbol.get( "number");

	private SetExpression m_last_expression;

	public BasicBase( ParseState[] states)
	{
		super( states);
	}

	public SetExpression getLastParsedExpression()
	{
		return m_last_expression;
	}

	public void setLastParsedExpression( SetExpression expr)
	{
		m_last_expression=expr;
	}
}