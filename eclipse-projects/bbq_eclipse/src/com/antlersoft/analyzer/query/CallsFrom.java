package analyzer.query;

import java.util.Enumeration;
import analyzer.DBMethod;
import analyzer.DBCall;

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

