package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
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
		{
			cleanRemainder();
			return m_caller.nextCharacter( c);
		}
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
			rescan();
		}
		else
		{
			if ( ! m_finder.canGrow())
			{
				Symbol s=m_finder.currentSymbol();
				m_reader.m_lex_to_preprocess.processToken( new PunctuationToken(
						m_finder.currentText(), s));
				m_finder.reset();
			}
		}
	}

	private void rescan()
	{
		Symbol s=m_finder.currentSymbol();
		if ( s!=null)
		{
			m_reader.m_lex_to_preprocess.processToken( new PunctuationToken(
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
				m_reader.m_lex_to_preprocess.processToken( new PunctuationToken( r.substring( 0,1),
					null));
			}
			for ( ; start_index<remainder_length; ++start_index)
				addCharacter( r.charAt( start_index));
		}
	}

	private void cleanRemainder()
	{
		rescan();
		if ( m_finder.isRemainder())
		{
			String r=m_finder.getRemainder();
			m_finder.reset();
			int remainder_length=r.length();
			for ( int i=0; i<remainder_length; ++i)
			{
				m_reader.m_lex_to_preprocess.processToken(
						new PunctuationToken( r.substring( i, i+1), null));
			}
		}
	}
}