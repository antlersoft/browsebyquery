package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

public class LexPunctuation implements LexState {
	private CxxReader m_reader;
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

	LexPunctuation( CxxReader reader, LexState caller, char initial)
	{
		m_reader=reader;
		m_caller=caller;
		m_finder=new SymbolFinder( m_tree);
		addCharacter( initial);
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		if ( ! CharClass.isOperator( c))
			return m_caller.nextCharacter( c);
		addCharacter( c);
		return this;
    }

    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }

	private void addCharacter( char c)
	{
		if ( ! m_finder.accept( c))
		{
		}
		else
		{
			if ( ! m_finder.canGrow())
			{
				m_reader.m_lex_to_preprocess( new LexToken
			}
		}
	}
}