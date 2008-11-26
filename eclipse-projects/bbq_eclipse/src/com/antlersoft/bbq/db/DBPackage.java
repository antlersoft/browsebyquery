/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import java.util.Enumeration;

import com.antlersoft.odb.FromRefIteratorEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectStoreException;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A package or namespace in the analyzed system.  Contains classes, and possibly
 * sub-packages.
 * <p>
 * Note that there is currently no way to remove a package element after it has been created.
 * @param C Class representing a class in the analyzed system.
 * @author Michael A. MacDonald
 *
 */
public class DBPackage<C> implements Persistent, Cloneable {

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
	private ObjectKeyHashSet<C> _classes;
	/**
	 * Contains ObjectRef's of contained packages
	 */
	private ObjectKeyHashSet<DBPackage<C>> _subpackages;
	
	/**
	 * Link to package containing this package, if any
	 */ 
	private ObjectRef<DBPackage<C>> _containingPackage;
	
	private DBPackage( String key, IndexObjectDB db)
	throws ObjectStoreException
	{
		_name=key;
		_classes=new ObjectKeyHashSet<C>();
		_subpackages=new ObjectKeyHashSet<DBPackage<C>>();
		_persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
		if ( _name.length()>0)
		{
			DBPackage containing=get( namespacePart( key ), db);
			_containingPackage=new ObjectRef<DBPackage<C>>( containing);
			containing.setSubPackage( this);
			ObjectDB.makeDirty( this);
		}
	}
	
	public static DBPackage get( String package_name, IndexObjectDB db) throws ObjectStoreException
	{
		DBPackage result=(DBPackage)db.findObject(PACKAGE_NAME_INDEX, package_name);
		if ( result==null)
		{
			result=new DBPackage( package_name, db);
		}
		return result;		
	}
	
	public DBPackage<C> getContainingPackage()
	{
		if ( _containingPackage==null)
			return null;
		return _containingPackage.getReferenced();
	}
	
	public synchronized void setSubPackage( DBPackage<C> new_sub)
	{
		ObjectRef<DBPackage<C>> new_sub_ref=new ObjectRef<DBPackage<C>>( new_sub);
		if ( ! _subpackages.contains( new_sub_ref))
		{
			_subpackages.add( new_sub_ref);
			ObjectDB.makeDirty( this);
		}
	}
	
	public Enumeration<DBPackage<C>> getSubPackages()
	{
		return new FromRefIteratorEnumeration<DBPackage<C>>( _subpackages.iterator());
	}
	
	public synchronized void setContainedClass( C new_class)
	{
		ObjectRef<C> new_class_ref=new ObjectRef<C>( new_class);
		if ( ! _classes.contains( new_class_ref))
		{
			_classes.add( new_class_ref);
			ObjectDB.makeDirty( this);
		}
	}
	
	public Enumeration<C> getContainedClasses()
	{
		return new FromRefIteratorEnumeration<C>( _classes.iterator());
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

	static public String namespacePart( String packageName)
	{
		int last_period=packageName.lastIndexOf( '.');
		if ( last_period<=0)
			return new String();
		return packageName.substring( 0, last_period);
	}
	
	public static class PackageNameKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBPackage)o1)._name;
		}
	}
}
