package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBField;

class FieldsIn extends TransformImpl
{
	FieldsIn()
	{
		super( DBClass.class, DBField.class);
	}

	public Enumeration transform( Object toTransform)
		throws Exception
	{
		return ((DBClass)toTransform).getFields();
	}
}
