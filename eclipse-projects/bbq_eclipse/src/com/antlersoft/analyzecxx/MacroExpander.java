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
				if ( next_token instanceof AltSymbolToken &&
					 ((AltSymbolToken)next_token).m_alt_symbol==PreprocessParser.pp_lparen)
				{

				}
			}
		}
	}

    void noMoreTokens() throws com.antlersoft.parser.RuleActionException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexReader method*/
	throw new java.lang.UnsupportedOperationException("Method noMoreTokens() not yet implemented.");
    }

	static class Argument
	{
		/* Tokens in the argument */
		ArrayList m_tokens;
		/* Hash sets corresponding to each token, used in expanding argument */
		ArrayList m_no_expands;
	}
}