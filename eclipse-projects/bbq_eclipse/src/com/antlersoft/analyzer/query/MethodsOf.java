package analyzer.query;

import analyzer.DBCall;
import analyzer.DBMethod;

class MethodsOf extends UniqueTransform
{
	MethodsOf()
	{
		super( DBCall.class, DBMethod.class);
	}

	public Object uniqueTransform( Object toTransform)
	{
		return ((DBCall)toTransform).getTarget();
	}
}