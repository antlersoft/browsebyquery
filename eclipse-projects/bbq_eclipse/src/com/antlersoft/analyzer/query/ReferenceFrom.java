package analyzer.query;

import java.util.Enumeration;
import analyzer.DBFieldReference;
import analyzer.DBMethod;

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
