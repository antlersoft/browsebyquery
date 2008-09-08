/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * Base type for class members: fields (DBField) and methods (DBMethod)
 * @author Michael A. MacDonald
 *
 */
public abstract class DBMember implements Persistent, Cloneable, SourceObject,
		HasDBType, AccessFlags {
	
	String MEMBER_TYPE_KEY="MEMBER_TYPE_KEY";

	/** Containing class */
    private ObjectRef<DBClass> dbclass;
    /** Type of this member */
    private ObjectRef<DBType> dbtype;
    String name;
    int accessFlags;

    private boolean deprecated;

    private transient PersistentImpl _persistentImpl;

    DBMember( String name, DBClass containingClass, DBType type)
    {
    	this.name=name;
    	this.dbclass=new ObjectRef<DBClass>( containingClass);
    	this.dbtype = new ObjectRef<DBType>( type);
    }
    
    /* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}

	/**
	 * The class that contains this member.
	 */
	public DBClass getDBClass() {
		return dbclass.getReferenced();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.HasDBType#getDBType(com.antlersoft.analyzer.AnalyzerDB)
	 */
	public DBType getDBType(IndexAnalyzeDB db) {
		return dbtype.getReferenced();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.AccessFlags#getAccessFlags()
	 */
	public int getAccessFlags() {
		return accessFlags;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.AccessFlags#isDeprecated()
	 */
	public boolean isDeprecated() {
		return deprecated;
	}
    void setDeprecated( boolean dep)
    {
    	deprecated=dep;
    }
	static class MemberTypeKeyGenerator implements KeyGenerator
	{

		public Comparable generateKey( Object obj)
		{
			DBMember field=(DBMember)obj;
			return new ObjectRefKey( field.dbtype);
		}
	}
}
