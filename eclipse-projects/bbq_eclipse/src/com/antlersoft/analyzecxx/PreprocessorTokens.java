package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Parser;

/**
 * This class represents the initial state of processing to
 * create preprocessor tokens, described as stage 3 in the spec
 */
class PreprocessorTokens implements LexState
{
	private Parser m_parser;

	public LexState nextCharacter( char c)
	{
		LexState result=this;
		switch ( c)
		{
			case '/' :
			    result=new InitialSlash( m_parser, this);
				break;
			case '.' :
			    result=new InitialPeriod( m_parser, this);
				break;
			default :
				if ( WhiteSpace.isWhiteSpace( c))
					result=new  WhiteSpace( m_parser, this, c);
		}
		return this;
	}

	public LexState endOfFile()
	{
		return this;
	}
}
