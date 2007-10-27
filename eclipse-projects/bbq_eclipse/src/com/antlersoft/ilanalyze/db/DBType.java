/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.ilanalyze.*;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A type in the analyzed system.  Note that types are invariant.
 * @author Michael A. MacDonald
 *
 */
public class DBType implements Persistent {
	
	static final String TYPE_KEY_INDEX="TYPE_KEY_INDEX";
	
	/** String that uniquely specifies the type */
	private String m_type_key;
	
	/** DBClass that implements this type; null if this is not a class type */
	private ObjectRef m_class;
	
	/** DBType that this array references, or null if this in not an array type */
	private ObjectRef m_referenced;
	
	/** User visible representation of the type */
	private String m_type_user_name;

	private transient PersistentImpl _persistentImpl;

	/**
	 * Create a new type object with the given key
	 * @param key Type key
	 * @param type_class Class that implements this type (may be null if type is not a class type)
	 * @param array_referenced_type Type that this type is an array of; null if this is not an array type
	 * @param user_name User-visible name for the type 
	 */
	DBType( String key, DBClass type_class, DBType array_referenced_type, String user_name) {
		m_type_key=key;
		if ( type_class!=null)
			m_class=new ObjectRef( type_class);
		if ( array_referenced_type!=null)
			m_referenced=new ObjectRef(array_referenced_type);
		m_type_user_name=user_name;
		ObjectDB.makePersistent( this);
	}
	
	static DBType get( ILDB db, ReadType t) throws ITypeInterpreter.TIException
	{
		ITypeInterpreter ti=ITypeInterpreter.Factory.getInstance().getTI(t);
		String key=ti.getTypeKey(t);
		DBType result=(DBType)db.findObject(TYPE_KEY_INDEX, key);
		if ( result==null)
		{
			result=ti.createType(db, key, t);
		}
		return result;
	}
	
	public String toString()
	{
		return m_type_user_name;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}

	static class TypeKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBType)o1).m_type_key;
		}
	
	}
}
