package com.antlersoft.query;

public class StringConcat extends OperatorExpression {
	public StringConcat( ValueExpression a, ValueExpression b)
	throws BindException
	{
		super( a, b);
	}
    public Object getValue() {
		return m_a.getValue().toString()+m_b.getValue().toString();
    }
}