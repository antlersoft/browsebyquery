package analyzer.query;

import java.util.Enumeration;
import analyzer.DBMethod;
import analyzer.DBCall;

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

