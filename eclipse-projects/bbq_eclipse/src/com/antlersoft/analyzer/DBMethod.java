package analyzer;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

class DBMethod implements Serializable, Cloneable
{
    public static final int UNRESOLVED=1;
    public static final int VIRTUAL=2;
    public static final int REAL=3;

    DBClass dbclass;
    String name;
    String signature;
    Vector calls;
    Vector calledBy;

    private boolean resolved;
    static Vector emptyVector=new Vector();

    public DBMethod( String key, AnalyzerDB db)
	throws Exception
    {
	StringTokenizer st=new StringTokenizer( key, "\t");
	dbclass=(DBClass)db.getWithKey( "analyzer.DBClass", (String)st.nextElement());
	name=(String)st.nextElement();
	signature=(String)st.nextElement();
	resolved=false;
    }

    public String toString()
    {
	return dbclass.name+":"+name+signature;
    }

    public int methodStatus()
    {
	if ( ! dbclass.isResolved())
	    return UNRESOLVED;
	else
	    return resolved ? REAL : VIRTUAL;
    }

    static String makeKey( String className, String methodName, String descriptor)
    {
	StringBuffer sb=new StringBuffer();
	sb.append( className);
	sb.append( "\t");
	sb.append( methodName);
	sb.append( "\t");
	sb.append( descriptor);
	return sb.toString();
    }

    public String getName()
    {
	return name;
    }

    public String getSignature()
    {
	return signature;
    }

    public DBClass getDBClass()
    {
	return dbclass;
    }

    public Enumeration getCalls()
    {
	if ( calls!=null)
	    return calls.elements();
	return emptyVector.elements();
    }

    public Enumeration getCalledBy()
    {
	if ( calledBy!=null)
	    return calledBy.elements();
	return emptyVector.elements();
    }

    public void addCalledBy( DBMethod caller, Vector callsFromCaller)
    {
	if ( calledBy==null)
	    calledBy=callsFromCaller;
	else
	    /* Remove calls from same method and append calls to list */
	{
	    int i;
	    for ( i=0; i<calledBy.size(); i++)
	    {
		if ( ((DBCall)calledBy.elementAt( i)).source==caller)
		{
		    calledBy.removeElementAt( i);
		    i--;
		}
	    }
	    for ( i=0; i<callsFromCaller.size(); i++)
	    {
		calledBy.addElement( callsFromCaller.elementAt( i));
	    }
	}
    }

    public void setFromAnalyzeClass( AnalyzeClass ac, int methodIndex, AnalyzerDB db)
    {
	calls=null;
	AnalyzeClass.FieldInfo mi=ac.methods[methodIndex];
	for ( int i=0; i<mi.attributesCount; i++)
	{
	    if ( ac.getString(mi.attributes[i].nameIndex).equals( "Code"))
	    {
		if ( calls==null)
		    calls=new Vector();
		else
		    calls.removeAllElements();
		new CodeReader( this, ac, (AnalyzeClass.CodeAttribute)mi.attributes[i].value, db).processOpCodes();
		Hashtable calledTable=new Hashtable();
		Enumeration e;
		for ( e=calls.elements(); e.hasMoreElements(); )
		{
		    DBCall call=(DBCall)e.nextElement();
		    Vector calledByMethod=(Vector)calledTable.get( call.target);
		    if ( calledByMethod==null)
		    {
			calledByMethod=new Vector();
			calledTable.put( call.target, calledByMethod);
		    }
		    calledByMethod.addElement( call);
		}
		for ( e=calledTable.keys(); e.hasMoreElements(); )
		{
		    DBMethod called=(DBMethod)e.nextElement();
		    called.addCalledBy( this, (Vector)calledTable.get( called));
		}
		break;
	    }
	}
    }
}
