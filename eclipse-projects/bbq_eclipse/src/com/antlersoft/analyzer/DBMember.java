/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import com.antlersoft.bbq.db.AnnotationCollection;
import com.antlersoft.bbq.db.DBAnnotatable;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
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
		HasDBType, AccessFlags, DBAnnotatable {
	
	/** Containing class */
    private ObjectRef<DBClass> dbclass;
    /** Type of this member */
    private ObjectRef<DBType> dbtype;
    String name;
    int accessFlags;
    
    private AnnotationCollection annotationCollection;

    private boolean deprecated;

    private transient PersistentImpl _persistentImpl;

    DBMember( String name, DBClass containingClass, DBType type)
    {
    	this.name=name;
    	this.dbclass=new ObjectRef<DBClass>( containingClass);
    	this.dbtype = new ObjectRef<DBType>( type);
    	annotationCollection=new AnnotationCollection();
    }
    
    /* (non-Javadoc)
	 * @see com.antlersoft.bbq.db.DBAnnotatable#getAnnotationCollection()
	 */
	public AnnotationCollection getAnnotationCollection() {
		return annotationCollection;
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

    public String getName()
    {
    	return name;
    }
 
    /**
     * Returns the type associated with this member; the type of the field or the
     * return type of the method.
     * 
     * For this and derived classes, the db param is not used and may be passed as null.
     * 
     * @param db Not used; may be null
     * @return Type of field or return type of method
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
    
    void setDBType( DBType new_type)
    {
    	if ( ! new_type.equals( getDBType( null)))
    	{
    		dbtype.setReferenced(new_type);
    		ObjectDB.makeDirty(this);
    	}
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
