/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import com.antlersoft.ilanalyze.DBDriver;

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
	
	/** Action object that just returns the string at the top of the stack */
	static RuleAction m_TopString=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return valueStack.s_0();
		} };
		
	/** Action object that returns the concatenation of the string 2 deep with the top string (dropping operator between) */
	static RuleAction m_StringConcat=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return valueStack.s_2() + valueStack.s_0();
		}};
	
	/** Action that returns the top object from the stack */
	static RuleAction m_TopObject=new RuleAction() {

		/* (non-Javadoc)
		 * @see com.antlersoft.parser.RuleAction#ruleFire(com.antlersoft.parser.Parser, com.antlersoft.parser.ValueStack)
		 */
		public Object ruleFire(Parser parser, ValueStack valueStack) throws RuleActionException {
			return valueStack.o_0();
		}};
}

