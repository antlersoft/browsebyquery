package analyzer;

public abstract class DBReference implements java.io.Serializable, Cloneable
{
    DBReference( DBMethod s, int l)
    {
	source=s;
	lineNumber=l;
    }

    protected DBMethod source;
    protected int lineNumber;
}
