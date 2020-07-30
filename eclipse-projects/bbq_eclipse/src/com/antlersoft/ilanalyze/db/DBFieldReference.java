/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;

/**
 * A reference from an analyzed method to a field in the analyzed system
 * @author Michael A. MacDonald
 *
 */
public class DBFieldReference extends DBReference {

	static final String FRTARGET="FRTARGET";
	private boolean m_write;
	
	/**
	 * @param source
	 * @param target
	 */
	public DBFieldReference(ObjectDB db, DBMethod source, DBField target, boolean isWrite) {
		super(source, target);
		m_write=isWrite;
		db.makePersistent(this);
	}

	public boolean isWrite()
	{
		return m_write;
	}
	
	public DBField getDBField()
	{
		return (DBField)m_target.getReferenced();
	}
	
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append( isWrite() ? "Write" : "Read");
		sb.append( " reference to ");
		sb.append( getDBField().toString());
		addPositionString( sb);
		return sb.toString();
	}

	synchronized void setWrite( ObjectDB db, boolean isWrite)
	{
		if ( isWrite!=m_write)
		{
			m_write=isWrite;
			db.makeDirty(this);
		}
	}
}
