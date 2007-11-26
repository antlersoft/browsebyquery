/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;
import com.antlersoft.parser.lex.LexWithSymbolTree;
import com.antlersoft.parser.lex.SymbolFinderTree;

/**
 * Lexer has encountered one or more punctuation characters that don't belong in other tokens.
 * See if the characters seen so far match any reserved word-- if they do, send them as tokens.
 * Continue collecting and testing punctuation tokens until you see a non-punctuation character.
 * If you see such a character, and you can't make tokens out of what you have, throw an exception.
 * @author Michael A. MacDonald
 *
 */
class PunctuationState extends LexWithSymbolTree {
	private IldasmReader m_reader;
	/**
	 * Set this to true when you think you've recognized a period token but it is really the start
	 * of a directive.
	 */
	private boolean m_period_exception;
	private boolean m_minus_exception;
	private static SymbolFinderTree m_tree;

	/**
	 * @param parent
	 * @param reader
	 */
	public PunctuationState(LexState parent, IldasmReader reader) {
		super(getTree(reader), parent);
		m_reader=reader;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexWithSymbolTree#isOperator(char)
	 */
	protected boolean isOperator(char c) {
		return isOp(c);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexWithSymbolTree#processToken(com.antlersoft.parser.Symbol, java.lang.String)
	 */
	protected void processToken(Symbol s, String value) throws LexException, RuleActionException {
		if ( s==null)
		{
			throw new LexException( "Unrecognized character: "+value);
		}
		if ( s==IldasmParser.t_period && m_reader.expectedReserved(value)==null)
		{
			m_period_exception=true;
			return;
		}
		if ( s==IldasmParser.t_minus && m_reader.expectedReserved(value)==null)
		{
			m_minus_exception=true;
			return;
		}
		m_reader.processToken(s, value);
	}
	
	/**
	 * Generate list of strings to generate tree; this (like building the tree) is fairly expensive,
	 * but we only do it once.
	 * @param reader that has the parser with the reserved wordfs we are interested in
	 * @return SymbolFinderTree built from punctuation reserved words
	 */
	private static SymbolFinderTree getTree( IldasmReader reader)
	{
		if ( m_tree==null)
		{
			ArrayList list=new ArrayList();
			for ( Iterator i=reader.getReservedWords().iterator(); i.hasNext();)
			{
				String s=(String)i.next();
				for ( int l=s.length()-1; l>=0; --l)
				{
					if ( ! isOp(s.charAt(l)))
					{
						s=null;
						break;
					}
				}
				if ( s!=null)
				{
					list.add(s);
				}
			}
			return new SymbolFinderTree( (String[])list.toArray( new String[list.size()]));
		}
		return m_tree;
	}
	
	private static boolean isOp( char c)
	{
		return ! CharClass.isIDPart(c) && ! CharClass.isWhiteSpace(c) && c!='\032' && c!='\000' && c!='"' && c!='\'' && c!='/';
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexWithSymbolTree#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		m_period_exception=false;
		m_minus_exception=false;
		LexState result=super.nextCharacter(c);
		if ( m_period_exception)
			result=new InitialPeriod( m_caller, m_reader).nextCharacter(c);
		if ( m_minus_exception)
			result=new InitialMinus( m_caller, m_reader).nextCharacter(c);
		else if ( c=='{' && m_reader.expectedReserved(".permissionContents")!=null)
		{
			result=new PermissionContentState( m_caller, m_reader);
		}
		return result;
	}

}
