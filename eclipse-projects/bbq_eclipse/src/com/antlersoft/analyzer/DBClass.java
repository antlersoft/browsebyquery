package com.antlersoft.analyzer;

import java.util.Vector;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBClass implements Persistent, Cloneable
{
    String name;
    Vector superClasses;
    Vector methods;
    Vector fields;
    Vector derivedClasses;
    private boolean resolved;

	private transient PersistentImpl _persistentImpl;

    public DBClass( String key, AnalyzerDB db)
    {
		name=key;
		superClasses=new Vector();
		derivedClasses=new Vector();
		methods=new Vector();
		fields=new Vector();
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
		return name;
    }

    public boolean isResolved()
    {
	return resolved;
    }

    public Enumeration getMethods()
    {
	return new FromRefEnumeration( methods.elements());
    }

    public Enumeration getFields()
    {
	return new FromRefEnumeration( fields.elements());
    }

    public String getName()
    {
	return name;
    }

    private void clearMethods()
    {
	methods.removeAllElements();
	ObjectDB.makeDirty( this);
    }

    private void clearFields()
    {
	fields.removeAllElements();
	ObjectDB.makeDirty( this);
    }

    public Enumeration getSuperClasses()
    {
	return new FromRefEnumeration( superClasses.elements());
    }

    public Enumeration getDerivedClasses()
    {
	return new FromRefEnumeration( derivedClasses.elements());
    }

    private void addSuperClass( DBClass toAdd)
    {
	superClasses.addElement( new ObjectRef( toAdd));
	toAdd.derivedClasses.addElement( new ObjectRef( this));
	ObjectDB.makeDirty( this);
	ObjectDB.makeDirty( toAdd);
    }

    private void clearSuperClasses()
    {
	superClasses.removeAllElements();
	ObjectDB.makeDirty( this);
    }

    public static void addClassToDB( AnalyzeClass ac, AnalyzerDB db)
	throws Exception
    {
	DBClass dbc=(DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass",
	    ac.getClassName( ac.thisClassIndex));
	dbc.clearMethods();
	dbc.clearSuperClasses();
	dbc.clearFields();
	dbc.resolved=true;
	int superClassIndex=ac.superClassIndex;
	if ( superClassIndex!=0)
	{
	    dbc.addSuperClass( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", ac.getClassName( superClassIndex)));
	}
	int i;
	for ( i=0; i<ac.interfaces.length; i++)
	{
	    dbc.addSuperClass( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", ac.getClassName(ac.interfaces[i])));
	}
	for ( i=0; i<ac.fields.length; i++)
	{
	    dbc.fields.addElement( new ObjectRef( (DBField)db.getWithKey( "com.antlersoft.analyzer.DBField",
		DBField.makeKey( dbc.name, ac.getString( ((AnalyzeClass.FieldInfo)ac.fields[i]).nameIndex)))));
	}
	for ( i=0; i<ac.methods.length; i++)
	{
	    AnalyzeClass.FieldInfo mi=ac.methods[i];
	    DBMethod method=(DBMethod)db.getWithKey( "com.antlersoft.analyzer.DBMethod",
		DBMethod.makeKey( dbc.name,
		ac.getString( mi.nameIndex),
		ac.getString( mi.descriptorIndex)
		));
	    method.setFromAnalyzeClass( ac, i, db);
	    dbc.methods.addElement( new ObjectRef( method));
	}
    }

    public static void addFileToDB( File file, AnalyzerDB db)
	throws Exception
    {
	if ( file.isDirectory())
	{
	    File[] listFiles;
	    try
	    {
		listFiles=file.listFiles();
		for ( int i=0; i<listFiles.length; i++)
		{
		    addFileToDB( listFiles[i], db);
		}
	    }
	    catch ( SecurityException se)
	    {
	    }
	    return;	    
	}
	// Check to see if it is a Zip file
	try
	{
	    ZipFile zip=new ZipFile( file);

	    // If it is a Zip file, process each member in turn
	    for ( Enumeration e=zip.entries(); e.hasMoreElements(); )
	    {
		ZipEntry entry=(ZipEntry)e.nextElement();
		String entryName=entry.getName();
		int length=entryName.length();
		if ( length>6 && entryName.substring( length-6).equals( ".class"))
		{
		    try
		    {
			addClassToDB( new AnalyzeClass( zip.getInputStream( entry)), db);
		    }
		    // If it is not a class file, don't fret it
		    catch ( IllegalStateException ise)
		    {
		    }
		    catch ( IOException ioe)
		    {
		    }
		    catch ( CodeReader.BadInstructionException bie)
		    {
			System.out.println( bie.getMessage());
			System.out.println( "In "+file.getCanonicalPath()+"/"+entryName);
		    }
		}
	    }

	    zip.close();
	    return;
	}
	// Not a zip file -- see if it is a class file
	catch ( ZipException zfe)
	{
	}
	String fileName=file.getName();
	int fileLength=fileName.length();
	if ( fileLength>6 && fileName.substring( fileLength-6).equals( ".class"))
	{
	    try
	    {
		addClassToDB(
		    new AnalyzeClass( new BufferedInputStream( new FileInputStream( file))), db);
	    }
	    catch ( CodeReader.BadInstructionException bie)
	    {
		System.out.println( bie.getMessage());
		System.out.println( "In "+file.getCanonicalPath());   
	    }
	}
    }
}
