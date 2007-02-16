package com.antlersoft.query;

/**
 * Simple implementation of ParserEnvironment
 * 
 * @author Michael A. MacDonald
 *
 */
public class ParserEnvironmentImpl implements ParserEnvironment {

	private SetExpression m_expression;
	
	public SetExpression getLastParsedExpression() {
		return m_expression;
	}

	public void setLastParsedExpression(SetExpression expr) {
		m_expression=expr;
	}

}
