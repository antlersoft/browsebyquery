package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import com.antlersoft.analyzer.DBFieldReference;
import com.antlersoft.analyzer.DBMethod;

class ReferenceFrom extends TransformImpl
{
	ReferenceFrom()
	{
		super( DBMethod.class, DBFieldReference.class);
	}

	public Enumeration transform( Object base)
		throws Exception
	{
		return ((DBMethod)base).getReferences();
	}
}
