package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBCall;
import com.antlersoft.analyzer.DBMethod;

class MethodsOf extends UniqueTransform
{
	MethodsOf()
	{
		super( DBCall.class, DBMethod.class);
	}

	public Object uniqueTransform( Object toTransform)
	{
		return ((DBCall)toTransform).getTarget();
	}
}