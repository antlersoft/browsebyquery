package com.antlersoft.query;

import java.util.Enumeration;

public class ValueListSetExpression extends SetExpression {
	public ValueListSetExpression( ValueList list)
	throws BindException
	{
		m_list=list;
		if ( list.getContextType()!=ValueContext.SCALAR)
			throw new BindException( "Non-scalarar value list can't be evaluated as a set expression");
	}
    public Enumeration evaluate(DataSource source) {
		return m_list.evaluate();
    }
    public Class getResultClass() {
		return m_list.resultClass();
    }

	private ValueList m_list;
}