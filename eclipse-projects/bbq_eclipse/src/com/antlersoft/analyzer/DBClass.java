/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.analyzer;

import java.util.Hashtable;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import com.antlersoft.classwriter.*;

import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.query.EmptyEnum;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBClass implements Persistent, Cloneable, SourceObject, AccessFlags, HasDBType
{

	/**
	 * @author Michael A. MacDonald
	 *
	 */
	static class NameKeyGenerator implements KeyGenerator {

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBClass)o1).name;
		}

	}

	String name;
    Vector superClasses;
    private Hashtable methods;
    private Hashtable fields;
    Vector derivedClasses;
    private boolean resolved;
    private String internalName;
    private String sourceFile;
    int accessFlags;
    boolean deprecated;
    int lineNumber;
    ObjectRef containingClass;
    Vector containedClasses;
    ObjectRef myPackage;
    
    public static String CLASS_NAME_INDEX="CLASS_NAME_INDEX";
    
	private transient PersistentImpl _persistentImpl;

    public DBClass( String key, AnalyzerDB db)
    throws Exception
    {
		internalName=key;
		superClasses=new Vector();
		derivedClasses=new Vector();
		containedClasses=null;
		containingClass=null;
		methods=new Hashtable();
		fields=new Hashtable();
		resolved=false;
		deprecated=false;
		_persistentImpl=new PersistentImpl();
		lineNumber= -1;
		name=TypeParse.convertFromInternalClassName( internalName);
		ObjectDB.makePersistent( this);
		if ( internalName.charAt(0)!='[')
		{
			DBPackage my_package=(DBPackage)db.getWithKey( "com.antlersoft.analyzer.DBPackage",
					TypeParse.packageFromInternalClassName( internalName));
			my_package.setContainedClass( this);
			myPackage=new ObjectRef( my_package);
			String containing_name=containingClassName( internalName);
			if ( containing_name!=null)
			{
				DBClass containing=(DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", containing_name);
				containingClass=new ObjectRef( containing);
				containing.setContainedClass( this);
				ObjectDB.makeDirty( this);
			}
		}
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
    	if ( lineNumber== -1)
    		return 0;
        return lineNumber;
    }
    
    public DBType getDBType( AnalyzerDB db)
    {
		return DBType.getFromClass( db, this); 
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

    public String getInternalName()
    {
        return internalName;
    }

    public String getSourceFile()
    {
        return sourceFile;
    }
    
    public boolean isDeprecated()
    {
    	return deprecated;
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
    
    public boolean isInnerClass()
    {
    	return containingClass!=null;
    }
    
    public DBClass getContainingClass()
    {
    	DBClass result=null;
    	if ( containingClass!=null)
    		result=(DBClass)containingClass.getReferenced();
    	
    	return result;
    }
    
    public Enumeration getInnerClasses()
    {
    	Enumeration result=EmptyEnum.empty;
    	if ( containedClasses!=null)
    		result=new FromRefEnumeration( containedClasses.elements());
    	
    	return result;
    }
    
    public DBPackage getPackage()
    {
    	if ( myPackage==null)
    		return null;
    	return (DBPackage)myPackage.getReferenced();
    }
    
    private synchronized void setContainedClass( DBClass toAdd)
    {
    	if ( containedClasses==null)
    	{
    		containedClasses=new Vector();
    	}
    	if ( ! containedClasses.contains( toAdd))
    	{
    		containedClasses.add( new ObjectRef( toAdd));
    		ObjectDB.makeDirty( this);
    	}
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
		    ac.getInternalClassName( ac.getCurrentClassIndex()));
		dbc.clearMethods();
		dbc.clearSuperClasses();
		dbc.clearFields();
		dbc.resolved=true;
        dbc.accessFlags=ac.getFlags();
        dbc.deprecated=ac.isDeprecated();
        dbc.sourceFile=ac.getSourceFile();
        dbc.lineNumber= -1;
		int superClassIndex=ac.getSuperClassIndex();
		if ( superClassIndex!=0)
		{
		    dbc.addSuperClass( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", ac.getInternalClassName( superClassIndex)));
		}
		Iterator i;
		for ( i=ac.getInterfaces().iterator(); i.hasNext();)
		{
		    dbc.addSuperClass( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", ac.getInternalClassName(((Integer)i.next()).intValue())));
		}
		for ( i=ac.getFields().iterator(); i.hasNext();)
		{
            FieldInfo fi=(FieldInfo)i.next();
            DBField new_field=(DBField)db.getWithKey( "com.antlersoft.analyzer.DBField",
				DBField.makeKey( dbc.internalName, fi.getName()));
            new_field.accessFlags=fi.getFlags();
            new_field.setDeprecated( fi.isDeprecated());
            new_field.setTypeFromDescriptor( db, fi.getType());
            dbc.addField( new_field);
		}
		for ( i=ac.getMethods().iterator(); i.hasNext();)
		{
		    MethodInfo mi=(MethodInfo)i.next();
		    DBMethod method=(DBMethod)db.getWithKey( "com.antlersoft.analyzer.DBMethod",
				DBMethod.makeKey( dbc.internalName,
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
    
    /**
     * Returns the internal name of the class that contains a given internal name if any.
     * @param internalName Internal name of a class
     * @return internal name of the class that contains this class, or null if this class
     * is not contained within another class
     */
    private static String containingClassName( String internalName)
    {
    	String result=null;
    	int last_dollar=internalName.lastIndexOf( '$');
    	if ( last_dollar!= -1)
    	{
    		result=internalName.substring( 0, last_dollar);
    	}
    	return result;
    }
}
