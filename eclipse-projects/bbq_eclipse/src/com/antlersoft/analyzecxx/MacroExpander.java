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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.antlersoft.parser.RuleActionException;

class MacroExpander {
	HashMap m_macros;
	LexReader m_reader;
	DBDriver m_db;
	/** Null if no function macro name has been recognized */
	FunctionMacro m_current_function_macro;
	/** Collection of tokens that have been deferred
	 * pending recognition of complete macro invocation
	 */
	ArrayList m_deferred_tokens;
	/** Null if no function macro opening paren has been recognized */
	Argument m_current_argument;
	/** When this is one, commas are significant */
	int m_paren_depth;
	/** ArrayList of Argument objects, each of which are the tokens in an argument */
	ArrayList m_arguments;

	MacroExpander( HashMap macros, LexReader reader, DBDriver db)
	{
		m_macros=macros;
		m_reader=reader;
		m_deferred_tokens=new ArrayList();
		m_db=db;
	}

    void processToken( LexToken next_token) throws com.antlersoft.parser.RuleActionException
	{
		// What no expand set do we use to expand the macro-- the one at the initial token,
		// the one at the last token, or the one that is the union of all the tokens?
		if ( m_current_function_macro==null)
		{
			if ( next_token.symbol==PreprocessParser.lex_identifier && next_token.canExpand( next_token.value))
			{
				Macro macro=(Macro)m_macros.get( next_token.value);
				if ( macro==null)
				{
					m_reader.processToken( next_token);
				}
				else
				{
					if ( macro instanceof ObjectMacro)
					{
						HashSet new_set=next_token.setWithNewMember( macro.getIdentifier());
						ArrayList replacement_list=((ObjectMacro)macro).getTokens();
						for ( Iterator i=replacement_list.iterator();
							  i.hasNext();)
						{
							processToken( ((LexToken)i.next()).cloneWithNewSet( new_set));
						}
					}
					else
					{
						m_current_function_macro=(FunctionMacro)macro;
						m_deferred_tokens.add( next_token);
					}
				}
			}
			else
				m_reader.processToken( next_token);
		}
		else
		{
			if ( m_current_argument==null)
			{
				// Looking for the leading paren of macro invocation
				if ( next_token instanceof AltSymbolToken &&
					 ((AltSymbolToken)next_token).m_alt_symbol==PreprocessParser.pp_lparen)
				{
					m_deferred_tokens.add( next_token);
					m_current_argument=new Argument();
					m_arguments=new ArrayList();
					m_arguments.add( m_current_argument);
					m_paren_depth=1;
				}
				else if ( next_token.symbol==PreprocessParser.lex_new_line ||
						  next_token.symbol==PreprocessParser.lex_white_space)
				{
					m_deferred_tokens.add( next_token);
				}
				else
				{
					m_current_function_macro=null;
					clearDeferredTokens();
					m_reader.processToken( next_token);
				}
			}
			else
			{
				if ( next_token instanceof AltSymbolToken &&
					((AltSymbolToken)next_token).m_alt_symbol==PreprocessParser.pp_lparen)
				{
					m_paren_depth++;
					m_current_argument.add( next_token);
					m_deferred_tokens.add( next_token);
				}
				else if ( next_token instanceof AltSymbolToken &&
					((AltSymbolToken)next_token).m_alt_symbol==PreprocessParser.pp_rparen)
				{
					m_paren_depth--;
					if ( m_paren_depth==0)
					{
						// Macro complete-- expand it
						if ( m_current_argument.isEmpty())
							m_arguments.remove( m_arguments.size()-1);
						FunctionMacro fm=m_current_function_macro;
						m_current_function_macro=null;
						LexToken orig_token=(LexToken)m_deferred_tokens.get(0);
						m_deferred_tokens.clear();
						m_current_argument=null;
						ArrayList args=new ArrayList( m_arguments.size());
						ArrayList exp_args=new ArrayList( m_arguments.size());
						for ( Iterator i=m_arguments.iterator(); i.hasNext();)
						{
							Argument arg=(Argument)i.next();
							args.add( arg.getArgumentTokens());
							exp_args.add( arg.getExpandedArgumentTokens( m_macros, m_db));
						}
						m_arguments=null;
						fm.expandTo( orig_token, this, args, exp_args);
					}
					else
					{
						m_current_argument.add( next_token);
						m_deferred_tokens.add( next_token);
					}
				}
				else if ( m_paren_depth==1 && next_token instanceof AltSymbolToken &&
					((AltSymbolToken)next_token).m_alt_symbol==PreprocessParser.pp_comma)
				{
				    m_current_argument=new Argument();
					m_arguments.add( m_current_argument);
					m_deferred_tokens.add( next_token);
				}
				else
				{
					m_current_argument.add( next_token);
					m_deferred_tokens.add( next_token);
				}
			}
		}
	}

    void noMoreTokens() throws com.antlersoft.parser.RuleActionException {
		if ( m_current_function_macro!=null)
		{
			m_current_function_macro=null;
			m_current_argument=null;
			clearDeferredTokens();
		}
		m_reader.noMoreTokens();
    }

	private void clearDeferredTokens() throws RuleActionException
	{
		ArrayList deferred = (ArrayList) m_deferred_tokens.clone();
		m_deferred_tokens.clear();
		Iterator i = deferred.iterator();
		if (i.hasNext()) {
			m_reader.processToken( (LexToken)i.next());
			while (i.hasNext()) {
				processToken( (LexToken)i.next());
			}
		}
	}

	static class Argument
	{
		/** Tokens in the argument */
		ArrayList m_tokens;

		Argument()
		{
			m_tokens=new ArrayList();
		}

		void add( LexToken twne)
		{
			if ( twne.symbol==PreprocessParser.lex_new_line)
			{
				try
				{
					twne = (LexToken) twne.clone();
				}
				catch ( CloneNotSupportedException cnse)
				{

				}
				twne.symbol=PreprocessParser.lex_white_space;
			}
			m_tokens.add( twne);
		}

		boolean isEmpty()
		{
			return m_tokens.size()==0 || ( m_tokens.size()==1 && ((LexToken)m_tokens.get(0)).symbol==
				PreprocessParser.lex_white_space);
		}

		ArrayList getExpandedArgumentTokens( HashMap macros, DBDriver db)
		throws RuleActionException
		{
			InitialMacroReader reader=new InitialMacroReader();
			MacroExpander expander=new MacroExpander( macros, reader, db);
			Iterator token_i=m_tokens.iterator();
			while ( token_i.hasNext())
			{
				expander.processToken( (LexToken)token_i.next());
			}
			expander.noMoreTokens();
			return reader.getTokens();
		}

		ArrayList getArgumentTokens()
		{
			ArrayList result=(ArrayList)m_tokens.clone();
			PreprocessParserBase.normalizeWhitespace( result);
			return result;
		}
	}
}