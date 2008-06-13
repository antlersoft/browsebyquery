/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.Vector;

import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * Base type for class members: fields (DBField) and methods (DBMethod)
 * @author Michael A. MacDonald
 *
 */
public abstract class DBMember implements Persistent, Cloneable, SourceObject,
		HasDBType, AccessFlags {

	/** Containing class */
    ObjectRef dbclass;
    /** Type of this member */
    ObjectRef type;
    String name;
    String signature;
    int accessFlags;

    private int lineNumber;
    private boolean deprecated;

    private transient PersistentImpl _persistentImpl;

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
		return (DBClass)dbclass.getReferenced();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.SourceObject#getLineNumber()
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.HasDBType#getDBType(com.antlersoft.analyzer.AnalyzerDB)
	 */
	public DBType getDBType(AnalyzerDB db) {
		return (DBType)type.getReferenced();
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

}
