package com.antlersoft.query;

import java.util.Enumeration;

public abstract class SetExpression {
	public abstract Class getResultClass();
	public Ordering getOrdering()
	{
		return null;
	}
	public abstract Enumeration evaluate( DataSource source);
}