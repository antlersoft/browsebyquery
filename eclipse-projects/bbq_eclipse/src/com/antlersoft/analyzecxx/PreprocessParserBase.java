package com.antlersoft.analyzecxx;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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
	CxxReader m_reader;
	static SimpleDateFormat m_date_format=new SimpleDateFormat( "MMM dd yyyy");
	static SimpleDateFormat m_time_format=new SimpleDateFormat( "HH:mm:ss");

	PreprocessParserBase( ParseState[] states, CxxReader reader, Properties initial_defines)
	{
		super( states);
		m_reader=reader;
		m_skipping=false;
		m_skip_depth=0;
		m_macros=new HashMap();

		// Create built-in macros
		m_macros.put( "__LINE__", new LineMacro("__LINE__"));
		m_macros.put( "__FILE__", new FileMacro("__FILE__"));

		Date now=new Date();
		m_macros.put( "__DATE__", new ObjectMacro("__DATE__",
		    new StringLiteral( m_date_format.format( now), false)));
		m_macros.put( "__TIME__", new ObjectMacro("__TIME__",
		    new StringLiteral( m_time_format.format( now), false)));
		NumericLiteral version=new NumericLiteral( 199711);
		version.setFlag( 'L');
		m_macros.put( "__cplusplus", new ObjectMacro("__cplusplus", version));


		// Add macros from initial defines
		for ( Enumeration names=initial_defines.propertyNames();
			  names.hasMoreElements();)
		{
			String name=(String)names.nextElement();
			String value=initial_defines.getProperty( name);
			if ( value==null)
				value="";
			addInitialMacro( name, value);
		}
	}

	public boolean parse( Symbol s, Object v)
	{
System.out.println( "Accept "+s.toString());
		return super.parse( s, v);
	}
	public boolean parse( Symbol s)
	{
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
				m_skipping=true;
				m_skip_depth=1;
			}
		}
		return "";
	}

	/**
	 * Perform macro expansions on tokens and pass them on to the next stage
	 */
	final String expandAndSend( ArrayList tokens)
	{
		return "";
	}

	/**
	 * Evaluate "defined" terms in preprocessor constant expressions
	 */
	final NumericLiteral evaluateDefined( String identifier)
	{
		Object o=m_macros.get( identifier);
		return new NumericLiteral( o==null ? 0 : 1);
	}

	private void addInitialMacro( String name, String value)
	{
		int len=value.length();
		if ( len==0)
			m_macros.put( name, new ObjectMacro( name));
		else
		{
			try
			{
				InitialMacroReader reader = new InitialMacroReader();
				LexState initial_tokens = new PreprocessorTokens(reader, this);
				for (int i = 0; i < len; ++i) {
					initial_tokens = initial_tokens.nextCharacter(value.charAt(
						i));
				}
				initial_tokens=initial_tokens.endOfFile();
				m_macros.put(name, new ObjectMacro(name, reader.getTokens()));
			}
			catch ( Exception e)
			{
System.err.println( "Exception parsing initial define macro "+name+": "+value);
e.printStackTrace();
			}
		}
	}

	final Object defineObjectMacro( String name, ArrayList replacement_list)
	throws RuleActionException
	{
		normalizeWhitespace( replacement_list);

		Macro macro=(Macro)m_macros.get( name);
		if ( macro!=null)
		{
			if ( macro instanceof ObjectMacro)
			{
				ObjectMacro om=(ObjectMacro)macro;
				if ( om.getTokens().equals( replacement_list))
					return "";
				throw new RuleActionException( "Redefining macro "+name+" to different value.");
			}
			else
				throw new RuleActionException( "Redefining function macro "+name+" as object macro.");
		}
		m_macros.put( name, new ObjectMacro( name, replacement_list));
		return "";
	}

	final Object undefineMacro( String name)
	{
		m_macros.remove( name);
		return "";
	}

	/**
	 * Remove trailing whitespace and convert repeated whitespace to
	 * single whitespace in list of tokens
	 */
	private void normalizeWhitespace( ArrayList tokens)
	throws RuleActionException
	{
		boolean whitespace_last=false;
		for ( Iterator i=tokens.iterator(); i.hasNext();)
		{
			LexToken next_token=(LexToken)i.next();
			if ( next_token.symbol==PreprocessParser.lex_white_space)
			{
				if ( whitespace_last)
					i.remove();
				else
					whitespace_last=true;
			}
			else
				whitespace_last=false;
		}
		if ( whitespace_last)
		{
			tokens.remove( tokens.size()-1);
		}
		if ( tokens.size()>0)
		{
			confirmNotConcatenate( (LexToken)tokens.get(0));
			confirmNotConcatenate( (LexToken)tokens.get( tokens.size()-1));
		}
	}

	private void confirmNotConcatenate( LexToken token)
	throws RuleActionException
	{
		if ( token instanceof PunctuationToken)
		{
			if ( ((PunctuationToken)token).m_alt_symbol==
				PreprocessParser.pp_token_concat)
			   throw new RuleActionException( "## must not be the first or last token in a replacement list");
		}
	}

	class LineMacro extends ObjectMacro
	{
		LineMacro( String name)
		{
			super( name);
		}

		ArrayList getTokens()
		{
			ArrayList result=new ArrayList(1);
			result.add( new NumericLiteral( m_reader.m_line));
			return result;
		}
	}

	class FileMacro extends ObjectMacro
	{
		FileMacro( String name)
		{
			super( name);
		}

		ArrayList getTokens()
		{
			ArrayList result=new ArrayList(1);
			result.add( new StringLiteral( m_reader.m_file, false));
			return result;
		}
	}
}