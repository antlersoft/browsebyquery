package com.antlersoft.query;

/**
 * Simple implementation of ParserEnvironment
 * 
 * @author Michael A. MacDonald
 *
 */
public class ParserEnvironmentImpl implements ParserEnvironment {

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ParserEnvironment#getSelected()
	 */
	@Override
	public SetExpression getSelection() {
		return new EmptySetExpression();
	}

	private SetExpression m_expression;
	
	public SetExpression getLastParsedExpression() {
		return m_expression;
	}

	public void setLastParsedExpression(SetExpression expr) {
		m_expression=expr;
	}

}
