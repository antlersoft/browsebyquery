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

class ParameterExpander extends LexToken implements SpecialExpander {
	private int m_arg_index;
	ParameterExpander( int arg_index)
	{
		super( Symbol.get( "pp_param_expr"), "");
		m_arg_index=arg_index;
	}

	int getIndex()
	{
		return m_arg_index;
	}

    public void expandTo(MacroExpander reader, HashSet no_expand, ArrayList arguments, ArrayList expanded_arguments) throws RuleActionException {
		ArrayList tokens=(ArrayList)expanded_arguments.get( m_arg_index);
		for ( Iterator i=tokens.iterator(); i.hasNext();)
			reader.processToken( ((LexToken) i.next()).cloneWithMergedSet( no_expand));
    }
}