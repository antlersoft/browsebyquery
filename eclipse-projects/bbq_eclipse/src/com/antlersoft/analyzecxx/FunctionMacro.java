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