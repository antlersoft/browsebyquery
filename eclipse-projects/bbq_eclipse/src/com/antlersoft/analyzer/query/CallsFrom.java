package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import com.antlersoft.analyzer.DBMethod;
import com.antlersoft.analyzer.DBCall;

class CallsFrom extends TransformImpl
{
	CallsFrom()
	{
		super( DBMethod.class, DBCall.class);
	}

	public Enumeration transform( Object method)
		throws Exception
	{
		return ((DBMethod)method).getCalls();
	}
}

