package analyzer.query;

import analyzer.DBFieldReference;
import analyzer.DBField;

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