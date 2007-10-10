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

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;
import com.antlersoft.parser.lex.LexWithSymbolTree;
import com.antlersoft.parser.lex.SymbolFinder;
import com.antlersoft.parser.lex.SymbolFinderTree;

/**
 * See tokens composed of punctuation and try to recognize them
 * @author Michael A. MacDonald
 *
 */
public class LexPunctuation extends LexWithSymbolTree {
	private LexReader m_reader;
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
		throws RuleActionException, LexException, IOException
	{
		super( m_tree, caller);
		m_reader=reader;
		nextCharacter( initial);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexWithSymbolTree#processToken(com.antlersoft.parser.Symbol, java.lang.String)
	 */
	protected void processToken(Symbol s, String value) throws LexException, RuleActionException {
		m_reader.processToken( new PunctuationToken(value, s));
	}


}