package analyzer;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class DBField implements Serializable, Cloneable
{
    DBClass dbclass;
    String name;
    String descriptor;
    Vector referencedBy;

    public DBField( String key, AnalyzerDB db)
	throws Exception
    {
	StringTokenizer st=new StringTokenizer( key, "\t");
	dbclass=(DBClass)db.getWithKey( "analyzer.DBClass", (String)st.nextElement());
	name=(String)st.nextElement();
	descriptor=new String();
    }

    public String toString()
    {
	return dbclass.name+":"+name+descriptor;
    }

    public int fieldStatus()
    {
	if ( ! dbclass.isResolved())
	    return DBMethod.UNRESOLVED;
	else
	    return DBMethod.REAL;
    }

    static String makeKey( String className, String fieldName)
    {
	StringBuffer sb=new StringBuffer();
	sb.append( className);
	sb.append( "\t");
	sb.append( fieldName);
	return sb.toString();
    }

    public String getName()
    {
	return name;
    }

    public String getDescriptor()
    {
	return descriptor;
    }

    public void setDescriptor( String s)
    {
	descriptor=s;
    }

    public DBClass getDBClass()
    {
	return dbclass;
    }

    public Enumeration getReferencedBy()
    {
	if ( referencedBy!=null)
	    return referencedBy.elements();
	return DBMethod.emptyVector.elements();
    }

    public void addReferencedBy( DBMethod caller, Vector callsFromCaller)
    {
	if ( referencedBy==null)
	    referencedBy=callsFromCaller;
	else
	    /* Remove calls from same method and append calls to list */
	{
	    int i;
	    for ( i=0; i<referencedBy.size(); i++)
	    {
		if ( ((DBFieldReference)referencedBy.elementAt( i)).source==caller)
		{
		    referencedBy.removeElementAt( i);
		    i--;
		}
	    }
	    for ( i=0; i<callsFromCaller.size(); i++)
	    {
		referencedBy.addElement( callsFromCaller.elementAt( i));
	    }
	}
    }
}
