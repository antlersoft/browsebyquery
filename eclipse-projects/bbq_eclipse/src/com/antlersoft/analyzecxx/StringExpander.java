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

		try
		{
			reader.processToken( new StringLiteral(sb.toString(), m_wide).cloneWithNewSet( no_expand));
		}
		catch ( LexException le)
		{
			throw new RuleActionException ( le.getMessage());
		}
    }
}