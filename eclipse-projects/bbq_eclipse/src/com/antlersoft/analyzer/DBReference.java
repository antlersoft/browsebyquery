package analyzer;

import odb.ObjectRef;

public abstract class DBReference implements java.io.Serializable, Cloneable
{
    DBReference( DBMethod s, int l)
    {
	source=new ObjectRef( s);
	lineNumber=l;
    }

    private ObjectRef source;

    public DBMethod getSource()
    {
	return (DBMethod)source.getReferenced();
    }
    protected int lineNumber;
}
