package analyzer;

public class DBFieldReference extends DBReference
{
    DBFieldReference( DBMethod s, DBField t, int l, boolean write)
    {
		super( s, l);
		target=t;
		writeReference=write;
    }

    DBField target;
    boolean writeReference;

    public boolean isWrite() { return writeReference; }

    public String toString()
    {
		return ( writeReference ? "Write" : "Read")+" reference to "+target.toString()+" from "+source.toString()+" at line "+String.valueOf( lineNumber);
    }
}
