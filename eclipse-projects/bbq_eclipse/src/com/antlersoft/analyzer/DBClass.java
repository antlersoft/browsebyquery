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


import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import com.antlersoft.bbq.db.*;

import com.antlersoft.bbq.db.AnnotationCollection;
import com.antlersoft.bbq.db.DBAnnotatable;
import com.antlersoft.bbq.db.DBClassBase;
import com.antlersoft.bbq.db.DBPackage;
import com.antlersoft.classwriter.*;

import com.antlersoft.odb.FromRefIteratorEnumeration;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.query.EmptyEnum;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBClass implements DBClassBase, Cloneable, SourceObject, AccessFlags, HasDBType, DBAnnotatable
{

	/**
	 * Key on class's user-visible name
	 * @author Michael A. MacDonald
	 */
	static class NameKeyGenerator implements KeyGenerator {

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBClass)o1).name;
		}

	}

	/**
	 * Key on class's internal name
	 * @author Michael A. MacDonald
	 */
	static class InternalNameKeyGenerator implements KeyGenerator {

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBClass)o1).internalName;
		}

	}
	
	private static String PROPERTIES_SUFFIX=".properties";
	
	String name;
    ObjectKeyHashSet<DBClass> superClasses;
    private TreeMap<String,ObjectRef<DBMethod>> methods;
    private TreeMap<String,ObjectRef<DBField>> fields;
    ObjectKeyHashSet<DBClass> derivedClasses;
    private boolean resolved;
    private String internalName;
    private String sourceFile;
    int accessFlags;
    boolean deprecated;
    int lineNumber;
    ObjectRef<DBClass> containingClass;
    ObjectKeyHashSet<DBClass> containedClasses;
    ObjectRef<DBPackage> myPackage;
    private AnnotationCollection annotations;
    
    public final static String CLASS_NAME_INDEX="CLASS_NAME_INDEX";
    
    public final static String CLASS_INTERNAL_NAME_INDEX="CLASS_INTERNAL_NAME_INDEX";
    
	private transient PersistentImpl _persistentImpl;

    private DBClass( String key, IndexAnalyzeDB db)
    throws Exception
    {
		internalName=key;
		superClasses=new ObjectKeyHashSet<DBClass>();
		derivedClasses=new ObjectKeyHashSet<DBClass>();
		containedClasses=null;
		containingClass=null;
		methods=new TreeMap<String,ObjectRef<DBMethod>>();
		fields=new TreeMap<String,ObjectRef<DBField>>();
		resolved=false;
		deprecated=false;
		_persistentImpl=new PersistentImpl();
		annotations=new AnnotationCollection();
		lineNumber= -1;
		name=TypeParse.convertFromInternalClassName( internalName);
		db.getSession().makePersistent( this);
		if ( internalName.charAt(0)!='[')
		{
			DBPackage my_package=DBPackage.get( TypeParse.packageFromInternalClassName( internalName), db.getSession());
			my_package.setContainedClass( db.getSession(), this);
			myPackage=new ObjectRef<DBPackage>( my_package);
			String containing_name=containingClassName( internalName);
			if ( containing_name!=null)
			{
				DBClass containing=getByInternalName( containing_name, db);
				containingClass=new ObjectRef<DBClass>( containing);
				containing.setContainedClass( db.getSession(), this);
				db.getSession().makeDirty( this);
			}
		}
    }
    
    public static DBClass getByInternalName( String internal, IndexAnalyzeDB db) throws Exception
    {
    	DBClass result=(DBClass)db.findWithIndex(CLASS_INTERNAL_NAME_INDEX, internal);
    	
    	if ( result==null)
    	{
    		result=new DBClass(internal, db);
    		db.incrementCreateCount();
    	}
    	
    	return result;
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

    public Enumeration<DBMethod> getMethods()
    {
		return new FromRefIteratorEnumeration<DBMethod>( methods.values().iterator());
    }

    public Enumeration<DBField> getFields()
    {
		return new FromRefIteratorEnumeration<DBField>( fields.values().iterator());
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
    
    public DBType getDBType( IndexAnalyzeDB db)
    {
		return DBType.getFromClass( db, this); 
    }
    
    /* (non-Javadoc)
	 * @see com.antlersoft.bbq.db.DBAnnotatable#getAnnotationCollection()
	 */
	public AnnotationCollection getAnnotationCollection() {
		return annotations;
	}

	static class ClearUnfound<E>
    {
    	void clearUnfound( IndexAnalyzeDB db, String indexName, TreeMap<String,ObjectRef<E>> tree, HashSet<E> set)
    	{
    		for ( Iterator<Map.Entry<String,ObjectRef<E>>> i=tree.entrySet().iterator(); i.hasNext();)
    		{
    			Map.Entry<String,ObjectRef<E>> entry=i.next();
    			if ( ! set.contains(entry.getValue().getReferenced()))
    			{
    				if ( ! db.retrieveByIndex(indexName, new ObjectRefKey(entry.getValue())).hasMoreElements())
    					i.remove();
    			}
    		}
    	}
    }

    private void addMethod(ObjectDB db, DBMethod toAdd)
    {
        String key=toAdd.getName()+toAdd.getSignature();
        if ( methods.get( key)==null)
        {
            methods.put( key, new ObjectRef<DBMethod>( toAdd));
            db.makeDirty( this);
        }
    }

    private void addField(ObjectDB db, DBField toAdd)
    {
        String key=toAdd.getName();
        if ( fields.get( key)==null)
        {
            fields.put( key, new ObjectRef<DBField>( toAdd));
            db.makeDirty( this);
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

    public Enumeration<DBClass> getSuperClasses()
    {
		return new FromRefIteratorEnumeration<DBClass>( superClasses.iterator());
    }

    public Enumeration<DBClass> getDerivedClasses()
    {
		return new FromRefIteratorEnumeration<DBClass>( derivedClasses.iterator());
    }
    
    public boolean isInnerClass()
    {
    	return containingClass!=null;
    }
    
    public DBClass getContainingClass()
    {
    	DBClass result=null;
    	if ( containingClass!=null)
    		result=containingClass.getReferenced();
    	
    	return result;
    }
    
    public Enumeration<DBClass> getInnerClasses()
    {
    	Enumeration<DBClass> result=EmptyEnum.empty;
    	if ( containedClasses!=null)
    		result=new FromRefIteratorEnumeration<DBClass>( containedClasses.iterator());
    	
    	return result;
    }
    
    /**
     * Enumeration of
     * DBCatch object representing places in the analyzed system where this class is caught
     * @param db Analyzed system db
     * @return
     */
    public Enumeration getCatchesOf( IndexAnalyzeDB db)
    {
    	return db.retrieveByIndex( DBCatch.CATCH_TARGET, new ObjectRefKey(this));
    }
    
    public DBPackage getPackage()
    {
    	if ( myPackage==null)
    		return null;
    	return myPackage.getReferenced();
    }
    
    synchronized DBField getField( IndexAnalyzeDB db, String name, String signature )
    throws Exception
    {
    	DBType t=DBType.getWithTypeKey(signature, db);
    	
    	ObjectRef<DBField> f=fields.get(name);
    	
    	DBField result;

    	ObjectDB session = db.getSession();
    	
    	if ( f==null)
    	{
    		result=new DBField( session,this, name, t);
    		addField(session, result);
    	}
    	else
    	{
    		result=f.getReferenced();
    		result.setDBType(session, t);
    	}
    	
    	return result;
    }
    
    /**
     * Returns a DBMethod in this class with the given name and signature if it exists;
     * otherwise returns null.
     * @param name
     * @param signature
     * @return
     */
    public DBMethod getMethod( String name, String signature)
    {
    	ObjectRef<DBMethod> m=methods.get(name + signature);
    	if ( m!=null)
    		return m.getReferenced();
    	return null;
    }
    
    /**
     * Returns a DBMethod in this class with the given name and signature.
     * If no such method exists, one is created.
     * @param db database of the analyzed system
     * @param name Method name
     * @param signature Internal method signature
     * @return Existing or newly created method
     * @throws Exception
     */
    synchronized DBMethod getOrCreateMethod( IndexAnalyzeDB db, String name, String signature)
    throws Exception
    {
    	DBMethod result=getMethod( name, signature);
    	if ( result==null)
    	{
    		result=new DBMethod(db,this,name,signature);
    		addMethod( db.getSession(), result);
    	}
    	
    	return result;
    }
    
    private synchronized void setContainedClass(ObjectDB db, DBClass toAdd)
    {
    	if ( containedClasses==null)
    	{
    		containedClasses=new ObjectKeyHashSet<DBClass>();
    	}
    	if ( ! containedClasses.contains( toAdd))
    	{
    		containedClasses.add( new ObjectRef<DBClass>( toAdd));
    		db.makeDirty( this);
    	}
    }

    private void addSuperClass(ObjectDB db, DBClass toAdd)
    {
		superClasses.add( new ObjectRef<DBClass>( toAdd));
		toAdd.derivedClasses.add( new ObjectRef<DBClass>( this));
		db.makeDirty( this);
		db.makeDirty( toAdd);
    }

    private void clearSuperClasses(ObjectDB db)
    {
		superClasses.clear();
		db.makeDirty( this);
    }
    
    /**
     * Add a stream representing a .properties file to the database as a resource bundle
     * (DBBundle)
     * @param input
     * @param db
     */
    public static void addPropertiesToDB( String name, InputStream input, IndexAnalyzeDB db)
    {
    	try
    	{
	    	Properties p=new Properties();
	    	p.load(input);
	    	DBBundle bundle=DBBundle.get( db.getSession(), name);
	    	DBBundleBase.BundleUpdater updater = new DBBundle.BundleUpdater(bundle);
	    	for ( Map.Entry<Object,Object> entry : p.entrySet())
	    	{
	    		updater.addNameValuePair(db.getSession(), entry.getKey().toString(), entry.getValue().toString());
	    	}
	    	updater.finishBundle(db.getSession());
    	}
    	catch ( IOException ioe)
    	{
    		
    	}
    	catch ( IllegalArgumentException iae)
    	{
    		
    	}
    	finally
    	{
    		try
    		{
    			input.close();
    		}
    		catch ( IOException ioe)
    		{}
    	}
    }
    
    private static void processArgumentAnnotations( ClassWriter ac, IndexAnalyzeDB db, DBMethod method, AnnotationInitializerHolder holder, AttributeList attr)
    throws Exception
    {
    	RuntimeVisibleParameterAnnotationsAttribute vis=(RuntimeVisibleParameterAnnotationsAttribute)attr.getAttributeByType(RuntimeVisibleParameterAnnotationsAttribute.typeString);
    	RuntimeInvisibleParameterAnnotationsAttribute invis=(RuntimeInvisibleParameterAnnotationsAttribute)attr.getAttributeByType(RuntimeInvisibleParameterAnnotationsAttribute.typeString);
    	
    	if ( vis!=null || invis!=null)
    	{
    		RuntimeVisibleParameterAnnotationsAttribute.ParameterAnnotation[] visArray=(vis==null) ? null : vis.getParameterAnnotations();
    		RuntimeVisibleParameterAnnotationsAttribute.ParameterAnnotation[] invisArray=(invis==null) ? null : invis.getParameterAnnotations();
    		int argCount=method.arguments.size();
    		for ( int i=0; i<argCount; i++)
    		{
    			if ( ( visArray!=null && i<visArray.length) || (invisArray!=null && i<invisArray.length))
    			{
    				AnnotationProcessor<DBArgument> processor=new AnnotationProcessor<DBArgument>(ac, db, method.arguments.get(i).getReferenced(), holder);
    				if ( visArray!=null && i<visArray.length)
    					processor.addAnnotations(visArray[i], false);
    				if ( invisArray!=null && i<invisArray.length)
    					processor.addAnnotations(invisArray[i], true);
    			}
    		}
    	}
    }

    public static void addClassToDB( ClassWriter ac, IndexAnalyzeDB db)
		throws Exception
    {
		DBClass dbc=getByInternalName( ac.getInternalClassName(ac.getCurrentClassIndex()), db);
		dbc.clearSuperClasses(db.getSession());
		dbc.resolved=true;
        dbc.accessFlags=ac.getFlags();
        dbc.deprecated=ac.isDeprecated();
        dbc.sourceFile=ac.getSourceFile();
        dbc.lineNumber= -1;
        HashSet<DBField> foundFields=new HashSet<DBField>();
        HashSet<DBMethod> foundMethods=new HashSet<DBMethod>();
		int superClassIndex=ac.getSuperClassIndex();
		if ( superClassIndex!=0)
		{
		    dbc.addSuperClass( db.getSession(), getByInternalName( ac.getInternalClassName(superClassIndex), db));
		}
		for ( Iterator<Integer> i=ac.getInterfaces().iterator(); i.hasNext();)
		{
		    dbc.addSuperClass( db.getSession(), getByInternalName( ac.getInternalClassName(i.next()), db));
		}
		AnnotationInitializerHolder initializerHolder=new AnnotationInitializerHolder(db,dbc,foundMethods);
		new AnnotationProcessor<DBClass>(ac,db,dbc,initializerHolder).processAttributeList(ac.getAttributeList());
		for ( Iterator<FieldInfo> i=ac.getFields().iterator(); i.hasNext();)
		{
            FieldInfo fi=(FieldInfo)i.next();
            DBField new_field=dbc.getField( db, fi.getName(), fi.getType());
            foundFields.add( new_field);
            new_field.accessFlags=fi.getFlags();
            new_field.setDeprecated( fi.isDeprecated());
            new AnnotationProcessor<DBField>(ac,db,new_field,initializerHolder).processAttributeList(fi.getAttributeList());
		}
		for ( Iterator<MethodInfo> i=ac.getMethods().iterator(); i.hasNext();)
		{
		    MethodInfo mi=(MethodInfo)i.next();
		    DBMethod method=dbc.getOrCreateMethod(db, mi.getName(), mi.getType());
		    foundMethods.add( method);
		    method.setFromClassWriter( ac, mi, db);
		    new AnnotationProcessor<DBMethod>(ac,db,method,initializerHolder).processAttributeList(mi.getAttributeList());
		    processArgumentAnnotations(ac,db,method,initializerHolder,mi.getAttributeList());
		}
		initializerHolder.finish();
		new ClearUnfound<DBField>().clearUnfound( db, DBFieldReference.FRTARGET, dbc.fields, foundFields);
		new ClearUnfound<DBMethod>().clearUnfound( db, DBCall.CALL_TARGET, dbc.methods, foundMethods);
    }
    
    /**
     * Determine if a file name is a valid resource bundle we want to include in
     * the analyzed system.
     * @param name File name
     * @return true if it is a bundle we want to include
     */
    private static boolean isValidPropertiesName( String name)
    {
    	if ( name.endsWith( PROPERTIES_SUFFIX) && name.indexOf('_')== -1)
    	{
    		return true;
    	}
    	return false;
    }

    public static void addFileToDB( File file, IndexAnalyzeDB db)
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
		    db.commit();
		    return;
		}
		// Check to see if it is a Zip file
		try
		{
		    ZipFile zip=new ZipFile( file);

		    // If it is a Zip file, process each member in turn
		    for ( Enumeration<? extends ZipEntry> e=zip.entries(); e.hasMoreElements(); )
		    {
				ZipEntry entry=e.nextElement();
				String entryName=entry.getName();
				if ( entryName.endsWith( ".class"))
				{
			    	InputStream is=zip.getInputStream( entry);
				    try
				    {
						cl.readClass( is);
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
				    finally
				    {
				    	is.close();
				    }
				}
				else if ( isValidPropertiesName(entryName))
				{
					addPropertiesToDB(entryName, zip.getInputStream(entry), db);
				}
		    }

		    zip.close();
		    db.commit();
		    return;
		}
		// Not a zip file -- see if it is a class file
		catch ( ZipException zfe)
		{
		}
		String fileName=file.getName();
		if ( fileName.endsWith( ".class"))
		{
	    	InputStream is=new BufferedInputStream( new FileInputStream( file));
		    try
		    {
				cl.readClass( is);
				addClassToDB( cl, db);
		    }
		    catch ( CodeCheckException bie)
		    {
				System.out.println( bie.getMessage());
				System.out.println( "In "+file.getCanonicalPath());
		    }
		    finally
		    {
		    	is.close();
		    }
		}
		else if ( isValidPropertiesName(fileName))
		{
			InputStream is=new BufferedInputStream( new FileInputStream(file));
			addPropertiesToDB(file.getCanonicalPath(), is, db);
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
