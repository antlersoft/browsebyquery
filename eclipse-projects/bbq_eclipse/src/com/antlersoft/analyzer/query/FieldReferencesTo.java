package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import com.antlersoft.analyzer.DBFieldReference;
import com.antlersoft.analyzer.DBField;

class FieldReferencesTo extends TransformImpl
{
	FieldReferencesTo()
	{
		super( DBField.class, DBFieldReference.class);
	}

	public Enumeration transform( Object base)
		throws Exception
	{
		return ((DBField)base).getReferencedBy();
	}
}
