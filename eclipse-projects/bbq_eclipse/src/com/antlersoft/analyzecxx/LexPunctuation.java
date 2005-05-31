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

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import java.io.IOException;

public class LexPunctuation implements LexState {
	private LexReader m_reader;
	private LexState m_caller;
	private static SymbolFinderTree m_tree=new SymbolFinderTree(
		   new String[] {
		   "{", "}", "[", "]", "#", "##", "(", ")", ";", ":", "...",
		   "?", "::", ".", ".*",
		   "+", "-", "/", "*", "%", "^", "&", "|", "~",
		   "!", "=", "<", ">", "+=", "-=", "*=", "/=", "%=",
		   "^=", "&=", "|=", "<<", ">>", ">>=", "<<=", "==", "!=",
		   "<=", ">=", "&&", "||", "++", "--", ",", "->*", "->"
		   });
	SymbolFinder m_finder;

	LexPunctuation( LexReader reader, LexState caller, char initial)
		throws RuleActionException, LexException
	{
		m_reader=reader;
		m_caller=caller;
		m_finder=new SymbolFinder( m_tree);
		addCharacter( initial);
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		if ( ! CharClass.isOperator( c))
		{
			cleanRemainder();
			return m_caller.nextCharacter( c);
		}
		addCharacter( c);
		return this;
    }

    public LexState endOfFile() throws IOException, RuleActionException, LexException {
		cleanRemainder();
		return m_caller.endOfFile();
    }

	private void addCharacter( char c)
	throws RuleActionException, LexException
	{
		if ( ! m_finder.accept( c))
		{
			rescan();
		}
		else
		{
			if ( ! m_finder.canGrow())
			{
				Symbol s=m_finder.currentSymbol();
				m_reader.processToken( new PunctuationToken(
						m_finder.currentText(), s));
				m_finder.reset();
			}
		}
	}

	private void rescan()
	throws RuleActionException, LexException
	{
		Symbol s=m_finder.currentSymbol();
		if ( s!=null)
		{
			m_reader.processToken( new PunctuationToken(
					m_finder.currentText(), s));
		}
		String r=null;
		if ( m_finder.isRemainder())
		{
			r=m_finder.getRemainder();
		}
		m_finder.reset();
		if ( r!=null)
		{
			int remainder_length=r.length();
			int start_index=0;
			if ( s==null)
			{
				start_index=1;
				m_reader.processToken( new PunctuationToken( r.substring( 0,1),
					null));
			}
			for ( ; start_index<remainder_length; ++start_index)
				addCharacter( r.charAt( start_index));
		}
	}

	private void cleanRemainder()
	throws RuleActionException, LexException
	{
		rescan();
		if ( m_finder.isRemainder())
		{
			String r=m_finder.getRemainder();
			m_finder.reset();
			int remainder_length=r.length();
			for ( int i=0; i<remainder_length; ++i)
			{
				m_reader.processToken(
						new PunctuationToken( r.substring( i, i+1), null));
			}
		}
	}
}