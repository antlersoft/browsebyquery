package com.antlersoft.analyzecxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.antlersoft.parser.RuleActionException;

class MacroExpander {
	HashMap m_macros;
	LexReader m_reader;
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

	MacroExpander( HashMap macros, LexReader reader)
	{
		m_macros=macros;
		m_reader=reader;
		m_deferred_tokens=new ArrayList();
	}

    void processToken(HashSet no_expand, LexToken next_token) throws com.antlersoft.parser.RuleActionException, LexException
	{
		// What no expand set do we use to expand the macro-- the one at the initial token,
		// the one at the last token, or the one that is the union of all the tokens?
		if ( m_current_function_macro==null)
		{
			if ( next_token.symbol==PreprocessParser.lex_identifier)
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
						HashSet new_set=(HashSet)no_expand.clone();
						new_set.add( next_token.value);
						ArrayList replacement_list=((ObjectMacro)macro).getTokens();
						for ( Iterator i=replacement_list.iterator();
							  i.hasNext();)
						{
							processToken( new_set, (LexToken)i.next());
						}
					}
					else
					{
						m_current_function_macro=(FunctionMacro)macro;
						m_deferred_tokens.add( new TokenWithNoExpand( next_token, no_expand));
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
					m_deferred_tokens.add( new TokenWithNoExpand( next_token, no_expand));
					m_current_argument=new Argument();
					m_arguments=new ArrayList();
					m_arguments.add( m_current_argument);
					m_paren_depth=1;
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
					TokenWithNoExpand twne=new TokenWithNoExpand( next_token, no_expand);
					m_current_argument.add( twne);
					m_deferred_tokens.add( twne);
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
						m_deferred_tokens.clear();
						m_current_argument=null;
						ArrayList args=new ArrayList( m_arguments.size());
						ArrayList exp_args=new ArrayList( m_arguments.size());
						for ( Iterator i=m_arguments.iterator(); i.hasNext();)
						{
							Argument arg=(Argument)i.next();
							args.add( arg.getArgumentTokens());
							exp_args.add( arg.getExpandedArgumentTokens( m_macros));
						}
						m_arguments=null;
						fm.expandTo( this, no_expand, args, exp_args);
					}
					else
					{
						TokenWithNoExpand twne=new TokenWithNoExpand( next_token, no_expand);
						m_current_argument.add( twne);
						m_deferred_tokens.add( twne);
					}
				}
				else if ( m_paren_depth==1 && next_token instanceof AltSymbolToken &&
					((AltSymbolToken)next_token).m_alt_symbol==PreprocessParser.pp_comma)
				{
				    m_current_argument=new Argument();
					m_arguments.add( m_current_argument);
					m_deferred_tokens.add( new TokenWithNoExpand( next_token, no_expand));
				}
				else
				{
					TokenWithNoExpand twne=new TokenWithNoExpand( next_token, no_expand);
					m_current_argument.add( twne);
					m_deferred_tokens.add( twne);
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
    }

	private void clearDeferredTokens() throws RuleActionException
	{
		try
		{
			ArrayList deferred = (ArrayList) m_deferred_tokens.clone();
			m_deferred_tokens.clear();
			Iterator i = deferred.iterator();
			if (i.hasNext()) {
				m_reader.processToken( ( (TokenWithNoExpand) i.next()).m_token);
				while (i.hasNext()) {
					TokenWithNoExpand twne = (TokenWithNoExpand) i.next();
					processToken(twne.m_no_expand, twne.m_token);
				}
			}
		}
		catch ( LexException le)
		{
			throw new RuleActionException( le.getMessage());
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

		void add( TokenWithNoExpand twne)
		{
			if ( twne.m_token==WhiteSpace.m_new_line_token)
				twne.m_token=WhiteSpace.m_white_space_token;
			m_tokens.add( twne);
		}

		boolean isEmpty()
		{
			return m_tokens.size()==0 || ( m_tokens.size()==1 && ((TokenWithNoExpand)m_tokens.get(0)).m_token.symbol==
				PreprocessParser.lex_white_space);
		}

		ArrayList getExpandedArgumentTokens( HashMap macros)
		throws RuleActionException, LexException
		{
			InitialMacroReader reader=new InitialMacroReader();
			MacroExpander expander=new MacroExpander( macros, reader);
			Iterator token_i=m_tokens.iterator();
			while ( token_i.hasNext())
			{
				TokenWithNoExpand twne=(TokenWithNoExpand)token_i.next();
				expander.processToken( twne.m_no_expand, twne.m_token);
			}
			expander.noMoreTokens();
			return reader.getTokens();
		}

		ArrayList getArgumentTokens()
		{
			ArrayList result=new ArrayList( m_tokens.size());
			for ( Iterator i=m_tokens.iterator(); i.hasNext();)
				result.add( ((TokenWithNoExpand)i.next()).m_token);
			PreprocessParserBase.normalizeWhitespace( result);
			return result;
		}
	}

	static class TokenWithNoExpand
	{
		LexToken m_token;
		HashSet m_no_expand;

		TokenWithNoExpand( LexToken token, HashSet no_expand)
		{
			m_token=token;
			m_no_expand=no_expand;
		}
	}
}