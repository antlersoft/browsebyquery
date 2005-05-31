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

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;

class StringExpander extends LexToken implements SpecialExpander
{
	private int m_arg_index;
	private boolean m_wide;
	StringExpander( int arg_index, boolean wide)
	{
		super( Symbol.get( "pp_stringize_exp"), "");
		m_arg_index=arg_index;
		m_wide=wide;
	}
    public void expandTo(MacroExpander reader, HashSet no_expand, ArrayList arguments, ArrayList expanded_arguments) throws RuleActionException {
		ArrayList tokens=(ArrayList)arguments.get( m_arg_index);
		StringBuffer sb=new StringBuffer();

		for ( Iterator it=tokens.iterator(); it.hasNext();)
		{
			LexToken token=(LexToken)it.next();
			if ( token.symbol==PreprocessParser.lex_white_space)
				sb.append( ' ');
			else if ( token.symbol==PreprocessParser.lex_character_literal)
			{
				sb.append( '\'');
				String to_add=token.value;
				int len=to_add.length();
				for ( int i=0; i<len; ++i)
				{
					char c=to_add.charAt( i);
					if ( c=='"' || c=='\\')
						sb.append( '\\');
					sb.append( c);
				}
				sb.append( '\'');
			}
			else if ( token.symbol==PreprocessParser.lex_string_literal)
			{
				sb.append( "\\\"");
				String to_add=token.value;
				int len=to_add.length();
				for ( int i=0; i<len; ++i)
				{
					char c=to_add.charAt( i);
					if ( c=='"' || c=='\\')
						sb.append( '\\');
					sb.append( c);
				}
				sb.append( "\\\"");
			}
			else sb.append( token.value);
		}

		reader.processToken( new StringLiteral(sb.toString(), m_wide).cloneWithNewSet( no_expand));
    }
}