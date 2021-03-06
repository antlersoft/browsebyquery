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
package com.antlersoft.analyzecxx;

import java.io.IOException;

import java.util.Enumeration;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * This class represents the initial state of processing to
 * create preprocessor tokens, described as stage 3 in the spec
 */
class PreprocessorTokens implements LexState
{
	private LexReader m_reader;
	private PreprocessParserBase m_parser;

	PreprocessorTokens( LexReader reader, PreprocessParserBase parser)
	{
		m_reader=reader;
		m_parser=parser;
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
						  new CharacterLiteral(  null, false));
				break;
			case '"' :
			    result=new QuotedLiteral( m_reader, this, c,
						  isExpectingIncludeHeader()
						  ? new LexToken( PreprocessParser.lex_include_header_with_current, null)
						  : new StringLiteral( null, false));
				break;
			case '<' :
							if ( isExpectingIncludeHeader())
							{
								result=new QuotedLiteral( m_reader, this, '>',
								new LexToken( PreprocessParser.lex_include_header, null));
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
					result=new  WhiteSpace( m_reader, this, c);
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
		for ( Enumeration e=m_parser.getExpectedSymbols();
			  e.hasMoreElements();)
		{
			if ( ((Symbol)e.nextElement())==PreprocessParser.lex_include_header)
				return true;
		}
		return false;
	}
}
