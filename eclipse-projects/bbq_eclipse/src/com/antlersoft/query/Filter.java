package com.antlersoft.query;

import java.util.List;

public abstract class Filter implements ValueExpression {
	public abstract boolean booleanValue();

	public final Object getValue()
	{
		return adjustedBooleanValue() ? Boolean.TRUE : Boolean.FALSE;
	}

	public final void applyNot()
	{
		m_not= ! m_not;
	}

	public final boolean adjustedBooleanValue()
	{
		return m_not ? ! booleanValue() : booleanValue();
	}

	private boolean m_not;
}