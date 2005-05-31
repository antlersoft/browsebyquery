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
		for (Iterator i = start.iterator(); i.hasNext(); )
			reader.processToken( ((LexToken) i.next()).cloneWithMergedSet( no_expand));
		for (Iterator i = end.iterator(); i.hasNext(); )
			reader.processToken(((LexToken) i.next()).cloneWithMergedSet( no_expand));
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
				MacroExpander expander=new MacroExpander( new HashMap(), reader, m_parser.m_reader.m_db);
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