package com.antlersoft.analyzecxx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;

/**
 * Concatenates the last token in the special expansion of a with the
 * fist token in the special expansion of b
 */
class ConcatExpander extends LexToken implements SpecialExpander {
	private LexToken m_a, m_b;
	private PreprocessParserBase m_parser;
	ConcatExpander( PreprocessParserBase parser, LexToken a, LexToken b)
	{
		super( Symbol.get( "pp_concat_expr"), "");
		m_parser=parser;
		m_a=a;
		m_b=b;
	}

    public void expandTo(MacroExpander reader, HashSet no_expand, ArrayList arguments, ArrayList expanded_arguments) throws RuleActionException {
		ArrayList start=convertParameters( m_a, no_expand, arguments, expanded_arguments);
		ArrayList end=convertParameters( m_b, no_expand, arguments, expanded_arguments);
		if ( start.size()>0 && end.size()>0)
		{
			LexToken concat=m_parser.concatenateTokens( (LexToken)start.get( start.size()-1),
				(LexToken)end.get(0));
			if ( concat!=null)
			{
				start.remove( start.size()-1);
				end.set( 0, concat);
			}
		}
		try
		{
			for (Iterator i = start.iterator(); i.hasNext(); )
				reader.processToken( ((LexToken) i.next()).cloneWithMergedSet( no_expand));
			for (Iterator i = end.iterator(); i.hasNext(); )
				reader.processToken(((LexToken) i.next()).cloneWithMergedSet( no_expand));
		}
		catch ( LexException le)
		{
			throw new RuleActionException( le.getMessage());
		}
    }

	/**
	 * Convert one of the concatenated parameters to an array list of tokens, depending on its type
	 */
	private ArrayList convertParameters( LexToken a, HashSet no_expand, ArrayList arguments, ArrayList expanded_arguments)
	throws RuleActionException
	{
		ArrayList result;
		if ( a instanceof SpecialExpander)
		{
			if ( a instanceof ParameterExpander)
				result=(ArrayList)arguments.get( ((ParameterExpander)a).getIndex());
			else
			{
				InitialMacroReader reader=new InitialMacroReader();
				MacroExpander expander=new MacroExpander( new HashMap(), reader);
				((SpecialExpander)a).expandTo( expander, no_expand, arguments, expanded_arguments);
				expander.noMoreTokens();
				result=reader.getTokens();
			}
		}
		else
		{
			result=new ArrayList(1);
			result.add( a);
		}
		return result;
	}
}