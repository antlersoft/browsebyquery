package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

/**
 * This class represents the initial state of processing to
 * create preprocessor tokens, described as stage 3 in the spec
 */
class PreprocessorTokens implements LexState
{
	private CxxReader m_reader;

	PreprocessorTokens( CxxReader reader)
	{
		m_reader=reader;
	}

	public LexState nextCharacter( char c)
		throws RuleActionException, IOException, LexException
	{
		LexState result=this;
		switch ( c)
		{
			case '/' :
			    result=new InitialSlash( m_reader, this);
				break;
			case '.' :
			    result=new InitialPeriod( m_reader, this);
				break;
			case 'L' :
			    result=new InitialL( m_reader, this);
				break;
			case '\'' :
				result=new QuotedLiteral( m_reader, this, c,
						  new LexToken( PreprocessParser.lex_character_literal, null, null), false);
				break;
			case '"' :
			    result=new QuotedLiteral( m_reader, this, c,
						  new LexToken( isExpectingIncludeHeader()
						  ? PreprocessParser.lex_include_header
						  : PreprocessParser.lex_string_literal, null, null), false);
				break;
			case '<' :
							if ( isExpectingIncludeHeader())
							{
								result=new QuotedLiteral( m_reader, this, '>',
								new LexToken( PreprocessParser.lex_include_header, null, null),
								false);
							}
							else
							{
								result=new LexPunctuation( m_reader, this, c);
							}
							break;
			case '\\' :
				throw new LexException( "Unsupported: naked backslash or universal character name");
			default :
				if ( CharClass.isWhiteSpace( c))
					result=new  WhiteSpace( m_reader.m_lex_to_preprocess, this, c);
				else if ( CharClass.isDigit( c))
					result=new LexNumber( m_reader, this, c);
				else if ( CharClass.isIdentifierStart(c))
					result=new LexIdentifier( m_reader, this, c);
				else
					result=new LexPunctuation( m_reader, this, c);
		}
		return result;
	}

	public LexState endOfFile()
	{
		return this;
	}

	private boolean isExpectingIncludeHeader()
	{
		return false;
	}
}
