package com.antlersoft.analyzecxx;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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
	MacroExpander m_base_expander;
	static HashSet m_empty_set=new HashSet();
	static SimpleDateFormat m_date_format=new SimpleDateFormat( "MMM dd yyyy");
	static SimpleDateFormat m_time_format=new SimpleDateFormat( "HH:mm:ss");

	PreprocessParserBase( ParseState[] states, CxxReader reader, Properties initial_defines)
	{
		super( states);
		m_reader=reader;
		m_skipping=false;
		m_skip_depth=0;
		m_macros=new HashMap();
		m_base_expander=new MacroExpander( m_macros, m_reader.m_preprocessing_output);

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
//System.out.println( "Accept "+s.toString());
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
	throws RuleActionException
	{
		try
		{
			for (Iterator i = tokens.iterator(); i.hasNext(); )
				m_base_expander.processToken((LexToken) i.next());
			m_base_expander.processToken( WhiteSpace.m_new_line_token);
		}
		catch ( LexException le)
		{
			throw new RuleActionException( le.getMessage());
		}
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
		normalizeReplacementTokens( replacement_list);
		replaceConcat( replacement_list,
					   new ConcatenatedTokenPairReplacer() {
			public LexToken replacePair( LexToken a, LexToken b)
			{
				return concatenateTokens( a, b);
			}
		});

		m_macros.put( name, new ObjectMacro( name, replacement_list));
		return "";
	}

	final Object defineFunctionMacro( String name, ArrayList identifier_list, ArrayList replacement_list)
	throws RuleActionException
	{
		HashMap identifier_names=new HashMap( identifier_list.size());
		int identifier_count=0;
		for ( Iterator i=identifier_list.iterator(); i.hasNext();)
		{
			identifier_names.put( ((LexToken)i.next()).value, new Integer( identifier_count++));
		}
		if ( identifier_names.size()!=identifier_list.size())
			throw new RuleActionException( "Error defining macro: "+name+".  Duplicate parameter names.");
		normalizeReplacementTokens( replacement_list);
		int len=replacement_list.size();
		// Look for parameters and stringized parameters in the replacement list
		for ( int i=0; i<len; ++i)
		{
			LexToken token=(LexToken)replacement_list.get( i);
			if ( token.symbol==PreprocessParser.lex_identifier)
			{
				Integer arg_index=(Integer)identifier_names.get( token.value);
				if ( arg_index!=null)
				{
					// Look for stringize operator
					int candidate_stringize=isStringize( replacement_list, i-1);
					if ( candidate_stringize>=0)
					{
						LexToken string_expander=new StringExpander( arg_index.intValue(),
							((AltSymbolToken)replacement_list.get( candidate_stringize)).m_alt_symbol==
							PreprocessParser.pp_wide_stringize);
						for ( ; i >candidate_stringize; --i)
						{
							replacement_list.remove( candidate_stringize);
							len--;
						}
						replacement_list.set( i, string_expander);
					}
					else
					{
						replacement_list.set( i, new ParameterExpander( arg_index.intValue()));
					}
				}
			}
		}
		// Look for and replace concatenation operators in the replacement list
		replaceConcat( replacement_list, new ConcatenatedTokenPairReplacer () {
			public LexToken replacePair( LexToken a, LexToken b)
			{
				return new ConcatExpander( PreprocessParserBase.this, a, b);
			}
		});
		m_macros.put( name, new FunctionMacro( name, identifier_count, replacement_list));
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
	final static void normalizeWhitespace( ArrayList tokens)
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
		if ( tokens.size()>0 && ((LexToken)tokens.get(0)).symbol==PreprocessParser.lex_white_space)
			tokens.remove(0);
	}

	/**
	 * Concatenate two lex tokens as with the ## operator; if no single
	 * valid token results, return null
	 */
	final LexToken concatenateTokens( LexToken a, LexToken b)
	{
		String value=(a.value==null ? a.symbol.toString() : a.value)+
		(b.value==null ? b.symbol.toString() : b.value);
	    int len=value.length();
		try
		{
			InitialMacroReader reader = new InitialMacroReader();
			LexState initial_tokens = new PreprocessorTokens(reader, this);
			for (int i = 0; i < len; ++i) {
				initial_tokens = initial_tokens.nextCharacter(value.charAt(
					i));
			}
			initial_tokens=initial_tokens.endOfFile();
			ArrayList result=reader.getTokens();
			if ( result.size()==1)
				return (LexToken)result.get(0);
		}
		catch ( Exception e)
		{
System.err.println( "Exception parsing concatenated token: "+value);
e.printStackTrace();
		}
		return null;
	}

	private static int isStringize( ArrayList tokens, int start)
	{
		if ( start<0)
			return -1;
		if ( start>=tokens.size())
			return -1;
		LexToken token=(LexToken)tokens.get( start);
		if ( token.symbol==PreprocessParser.lex_white_space)
			return isStringize( tokens, start-1);
		if ( token instanceof AltSymbolToken)
		{
			Symbol alt=((AltSymbolToken)token).m_alt_symbol;
			if ( alt==PreprocessParser.pp_hash || alt==PreprocessParser.pp_wide_stringize)
				return start;
		}
		return -1;
	}

	private static void normalizeReplacementTokens( ArrayList tokens)
	throws RuleActionException
	{
		normalizeWhitespace( tokens);
		if (tokens.size() > 0) {
			confirmNotConcatenate( (LexToken) tokens.get(0));
			confirmNotConcatenate( (LexToken) tokens.get(tokens.size() - 1));
		}
	}

	private static void confirmNotConcatenate( LexToken token)
	throws RuleActionException
	{
		if ( token instanceof PunctuationToken)
		{
			if ( ((PunctuationToken)token).m_alt_symbol==
				PreprocessParser.pp_token_concat)
			   throw new RuleActionException( "## must not be the first or last token in a replacement list");
		}
	}

	private static void replaceConcat( ArrayList tokens, ConcatenatedTokenPairReplacer replacer)
	{
		int len=tokens.size()-1;
		for ( int i=1; i<len; ++i)
		{
			LexToken token=(LexToken)tokens.get( i);
			if ( token instanceof AltSymbolToken && ((AltSymbolToken)token).m_alt_symbol==
				 PreprocessParser.pp_token_concat)
			{
				int previous_token_index=i-1;
				if ( ((LexToken)tokens.get( previous_token_index)).symbol==
					 PreprocessParser.lex_white_space)
				{
					if ( previous_token_index==0)
						continue;
					--previous_token_index;
				}
				int next_token_index=i+1;
				if ( ((LexToken)tokens.get( next_token_index)).symbol==
					 PreprocessParser.lex_white_space)
				{
					if ( next_token_index==len)
						break;
					++next_token_index;
				}
				LexToken replacement=replacer.replacePair(
								(LexToken)tokens.get( previous_token_index),
								(LexToken)tokens.get( next_token_index));
				if ( replacement!=null)
				{
					int removed=next_token_index-previous_token_index+1;
					while ( 0<removed--)
					{
						tokens.remove( previous_token_index);
					}
					tokens.add( previous_token_index, replacement);
					len-=next_token_index-previous_token_index;
					i=previous_token_index;
				}
				else
				{
					tokens.remove(i);
					--len;
					--i;
				}
			}
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