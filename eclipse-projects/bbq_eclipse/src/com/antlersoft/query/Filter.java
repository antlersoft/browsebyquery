package com.antlersoft.query;

import java.util.List;

public abstract class Filter implements ValueExpression {
	boolean booleanValue()
	{
		return ((Boolean)getValue()).booleanValue();
	}
}