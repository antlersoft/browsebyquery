package com.antlersoft.query;

import java.util.Enumeration;

public class SetExpressionGroupTransform extends TransformImpl {
	public SetExpressionGroupTransform( SetExpression expr)
	{
		super( SetExpression.class, null);
		m_set_expression=expr;
	}
    public Enumeration transformObject(DataSource source, Object to_transform) {
		return new SingleEnum( m_set_expression);
    }

	private SetExpression m_set_expression;
}