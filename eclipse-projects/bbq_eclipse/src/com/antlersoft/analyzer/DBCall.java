package analyzer;

class DBCall implements java.io.Serializable, Cloneable
{
    DBCall( DBMethod s, DBMethod t, int l)
    {
	source=s;
	target=t;
	lineNumber=l;
    }

    DBMethod target;
    DBMethod source;
    int lineNumber;

    public String toString()
    {
	return "Call to "+target.toString()+" from "+source.toString()+" at line "+String.valueOf( lineNumber);
    }
}
