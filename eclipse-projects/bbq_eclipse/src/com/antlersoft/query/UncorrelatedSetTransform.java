package com.antlersoft.query;

import java.util.Enumeration;

public class UncorrelatedSetTransform extends TransformImpl {
	public UncorrelatedSetTransform( SetExpression expr)
	{
		super( expr.getResultClass(), null);
		m_expression=expr;
	}
    public Enumeration transformObject(DataSource source, Object to_transform) {
		return m_expression.evaluate( source);
    }

	private SetExpression m_expression;
}