package analyzer.query;

import java.util.Enumeration;
import analyzer.DBClass;
import analyzer.DBField;

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
