package analyzer;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import odb.ObjectRef;
import odb.ObjectDB;
import odb.Persistent;
import odb.PersistentImpl;

public class DBField implements Persistent, Cloneable
{
    ObjectRef dbclass;
    String name;
    String descriptor;
    Vector referencedBy;

    private transient PersistentImpl _persistentImpl;

    public DBField( String key, AnalyzerDB db)
	throws Exception
    {
	StringTokenizer st=new StringTokenizer( key, "\t");
	dbclass=new ObjectRef( (DBClass)db.getWithKey( "analyzer.DBClass", (String)st.nextElement()));
	name=(String)st.nextElement();
	descriptor=new String();
	_persistentImpl=new PersistentImpl();
	ObjectDB.setPersistent( this);
    }

    public PersistentImpl _getPersistentImpl()
    {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
	    return _persistentImpl;
    }

    public String toString()
    {
	return ((DBClass)dbclass.getReferenced()).name+":"+name+descriptor;
    }

    public int fieldStatus()
    {
	if ( ! ((DBClass)dbclass.getReferenced()).isResolved())
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
	ObjectDB.makeDirty( this);
    }

    public DBClass getDBClass()
    {
	return (DBClass)dbclass.getReferenced();;
    }

    public Enumeration getReferencedBy()
    {
	if ( referencedBy!=null)
	    return referencedBy.elements();
	return DBMethod.emptyVector.elements();
    }

    public void addReferencedBy( DBMethod caller, Vector callsFromCaller)
    {
	ObjectDB.makeDirty( this);
	if ( referencedBy==null)
	    referencedBy=callsFromCaller;
	else
	    /* Remove calls from same method and append calls to list */
	{
	    int i;
	    for ( i=0; i<referencedBy.size(); i++)
	    {
		if ( ((DBFieldReference)referencedBy.elementAt( i)).getSource()==caller)
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
