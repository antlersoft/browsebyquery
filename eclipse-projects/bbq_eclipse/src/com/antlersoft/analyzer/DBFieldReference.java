package analyzer;

import odb.ObjectRef;

public class DBFieldReference extends DBReference
{
    DBFieldReference( DBMethod s, DBField t, int l, boolean write)
    {
		super( s, l);
		target=new ObjectRef( t);
		writeReference=write;
    }

    ObjectRef target;
    boolean writeReference;

    public boolean isWrite() { return writeReference; }

	public DBField getTarget()
	{
		return (DBField)target.getReferenced();
	}

    public String toString()
    {
		return ( writeReference ? "Write" : "Read")+" reference to "+getTarget().toString()+" from "+getSource().toString()+" at line "+String.valueOf( lineNumber);
    }
}
