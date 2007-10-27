/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.Enumeration;

import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.util.IteratorEnumeration;

/**
 * A namespace that contains types or methods in the analyzed system
 * @author Michael A. MacDonald
 *
 */
public class DBNamespace implements Persistent {
	
	/** Primary key on namespace name (full dotted name) */
	static final String NAMESPACE_NAME_INDEX="NAMESPACE_NAME_INDEX";

	private transient PersistentImpl _persistentImpl;
	
	private String m_name;
	
	/** May be null for the top-level namespace */
	private ObjectRef m_parent;
	
	/** Set of object references to child namespaces */
	private ObjectKeyHashSet m_children;
	
	/** Set of object references to contained classes */
	private ObjectKeyHashSet m_classes;
	
	private DBNamespace( IndexObjectDB db, String name)
	{
		m_name=name;
		if ( name.length()==0)
		{
			m_parent=new ObjectRef();
		}
		else
		{
			DBNamespace parent=get( db, namespacePart( name));
			parent.addChild( this);
			m_parent=new ObjectRef( parent);
		}
		m_children=new ObjectKeyHashSet();
		m_classes=new ObjectKeyHashSet();
		ObjectDB.makePersistent( this);
	}
	
	public DBNamespace getParent()
	{
		return (DBNamespace)m_parent.getReferenced();
	}
	
	/** Enumeration of namespaces contained within this namespace */
	public Enumeration getChildren()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_children.iterator()));
	}
	
	/** Enumeration of classes contained within this namespace */
	public Enumeration getClasses()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_children.iterator()));
	}
	
	void addChild( DBNamespace child)
	{
		if ( m_children.add( new ObjectRef( child)))
		{
			ObjectDB.makeDirty( this);
		}
	}
	
	void addClass( DBClass added)
	{
		if ( m_classes.add( new ObjectRef( added)))
			ObjectDB.makeDirty( this);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}
	
	public String toString()
	{
		return m_name;
	}
	
	public static DBNamespace get( IndexObjectDB db, String name)
	{
		DBNamespace result=(DBNamespace)db.findObject(NAMESPACE_NAME_INDEX, name);
		if ( result==null)
		{
			result=new DBNamespace( db, name);
		}
		return result;
	}
	
	/**
	 * Extract the part of the input string before the final period (i.e. the namespace part).  If
	 * the string doesn't have a period, returns the empty string.
	 * @param name
	 * @return Namespace part of the string
	 */
	static String namespacePart( String name)
	{
		int index=name.lastIndexOf('.');
		if ( index== -1)
			return "";
		return name.substring(0,index);
	}

}
