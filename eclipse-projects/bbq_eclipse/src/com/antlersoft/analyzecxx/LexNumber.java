package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

class LexNumber implements LexState {
	static final int INITIAL_DIGIT = 0;
	static final int INITIAL_ZERO = 1;
	static final int FLOAT_FRACTION = 2;
	static final int EXPONENT_SIGN=3;
	static final int EXPONENT_DIGITS=4;
	static final int HEX_DIGITS=5;
	static final int OCTAL_DIGITS=6;
	static final int INT_SUFFIX=7;

	private CxxReader m_reader;
	private LexState m_caller;
	private int m_state;
	private StringBuffer m_buffer;

	LexNumber( CxxReader reader, LexState caller, char c)
	{
		m_reader=reader;
		m_caller=caller;

		switch ( c)
		{
		case '0' : m_state=INITIAL_ZERO;
						break;
		case '.' : m_state=FLOAT_FRACTION;
						break;
		default :
			m_state=INITIAL_DIGIT;
		}
		m_buffer=new StringBuffer();
		m_buffer.append( c);
	}
    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		char comp_c=( c>='a' && c<='z' ) ? (char)( (int)c-(int)'a'+(int)'A') : c;
		switch ( m_state)
		{
			case INITIAL_DIGIT :
				switch ( comp_c)
				{
					case '.' : m_state=FLOAT_FRACTION; break;
					case 'L' :
					case 'U' : m_state=INT_SUFFIX; break;
					case 'E' : m_state=EXPONENT_SIGN; break;
					default :
						if ( ! CharClass.isDigit( comp_c))
						{
							finish();
							return m_caller.nextCharacter( c);
						}
				}
				m_buffer.append( comp_c);
				break;
			case INITIAL_ZERO :
				switch ( comp_c)
				{
					case '.' : m_state=FLOAT_FRACTION; break;
				case 'L':
				case 'U':
					m_state = INT_SUFFIX;
					break;
				case 'E':
					m_state = EXPONENT_SIGN;
					break;
				case 'X' :
					m_state=HEX_DIGITS;
					break;
				default :
					if ( CharClass.isDigit( comp_c))
					{
						if ( ! CharClass.isOctalDigit( comp_c))
							throw new LexException( "Bad octal number!");
						m_state=OCTAL_DIGITS;
					}
					else
					{
						finish();
						return m_caller.nextCharacter( c);
					}
				}
				m_buffer.append( comp_c);
				break;
			case FLOAT_FRACTION :
				switch ( comp_c)
				{
					case 'L' :
					case 'F' :
						m_buffer.append( comp_c);
						finish();
						return m_caller;
					case 'E' :
						m_state=EXPONENT_SIGN;
						break;
					default :
						if ( ! CharClass.isDigit( comp_c))
						{
							finish();
							return m_caller.nextCharacter(c);
						}
				}
				m_buffer.append( comp_c);
				break;
			case EXPONENT_SIGN :
				if ( c=='+' || c=='-' || CharClass.isDigit(c))
				{
					m_buffer.append( c);
					m_state=EXPONENT_DIGITS;
					break;
				}
				else
					throw new LexException( "Improperly formatted exponent");
			case EXPONENT_DIGITS :
				switch ( comp_c)
				{
					case 'L' :
					case 'F' :
						m_buffer.append( comp_c);
						finish();
						return m_caller;
					default :
						if ( ! CharClass.isDigit( c))
						{
							finish();
							return m_caller.nextCharacter( c);
						}
				}
				m_buffer.append( c);
				break;
			case HEX_DIGITS :
				switch ( comp_c)
				{
					case 'U' :
					case 'L' :
						m_state=INT_SUFFIX;
						break;
					default :
						if ( ! CharClass.isHexDigit( comp_c))
						{
							finish();
							return m_caller.nextCharacter( c);
						}
				}
				m_buffer.append( c);
				break;
			case OCTAL_DIGITS :
				switch ( comp_c)
				{
					case 'U' :
					case 'L' :
						m_state=INT_SUFFIX;
						break;
					default :
						if ( ! CharClass.isOctalDigit( comp_c))
						{
							finish();
							return m_caller.nextCharacter( c);
						}
				}
				m_buffer.append( c);
				break;
			case INT_SUFFIX :
				if ( comp_c=='U' || comp_c=='L' &&
				comp_c!=m_buffer.charAt( m_buffer.length()-1))
		   {
			   m_buffer.append( comp_c);
			   finish();
			   return m_caller;
				}
			default :
				throw new LexException ( "Bad number parsing state");
			}
		return this;
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }
	void finish()
	{
		m_reader.m_lex_to_preprocess.processToken( new LexToken( PreprocessParser.lex_number,
			m_buffer.toString()));
	}
}