package com.antlersoft.analyzecxx;

import java.util.HashMap;
import java.util.Properties;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;

/**
 * This class extends the basic Parser to support the preprocessor-specific
 * functionality.  It is in turn extended by PreprocessorParser, which adds
 * only the stuff automatically generated from the grammar definition.
 */
class PreprocessParserBase extends Parser {
	HashMap m_macros;
	boolean m_skipping;
	int m_skip_depth;

	PreprocessParserBase( ParseState[] states, Properties initial_defines)
	{
		super( states);
		m_skipping=false;
		m_skip_depth=0;
		m_macros=new HashMap();
	}

	public boolean parse( Symbol s, Object v)
	{
System.out.println( "Accept "+s.toString());
		return super.parse( s, v);
	}
	public boolean parse( Symbol s)
	{
System.out.println( "Accept(no value) "+s.toString());
		return super.parse( s);
	}

	final Object acceptIfDefined( boolean condition, String identifier)
	{
		if ( m_skipping)
			++m_skip_depth;
		else
		{
			Object o=m_macros.get( identifier);
			if ( condition==( o==null))
			{
System.err.println( "If evaluated false ");
				m_skipping=true;
				m_skip_depth=1;
			}
else
System.err.println( "If evaluated true");
		}
		return "";
	}
}