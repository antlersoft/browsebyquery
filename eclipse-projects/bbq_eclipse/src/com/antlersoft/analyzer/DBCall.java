package analyzer;

class DBCall extends DBReference
{
    DBCall( DBMethod s, DBMethod t, int l)
    {
	super( s, l);	
	target=t;
    }

    DBMethod target;

    public String toString()
    {
	return "Call to "+target.toString()+" from "+source.toString()+" at line "+String.valueOf( lineNumber);
    }
}
