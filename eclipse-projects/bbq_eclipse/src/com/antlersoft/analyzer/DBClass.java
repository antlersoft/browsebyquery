package com.antlersoft.analyzer;

import java.util.Hashtable;
import java.util.Vector;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.classwriter.*;

public class DBClass implements Persistent, Cloneable, SourceObject, AccessFlags
{
    String name;
    Vector superClasses;
    private Hashtable methods;
    private Hashtable fields;
    Vector derivedClasses;
    private boolean resolved;
    int accessFlags;

		private transient PersistentImpl _persistentImpl;

    public DBClass( String key, AnalyzerDB db)
    {
				name=key;
				superClasses=new Vector();
				derivedClasses=new Vector();
				methods=new Hashtable();
				fields=new Hashtable();
				resolved=false;
				_persistentImpl=new PersistentImpl();
				ObjectDB.makePersistent( this);
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

    public int getAccessFlags()
    {
       return accessFlags;
    }

   public Enumeration getMethods()
    {
		return new FromRefEnumeration( methods.elements());
    }

    public Enumeration getFields()
    {
		return new FromRefEnumeration( fields.elements());
    }

    public DBClass getDBClass()
    {
        return this;
    }

    public int getLineNumber()
    {
        return 1;
    }

    void addMethod( DBMethod toAdd)
    {
        String key=toAdd.getName()+toAdd.getSignature();
        if ( methods.get( key)==null)
        {
            methods.put( key, new ObjectRef( toAdd));
            ObjectDB.makeDirty( this);
        }
    }

    void addField( DBField toAdd)
    {
        String key=toAdd.getName();
        if ( fields.get( key)==null)
        {
            fields.put( key, new ObjectRef( toAdd));
            ObjectDB.makeDirty( this);
        }
    }

    public String getName()
    {
		return name;
    }

    private void clearMethods()
    {
		methods.clear();
		ObjectDB.makeDirty( this);
    }

    private void clearFields()
    {
		fields.clear();
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

    public static void addClassToDB( ClassWriter ac, AnalyzerDB db)
		throws Exception
    {
		DBClass dbc=(DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass",
		    ac.getClassName( ac.getCurrentClassIndex()));
		dbc.clearMethods();
		dbc.clearSuperClasses();
		dbc.clearFields();
		dbc.resolved=true;
        dbc.accessFlags=ac.getFlags();
		int superClassIndex=ac.getSuperClassIndex();
		if ( superClassIndex!=0)
		{
		    dbc.addSuperClass( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", ac.getClassName( superClassIndex)));
		}
		Iterator i;
		for ( i=ac.getInterfaces().iterator(); i.hasNext();)
		{
		    dbc.addSuperClass( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", ac.getClassName(((Integer)i.next()).intValue())));
		}
		for ( i=ac.getFields().iterator(); i.hasNext();)
		{
            FieldInfo fi=(FieldInfo)i.next();
            DBField new_field=(DBField)db.getWithKey( "com.antlersoft.analyzer.DBField",
				DBField.makeKey( dbc.name, fi.getName()));
            new_field.accessFlags=fi.getFlags();
            dbc.addField( new_field);
		}
		for ( i=ac.getMethods().iterator(); i.hasNext();)
		{
		    MethodInfo mi=(MethodInfo)i.next();
		    DBMethod method=(DBMethod)db.getWithKey( "com.antlersoft.analyzer.DBMethod",
				DBMethod.makeKey( dbc.name,
				mi.getName(),
				mi.getType()
				));
			dbc.addMethod( method);
		    method.setFromClassWriter( ac, mi, db);
		}
    }

    public static void addFileToDB( File file, AnalyzerDB db)
		throws Exception
    {
    	ClassWriter cl=new ClassWriter();
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
						cl.readClass( zip.getInputStream( entry));
						addClassToDB( cl, db);
				    }
				    // If it is not a class file, don't fret it
				    catch ( IllegalStateException ise)
				    {
				    }
				    catch ( IOException ioe)
				    {
				    }
				    catch ( CodeCheckException bie)
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
				cl.readClass( new BufferedInputStream( new FileInputStream( file)));
				addClassToDB( cl, db);
		    }
		    catch ( CodeCheckException bie)
		    {
				System.out.println( bie.getMessage());
				System.out.println( "In "+file.getCanonicalPath());
		    }
		}
    }
}
