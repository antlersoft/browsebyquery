package analyzer.query;

import java.util.Enumeration;
import java.util.Hashtable;

import analyzer.DBReference;
import analyzer.DBMethod;

class MethodsContaining extends UniqueTransform
{
    MethodsContaining()
    {
		super( DBReference.class, DBMethod.class);
    }

    public Object uniqueTransform( Object toTransform)
    {
		return ((DBReference)toTransform).getSource();
    }
}
