/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.util.ArrayList;

import com.antlersoft.ilanalyze.*;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.RuleAction;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.ValueStack;

/**
 * @author Michael A. MacDonald
 *
 */
class IldasmParserBase extends Parser {
	
	DBDriver m_driver;
	
	IldasmParserBase( ParseState[] states)
	{
		super( states);
	}
	
	void setDriver( DBDriver driver)
	{
		m_driver=driver;
	}
	
	static DBDriver Driver( Parser p)
	{
		return ((IldasmParserBase)p).m_driver;
	}
	
	/** Action object that just returns the string at the top of the stack */
	static RuleAction m_TopString=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return valueStack.s_0();
		} };
		
	/** Action object that returns the concatenation of the string 2 deep with the operator between and then the top string  */
	static RuleAction m_StringConcat=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return valueStack.s_2() + valueStack.s_1()+ valueStack.s_0();
		}};
	
	/** Action that returns the top object from the stack */
	static RuleAction m_TopObject=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return valueStack.o_0();
		}};
		
	/** Create a basic type from the top string */
	static RuleAction m_BasicType=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return new BasicType( valueStack.s_0());
		} };

	/** Create a basic type from top two strings concated together */
	static RuleAction m_BasicType2Words=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return new BasicType( valueStack.s_1()+" "+valueStack.s_0());
		} };

	/** Start a list with the top item on the stack */
	static RuleAction m_StartList=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			ArrayList result=new ArrayList(); result.add( valueStack.o_0()); return result;
		} };
		
	/** Add to a list delimited with a single token (e.g., comma); add top of stack to list 2 deep on stack */
	static RuleAction m_AddToList=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			ArrayList result=(ArrayList)valueStack.o_2(); result.add( valueStack.o_0()); return result;
		} };

	/** Put a zero on the stack */
	static RuleAction m_EmptyAttribute=new AttribOnStack(0);
	
	/** Or a zero value to stack */
	static RuleAction m_AddIgnoredAttrib=new AttribAddToStack( 0);
	
	/** Bitwise or the top two stack values, which must be Integers */
	static RuleAction m_AddAttribute=new RuleAction() {
		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return new Integer( valueStack.i_1()|valueStack.i_0());
		} };
		
	/**
	 * Puts an int value on to the stack.
	 * @author Michael A. MacDonald
	 *
	 */
	static class AttribOnStack implements RuleAction {
		
		private int m_v;
		AttribOnStack( int v)
		{
			m_v=v;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack)
				throws RuleActionException {
			return new Integer( m_v);
		}

	}

	/**
	 * Or's an int value with the int 1 deep on the stack
	 * @author Michael A. MacDonald
	 *
	 */
	static class AttribAddToStack implements RuleAction {
		
		private int m_v;
		AttribAddToStack( int v)
		{
			m_v=v;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack)
				throws RuleActionException {
			return new Integer( valueStack.i_1()|m_v);
		}

	}

	/**
	 * Or's an int value with the int 2 deep on the stack
	 * @author Michael A. MacDonald
	 *
	 */
	static class AttribAddToStack2 implements RuleAction {
		
		private int m_v;
		AttribAddToStack2( int v)
		{
			m_v=v;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack)
				throws RuleActionException {
			return new Integer( valueStack.i_2()|m_v);
		}

	}


}

