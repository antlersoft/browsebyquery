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