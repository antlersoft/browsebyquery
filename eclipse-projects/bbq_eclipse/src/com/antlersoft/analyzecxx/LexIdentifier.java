package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;

import java.io.IOException;

public class LexIdentifier implements LexState {
	private CxxReader m_reader;
	private LexState m_caller;
	private StringBuffer m_buffer;
	private SymbolFinder m_finder;
	private static SymbolFinderTree m_tree=new SymbolFinderTree(
		   new String[] {
		   "asm", "auto", "bool", "break", "case", "catch", "char", "class",
		   "const", "const_cast", "continue", "default",
		   "define", "defined",
		   "delete", "do", "double", "dynamic_cast",
		   "elif",
		   "else", "enum",
		   "endif",
		   "explicit", "extern", "false", "float", "for", "friend", "goto", "if",
		   "ifdef", "ifndef", "include",
		   "inline", "int", "long", "mutable", "namespace", "new",
		   "operator", "private", "protected", "public", "register",
		   "reinterpret_cast", "return",
		   "short", "signed", "sizeof", "static", "static_cast", "struct",
		   "switch", "template", "this", "throw", "true", "try", "typedef",
		   "typeid", "typename", "union", "unsigned", "using", "virtual",
		   "void", "volatile", "wchar_t", "while"
		   }
		   );

	LexIdentifier( CxxReader reader, LexState caller, char initial)
	{
		m_reader=reader;
		m_caller=caller;
		m_finder=new SymbolFinder( m_tree);
		addCharacter( initial);
	}
    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		LexState result=this;
		if ( CharClass.isIdentifierPart( c))
		{
			addCharacter( c);
		}
		else
		{
			if ( m_buffer==null)
			{
				Symbol s=m_finder.currentSymbol();
				if ( s==null)
					m_reader.m_lex_to_preprocess.processToken(
					new LexToken( PreprocessParser.lex_identifier, m_finder.getRemainder()));
				else
					m_reader.m_lex_to_preprocess.processToken(
						new AltSymbolToken( PreprocessParser.lex_identifier,
						s.toString(), s));
			}
			else
				m_reader.m_lex_to_preprocess.processToken( new LexToken(
				    PreprocessParser.lex_identifier, m_buffer.toString()));
			result=m_caller.nextCharacter( c);
		}
		return result;
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }

	private void addCharacter( char c)
	{
		if ( m_buffer==null)
		{
			if ( ! m_finder.accept( c))
			{
				m_buffer=new StringBuffer(20);
				Symbol s=m_finder.currentSymbol();
				if ( s!=null)
					m_buffer.append( m_finder.currentText());
				m_buffer.append( m_finder.getRemainder());
				m_finder=null;
			}
		}
		else
			m_buffer.append( c);
	}
}