package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBFieldReference;
import com.antlersoft.analyzer.DBField;

class FieldsOf extends UniqueTransform
{
	FieldsOf()
	{
		super( DBFieldReference.class, DBField.class);
	}

	public Object uniqueTransform( Object toTransform)
	{
		return ((DBFieldReference)toTransform).getTarget();
	}
}