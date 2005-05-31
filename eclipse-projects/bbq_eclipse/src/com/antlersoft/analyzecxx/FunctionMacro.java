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
import java.util.HashSet;
import java.util.Iterator;

import com.antlersoft.parser.RuleActionException;

class FunctionMacro extends Macro
{
	private ArrayList m_replacement_list;
	private int m_arguments;

	FunctionMacro( String name, int arguments, ArrayList replacement_list)
	{
		super( name);
		m_arguments=arguments;
		m_replacement_list=replacement_list;
	}

	void expandTo( LexToken orig_token, MacroExpander reader, ArrayList arguments, ArrayList expanded_arguments)
	throws RuleActionException
	{
		HashSet new_no_expand=orig_token.setWithNewMember( getIdentifier());
		if ( arguments.size()!=m_arguments || expanded_arguments.size()!=m_arguments)
			throw new RuleActionException( "Bad argument count for macro "+getIdentifier());
		for ( Iterator i=m_replacement_list.iterator(); i.hasNext();)
		{
			LexToken token=(LexToken)i.next();
			if ( token instanceof SpecialExpander)
				((SpecialExpander)token).expandTo( reader, new_no_expand, arguments, expanded_arguments);
			else
				reader.processToken( token.cloneWithNewSet( new_no_expand));
		}
	}
}