package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import com.antlersoft.analyzer.DBMethod;
import com.antlersoft.analyzer.DBCall;

class CallsTo extends TransformImpl
{
	CallsTo()
	{
		super( DBMethod.class, DBCall.class);
	}

	public Enumeration transform( Object method)
		throws Exception
	{
		return ((DBMethod)method).getCalledBy();
	}
}

