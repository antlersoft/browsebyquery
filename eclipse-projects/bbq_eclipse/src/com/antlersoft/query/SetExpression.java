package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public abstract class SetExpression {
	public abstract Class getResultClass();
	public Comparator getOrdering()
	{
		return null;
	}
	public abstract Enumeration evaluate( DataSource source);
}