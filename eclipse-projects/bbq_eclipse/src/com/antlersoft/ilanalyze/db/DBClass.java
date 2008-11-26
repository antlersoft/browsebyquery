/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;

import com.antlersoft.bbq.db.DBPackage;

import com.antlersoft.ilanalyze.DBDriver;

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;

import com.antlersoft.util.IteratorEnumeration;

import com.antlersoft.query.EmptyEnum;

/**
 * A class that has been visited or referenced while analyzing the system.
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBClass extends DBSourceObject implements HasProperties, HasDBType {
	
	/** Primary key on class name in key form */
	public static final String CLASS_KEY_INDEX="CLASS_KEY_INDEX";
	/** Primary key on class name in key form */
	public static final String CLASS_BY_NAME_INDEX="CLASS_BY_NAME_INDEX";

	/**
	 * Base classes
	 */
	private ObjectKeyHashSet<DBClass> m_base;
	
	/**
	 * Derived classes
	 */
	private ObjectKeyHashSet<DBClass> m_derived;
	
	/**
	 * The containing DBNamespace
	 */
	private ObjectRef<DBPackage<DBClass>> m_namespace;
	
	/**
	 * The containing DBClass (null if not contained)
	 */
	private ObjectRef<DBClass> m_containing;
	
	/**
	 * Contained classes (null until inner classes found)
	 */
	private ObjectKeyHashSet<DBClass> m_contained;
	
	/**
	 * ObjectRefs of DBFields in name order
	 */
	private TreeMap<String,ObjectRef<DBField>> m_fields;
	
	/**
	 * ObjectRefs of DBMethods arranged by name+sig key
	 */
	private TreeMap<String,ObjectRef<DBMethod>> m_methods;
	
	/**
	 * The class key name; this is the namespace name (dotted) followed by the class name, which may contain
	 * slashes for a nested class.
	 * This is distinct from the user-readable name.
	 */
	String m_class_key;
	
	/**
	 * True if this class has actually been analyzed, rather than just being referenced
	 */
	private boolean m_visited;
	
	/**
	 * DBAssembly where we find this class
	 */
	ObjectRef<DBAssembly> m_assembly;
	
	/**
	 * Module where we find this class
	 */
	ObjectRef<DBModule> m_module;
	
	/**
	 * Visibility and other flags
	 */
	private int m_properties;
	
	private DBClass( IndexObjectDB db, String class_key)
	{
		m_class_key=class_key;
		DBPackage<DBClass> namespace= DBPackage.get( DBPackage.namespacePart(m_class_key), db);
		m_namespace=new ObjectRef<DBPackage<DBClass>>(namespace);
		m_base=new ObjectKeyHashSet<DBClass>();
		m_fields=new TreeMap<String,ObjectRef<DBField>>();
		m_methods=new TreeMap<String,ObjectRef<DBMethod>>();
		
		ObjectDB.makePersistent( this);
		namespace.setContainedClass( this);
		// Assume class is public unless we get concrete evidence otherwise
		m_properties=DBDriver.IS_PUBLIC;
		// Find containing class, if any
		int slash_pos=class_key.lastIndexOf('/');
		if ( slash_pos!= -1)
		{
			String containing_key=class_key.substring( 0, slash_pos);
			DBClass containing=DBClass.get(db, containing_key);
			m_containing=new ObjectRef<DBClass>( containing);
			containing.addContained( this);
		}
	}

	/**
	 * The namespace that contains this class (or the namespace-level class containing this class)
	 * @return DBNamespacethat contains the class
	 */
	public DBPackage<DBClass> getNamespace()
	{
		return m_namespace.getReferenced();
	}
	
	/**
	 * 
	 * @return true if the class has actually been analyzed, rather than just being referenced
	 */
	public boolean isVisited()
	{
		return m_visited;
	}
	
	void setVisited( boolean visited)
	{
		if ( visited!=m_visited)
		{
			m_visited=visited;
			ObjectDB.makeDirty(this);
		}
	}
	
	public int getProperties()
	{
		return m_properties;
	}
	
	void setProperties( int properties)
	{
		if ( m_properties!=properties)
		{
			m_properties=properties;
			ObjectDB.makeDirty(this);
		}
	}
	
	public DBAssembly getAssembly()
	{
		return (DBAssembly)OptionalRefGet( m_assembly);
	}
	
	private static IRefSetter m_AssemblySetter=new IRefSetter() {
		public void set( Persistent container, ObjectRef ref)
		{
			((DBClass)container).m_assembly=ref;
		}
	};
	void setAssembly( DBAssembly assembly)
	{
		OptionalRefSet(this, m_AssemblySetter, m_assembly, assembly);
	}
	
	public DBModule getModule()
	{
		return (DBModule)OptionalRefGet( m_module);
	}
	
	private static IRefSetter m_ModuleSetter=new IRefSetter() {
		public void set( Persistent container, ObjectRef ref)
		{
			((DBClass)container).m_module=ref;
		}
	};
	void setModule( DBModule module)
	{
		OptionalRefSet( this, m_ModuleSetter, m_module, module);
	}
	
	/**
	 * If this is an inner class, return the class that contains it
	 * @return Containing class, or null if this is not an inner class
	 */
	public DBClass getContainingClass()
	{
		return (DBClass)OptionalRefGet( m_containing);
	}
	
	/**
	 * Return the classes this class contains
	 * @return Enumeration over the collection of contained classes
	 */
	public Enumeration getContainedClasses()
	{
		if ( m_contained==null)
			return EmptyEnum.empty;
		return new FromRefEnumeration( new IteratorEnumeration( m_contained.iterator()));
	}
	
	public Enumeration getFields()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_fields.values().iterator()));
	}
	
	/**
	 * Add or get a field in the class
	 * @param name
	 * @param type
	 * @return Field with that name
	 */
	DBField getField( String name, DBType type)
	{
		DBField result;
		ObjectRef ref=(ObjectRef)m_fields.get( name);
		if ( ref==null)
		{
			result=new DBField( this, name, type);
			m_fields.put(name, new ObjectRef( result));
			ObjectDB.makeDirty(this);
		}
		else
		{
			result=(DBField)ref.getReferenced();
			result.setDBType(type);
		}
		return result;
	}
	
	/**
	 * 
	 * @return Enumeration over all the methods (including static and constructor) in the class
	 */
	public Enumeration getMethods()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_methods.values().iterator()));
	}
	
	DBMethod getMethod( String name, DBType type, String signature_key)
	{
		DBMethod result;
		String method_key=name+signature_key;
		ObjectRef ref=(ObjectRef)m_methods.get( method_key);
		if ( ref==null)
		{
			result=new DBMethod( this, name, type, signature_key);
			m_methods.put( method_key, new ObjectRef(result));
			ObjectDB.makeDirty( this);
		}
		else
		{
			result=(DBMethod)ref.getReferenced();
			result.setDBType( type);
		}
		
		return result;
	}
	
	public DBMethod findMethod( String name, String signature_key)
	{
		DBMethod result=null;
		String method_key=name+signature_key;
		ObjectRef ref=(ObjectRef)m_methods.get( method_key);
		if ( ref!=null)
			result=(DBMethod)ref.getReferenced();
		
		return result;
	}
	
	/**
	 * Add a class to the set of classes this class directly contains
	 * @param to_add
	 */
	void addContained( DBClass to_add)
	{
		if ( m_contained==null)
			m_contained=new ObjectKeyHashSet();
		if ( m_contained.add( new ObjectRef(to_add)))
			ObjectDB.makeDirty(this);
	}
	
	
	/**
	 * 
	 * @return Enumeration over immediate base classes (extends and implements) for this class
	 */
	public Enumeration getBaseClasses()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_base.iterator()));
	}
	
	
	/**
	 * 
	 * @return Enumeration over the immediate derived classes (if any) of this class
	 */
	public Enumeration getDerivedClasses()
	{
		return m_derived==null ? (Enumeration)EmptyEnum.empty : (Enumeration)new FromRefEnumeration( new IteratorEnumeration( m_derived.iterator()));
	}
	
	/**
	 * catches that reference this class (explicitly)
	 * @param db
	 * @return Enumeration of DBCatch object that reference this DBClass
	 */
	public Enumeration getCatchesOf( ILDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBCatch.CATCH_TARGET, new ObjectRefKey(this)));		
	}
	
	/**
	 * Add a class to the set of classes derived from this class
	 * @param to_add
	 */
	void addDerived( DBClass to_add)
	{
		if ( m_derived==null)
			m_derived=new ObjectKeyHashSet();
		if ( m_derived.add( new ObjectRef(to_add)))
			ObjectDB.makeDirty(this);
	}
	
	/**
	 * Remove a class that is no longer derived from this one
	 * @param to_remove
	 */
	private void removeDerived( ObjectRef to_remove)
	{
		if ( m_derived!=null)
		{
			if ( m_derived.remove( to_remove))
				ObjectDB.makeDirty( this);
		}
	}
	
	/**
	 * Change the set of base classes (extends and implements) to the supplied set.
	 * Base classes that no longer apply are removed; removed base classes are removed
	 * from the derived set of the parent classes.
	 * @param base_classes
	 */
	void updateBaseClasses( ObjectKeyHashSet base_classes)
	{
		boolean changed=false;
		for ( Iterator i=base_classes.iterator(); i.hasNext();)
		{
			ObjectRef ref=(ObjectRef)i.next();
			if ( m_base.add( ref))
			{
				changed=true;
				((DBClass)ref.getReferenced()).addDerived(this);
			}
		}
		Collection c=m_base.retainMembers(base_classes);
		if ( c!=null)
		{
			ObjectRef to_remove=new ObjectRef( this);
			changed=true;
			for ( Iterator i=c.iterator(); i.hasNext();)
			{
				((DBClass)((ObjectRef)i.next()).getReferenced()).removeDerived( to_remove);
			}
		}
		if ( changed)
			ObjectDB.makeDirty(this);
	}
	
	/**
	 * Get a DBClass from its class key; if the class is not found, create a new DBClass having that key.
	 * @param db
	 * @param f
	 * @return
	 */
	static DBClass get( IndexObjectDB db, String f)
	{
		DBClass result=(DBClass)db.findObject( CLASS_KEY_INDEX,
			f);
		if ( result==null)
			result=new DBClass( db, f);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.db.HasDBType#getDBType()
	 */
	public DBType getDBType( ILDB db) {
		return (DBType)db.findObject(DBType.TYPE_KEY_INDEX, m_class_key);
	}

	public String toString()
	{
		return m_class_key.replace('/', '.');
	}
	
	static class ClassKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBClass)o1).m_class_key;
		}
	}
}
