package analyzer.query;

import java.util.Enumeration;
import analyzer.DBFieldReference;
import analyzer.DBField;

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
