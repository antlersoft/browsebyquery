package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.RuleActionException;

class ConstExprParserBase extends Parser implements LexReader {
	static final Integer ZERO=new Integer(0);
	private static final Integer ONE=new Integer(1);

	int m_result;

	ConstExprParserBase( ParseState[] states)
	{
		super( states);
		m_result=0;
	}

	public void processToken( LexToken next_token)
	throws com.antlersoft.parser.RuleActionException
    {
	    if ( next_token instanceof CharacterLiteral)
		{
			try
			{
				next_token.value = CharClass.convertEscapes(next_token.value);
			}
			catch ( LexException le)
			{
				throw new RuleActionException( "Bad character literal: "+le.getMessage());
			}
			parse(ConstExprParser.cep_number,
				  new Integer( ( (CharacterLiteral) next_token).intValue()));
		}
		else if ( next_token.symbol==PreprocessParser.lex_identifier)
			parse( ConstExprParser.cep_number, next_token.value.equals( "true") ? ONE : ZERO);
		else if ( next_token instanceof NumericLiteral)
			try {
				parse(ConstExprParser.cep_number,
					  new Integer( ( (NumericLiteral) next_token).intValue()));
			}
			catch (LexException le) {
				throw new RuleActionException("Bad numeric literal: " +
											  le.getMessage());
			}
		else if ( next_token instanceof AltSymbolToken)
		{
			AltSymbolToken token=(AltSymbolToken)next_token;
			parse( token.m_alt_symbol==null ? token.symbol : token.m_alt_symbol,
				   token.value);
		}
	}

    public void noMoreTokens()
	throws com.antlersoft.parser.RuleActionException
    {
	    parse( _end_);
		reset();
	}

	int getResult()
	{
		return m_result;
	}
}