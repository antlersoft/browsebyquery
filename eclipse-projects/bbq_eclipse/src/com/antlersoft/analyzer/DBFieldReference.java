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

	public int hashCode()
	{
		return getSource().hashCode()^lineNumber^target.hashCode()+( writeReference ? 0 : 63);
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof DBFieldReference)
		{
			DBFieldReference f=(DBFieldReference)toCompare;

			return f.getSource().equals( getSource()) && f.lineNumber==lineNumber &&
				( ( writeReference && f.writeReference) || 
				! ( writeReference || f.writeReference))
				&& f.getTarget().equals( getTarget());
		}
		return false;
	}
}
