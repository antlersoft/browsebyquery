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

public class DBMethod implements Persistent, Cloneable
{
    public static final int UNRESOLVED=1;
    public static final int VIRTUAL=2;
    public static final int REAL=3;

    ObjectRef dbclass;
    String name;
    String signature;
    Vector calls;
    Vector calledBy;
    Vector fieldReferences;

    private boolean resolved;
    static public Vector emptyVector=new Vector();

    private transient PersistentImpl _persistentImpl;

    public DBMethod( String key, AnalyzerDB db)
	throws Exception
    {
	StringTokenizer st=new StringTokenizer( key, "\t");
	dbclass=new ObjectRef( (DBClass)db.getWithKey( "analyzer.DBClass", (String)st.nextElement()));
	name=(String)st.nextElement();
	signature=(String)st.nextElement();
	resolved=false;
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
	return ((DBClass)dbclass.getReferenced()).name+":"+name+signature;
    }

    public int methodStatus()
    {
	if ( ! ((DBClass)dbclass.getReferenced()).isResolved())
	    return UNRESOLVED;
	else
	    return resolved ? REAL : VIRTUAL;
    }

    public static String makeKey( String className, String methodName, String descriptor)
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
	return (DBClass)dbclass.getReferenced();
    }

    public Enumeration getCalls()
    {
	if ( calls!=null)
	    return calls.elements();
	return emptyVector.elements();
    }

    public Enumeration getReferences()
    {
	if ( fieldReferences!=null)
	{
	    return fieldReferences.elements();
	}
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
	ObjectDB.makeDirty( this);
	if ( calledBy==null)
	    calledBy=callsFromCaller;
	else
	    /* Remove calls from same method and append calls to list */
	{
	    int i;
	    for ( i=0; i<calledBy.size(); i++)
	    {
		if ( ((DBCall)calledBy.elementAt( i)).getSource()==caller)
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

    public void setFromAnalyzeClass( final AnalyzeClass ac, int methodIndex,
	final AnalyzerDB db)
	throws CodeReader.BadInstructionException
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
		if ( fieldReferences==null)
		    fieldReferences=new Vector();
		else
		    fieldReferences.removeAllElements();
		CodeReader cr=new CodeReader( (AnalyzeClass.CodeAttribute)mi.attributes[i].value);
		cr.setMethodInvokeCallback( new CodeReader.ReferenceCallback() // CodeReader.MethodInvokeCallback contract
		{
		    public void processReference( CodeReader.OpCode read, int position,
			int cpOffset, int lineNumber)
		    {
			try
			{
			    AnalyzeClass.CPTypeRef methodRef=(AnalyzeClass.CPTypeRef)ac.constantPool[cpOffset];
			    AnalyzeClass.CPNameAndType nameAndType=(AnalyzeClass.CPNameAndType)ac.constantPool[methodRef.nameAndTypeIndex];
			    calls.addElement( new DBCall(
				DBMethod.this,
				(DBMethod)db.getWithKey( "analyzer.DBMethod",
				DBMethod.makeKey(
				ac.getClassName( methodRef.classIndex),
				ac.getString( nameAndType.nameIndex),
				ac.getString( nameAndType.descriptorIndex)
				)), lineNumber));
			}
			catch ( Exception e)
			{
			    e.printStackTrace();
			}	
		    }
		} );
		cr.setFieldReferenceCallback( new CodeReader.ReferenceCallback() // CodeReader.MethodInvokeCallback contract
		{
		    public void processReference( CodeReader.OpCode read, int position,
			int cpOffset, int lineNumber)
		    {
			try
			{
			    AnalyzeClass.CPTypeRef methodRef=(AnalyzeClass.CPTypeRef)ac.constantPool[cpOffset];
			    AnalyzeClass.CPNameAndType nameAndType=(AnalyzeClass.CPNameAndType)ac.constantPool[methodRef.nameAndTypeIndex];
			    fieldReferences.addElement( new DBFieldReference(
				DBMethod.this,
				(DBField)db.getWithKey( "analyzer.DBField",
				DBField.makeKey(
				ac.getClassName( methodRef.classIndex),
				ac.getString( nameAndType.nameIndex)
				)), lineNumber, read.mnemonic.substring( 0, 3).equals( "put")));
			}
			catch ( Exception e)
			{
			    e.printStackTrace();
			}	
		    }
		} );
		cr.processOpCodes();
		Hashtable calledTable=new Hashtable();
		Enumeration e;
		for ( e=calls.elements(); e.hasMoreElements(); )
		{
		    DBCall call=(DBCall)e.nextElement();
		    Vector calledByMethod=(Vector)calledTable.get( call.getTarget());
		    if ( calledByMethod==null)
		    {
			calledByMethod=new Vector();
			calledTable.put( call.getTarget(), calledByMethod);
		    }
		    calledByMethod.addElement( call);
		}
		for ( e=calledTable.keys(); e.hasMoreElements(); )
		{
		    DBMethod called=(DBMethod)e.nextElement();
		    called.addCalledBy( this, (Vector)calledTable.get( called));
		}
		calledTable.clear();
		for ( e=fieldReferences.elements(); e.hasMoreElements(); )
		{
		    DBFieldReference reference=(DBFieldReference)e.nextElement();
		    Vector calledByMethod=(Vector)calledTable.get( reference.getTarget());
		    if ( calledByMethod==null)
		    {
			calledByMethod=new Vector();
			calledTable.put( reference.getTarget(), calledByMethod);
		    }
		    calledByMethod.addElement( reference);
		}
		for ( e=calledTable.keys(); e.hasMoreElements(); )
		{
		    DBField called=(DBField)e.nextElement();
		    called.addReferencedBy( this, (Vector)calledTable.get( called));
		}
		break;
	    }
	}
    }

}
