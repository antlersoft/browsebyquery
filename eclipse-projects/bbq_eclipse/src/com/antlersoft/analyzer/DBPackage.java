/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.Enumeration;

import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.util.IteratorEnumeration;

/**
 * A package in the database.
 * Note that there is currently no way to remove a package element after it has been created.
 * @author Michael A. MacDonald
 *
 */
public class DBPackage implements Persistent, Cloneable {

	/** Primary key on package name (full dotted name) */
	public static final String PACKAGE_NAME_INDEX="PACKAGE_NAME_INDEX";

	private transient PersistentImpl _persistentImpl;
	
	/**
	 * Name of the package; visible name (with .'s instead of /'s)
	 */
	private String _name;
	
	/**
	 * Contains ObjectRef's of contained classes
	 */
	private ObjectKeyHashSet _classes;
	/**
	 * Contains ObjectRef's of contained packages
	 */
	private ObjectKeyHashSet _subpackages;
	
	/**
	 * Link to package containing this package, if any
	 */ 
	private ObjectRef _containingPackage;
	
	private DBPackage( String key, IndexAnalyzeDB db)
	throws Exception
	{
		_name=key;
		_classes=new ObjectKeyHashSet();
		_subpackages=new ObjectKeyHashSet();
		_persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
		if ( _name.length()>0)
		{
			DBPackage containing=get( superPackage( key ), db);
			_containingPackage=new ObjectRef( containing);
			containing.setSubPackage( this);
			ObjectDB.makeDirty( this);
		}
	}
	
	public static DBPackage get( String package_name, IndexAnalyzeDB db) throws Exception
	{
		DBPackage result=(DBPackage)db.findWithIndex(PACKAGE_NAME_INDEX, package_name);
		if ( result==null)
		{
			result=new DBPackage( package_name, db);
		}
		return result;
		
	}
	
	public DBPackage getContainingPackage()
	{
		if ( _containingPackage==null)
			return null;
		return (DBPackage)_containingPackage.getReferenced();
	}
	
	public synchronized void setSubPackage( DBPackage new_sub)
	{
		ObjectRef new_sub_ref=new ObjectRef( new_sub);
		if ( ! _subpackages.contains( new_sub_ref))
		{
			_subpackages.add( new_sub_ref);
			ObjectDB.makeDirty( this);
		}
	}
	
	public Enumeration getSubPackages()
	{
		return new FromRefEnumeration( new IteratorEnumeration( _subpackages.iterator()));
	}
	
	public synchronized void setContainedClass( DBClass new_class)
	{
		ObjectRef new_class_ref=new ObjectRef( new_class);
		if ( ! _classes.contains( new_class_ref))
		{
			_classes.add( new_class_ref);
			ObjectDB.makeDirty( this);
		}
	}
	
	public Enumeration getContainedClasses()
	{
		return new FromRefEnumeration( new IteratorEnumeration( _classes.iterator()));
	}
	
	public String toString()
	{
		return _name;
	}
	
	// Implementation of Persistent interface
	
	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		
		return _persistentImpl;
	}

	static String superPackage( String packageName)
	{
		int last_period=packageName.lastIndexOf( '.');
		if ( last_period<=0)
			return new String();
		return packageName.substring( 0, last_period);
	}
}
