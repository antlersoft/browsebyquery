/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;
import com.antlersoft.odb.KeyGenerator;

/**
 * An assembly that has been analyzed
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBModule implements Persistent {
	
	/** Primary key on module name */
	public static final String MODULE_NAME_INDEX="MODULE_NAME_INDEX";
	private transient PersistentImpl _persistentImpl;
	
	String m_module_name;
	/** DBAssembly-valued */
	ObjectRef m_assembly_ref;
	
	private DBModule( String name)
	{
		m_module_name=name;
		m_assembly_ref=new ObjectRef();
		ObjectDB.makePersistent( this);
	}
	
	DBAssembly getAssembly()
	{
		return (DBAssembly)m_assembly_ref.getReferenced();
	}
	
	void setAssembly( DBAssembly assembly)
	{
		m_assembly_ref.setReferenced( assembly);
		ObjectDB.makeDirty(this);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}

	
	static synchronized DBModule get( IndexObjectDB db, String f)
	{
		DBModule result=(DBModule)db.findObject( MODULE_NAME_INDEX,
			f);
		if ( result==null)
			result=new DBModule( f);
		return result;
	}
	
	public String toString()
	{
		DBAssembly assembly=getAssembly();
		if ( assembly==null)
			return m_module_name;
		StringBuilder sb=new StringBuilder();
		sb.append('[');
		sb.append( assembly.toString());
		sb.append(']');
		sb.append(m_module_name);
		return sb.toString();
	}
	
	static class NameKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBModule)o1).m_module_name;
		}
		
	}
}

