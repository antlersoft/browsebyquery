package analyzer;

import odb.ObjectRef;

class DBCall extends DBReference
{
    DBCall( DBMethod s, DBMethod t, int l)
    {
	super( s, l);	
	target=new ObjectRef( t);
    }

    ObjectRef target;

	public DBMethod getTarget()
	{
		return (DBMethod)target.getReferenced();
	}

    public String toString()
    {
	return "Call to "+getTarget().toString()+" from "+getSource().toString()+" at line "+String.valueOf( lineNumber);
    }
}
