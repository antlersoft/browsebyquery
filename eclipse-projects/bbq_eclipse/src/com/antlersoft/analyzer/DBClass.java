package analyzer;

import java.util.Vector;
import java.util.Hashtable;
import java.io.Serializable;
import java.util.Enumeration;

class DBClass implements Serializable, Cloneable
{
    String name;
    Vector superClasses;
    Vector methods;
    Hashtable derivedClasses;
    private boolean resolved;

    public DBClass( String key, AnalyzerDB db)
    {
	name=key;
	superClasses=new Vector();
	derivedClasses=new Hashtable();
	methods=new Vector();
	resolved=false;
    }

    public String toString()
    {
	return name;
    }

    public boolean isResolved()
    {
	return resolved;
    }

    public Enumeration getMethods()
    {
	return methods.elements();
    }

    public String getName()
    {
	return name;
    }

    private void clearMethods()
    {
	methods.removeAllElements();
    }

    public Enumeration getSuperClasses()
    {
	return superClasses.elements();
    }

    public Enumeration getDerivedClasses()
    {
	return derivedClasses.elements();
    }

    private void addSuperClass( DBClass toAdd)
    {
	superClasses.addElement( toAdd);
	toAdd.derivedClasses.put( this, this);
    }

    private void clearSuperClasses()
    {
	superClasses.removeAllElements();
    }

    public static void addClassToDB( AnalyzeClass ac, AnalyzerDB db)
	throws Exception
    {
	DBClass dbc=(DBClass)db.getWithKey( "analyzer.DBClass",
	    ac.getClassName( ac.thisClassIndex));
	dbc.clearMethods();
	dbc.clearSuperClasses();
	dbc.resolved=true;
	int superClassIndex=ac.superClassIndex;
	if ( superClassIndex!=0)
	{
	    dbc.addSuperClass( (DBClass)db.getWithKey( "analyzer.DBClass", ac.getClassName( superClassIndex)));
	}
	int i;
	for ( i=0; i<ac.interfaces.length; i++)
	{
	    dbc.addSuperClass( (DBClass)db.getWithKey( "analyzer.DBClass", ac.getClassName(ac.interfaces[i])));
	}
	for ( i=0; i<ac.methods.length; i++)
	{
	    AnalyzeClass.FieldInfo mi=ac.methods[i];
	    DBMethod method=(DBMethod)db.getWithKey( "analyzer.DBMethod",
		DBMethod.makeKey( dbc.name,
		ac.getString( mi.nameIndex),
		ac.getString( mi.descriptorIndex)
		));
	    method.setFromAnalyzeClass( ac, i, db);
	    dbc.methods.addElement( method);
	}
    }
}
