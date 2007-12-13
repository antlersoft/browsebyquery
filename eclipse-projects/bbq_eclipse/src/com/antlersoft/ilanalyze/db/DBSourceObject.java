/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * Base class for persistent objects that can be found in source files
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBSourceObject implements Persistent {
	
	private transient PersistentImpl _persistentImpl;
	
	private transient boolean m_tainted;
	
	private ObjectRef m_source_file;
	private int m_line;
	
	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}
	
	/**
	 * Mark the file and line associated with this object
	 * as suspect and subject to revision when new information is available.
	 *
	 */
	void taintFileAndLine()
	{
		m_tainted=true;
	}
	
	/**
	 * Set the file and line for this object if they have not been set or if the information
	 * is marked as being tainted.
	 * @param file
	 * @param line
	 */
	void setFileAndLine( DBSourceFile file, int line)
	{
		if ( file!=null && ( m_tainted || m_source_file==null))
		{
			m_tainted=false;
			if ( m_source_file!=null)
			{
				if ( m_source_file.equals( file) && m_line==line)
				{
					return;
				}
				m_source_file.setReferenced( file);
			}
			else
				m_source_file=new ObjectRef( file);
			m_line=line;
			ObjectDB.makeDirty( this);
		}
	}
	
	protected void addPositionString( StringBuilder sb)
	{
		DBSourceFile file=getSourceFile();
		if ( file!=null)
		{
			sb.append( " at line ");
			sb.append(m_line);
			sb.append( ' ');
			sb.append( file.toString());
		}
	}
	
	public String getPositionString()
	{
		StringBuilder sb=new StringBuilder();
		addPositionString( sb);
		return sb.toString();
	}
	
	public DBSourceFile getSourceFile()
	{
		return (DBSourceFile)OptionalRefGet( m_source_file);
	}
	
	public int getLineNumber()
	{
		return m_line;
	}
	
	static interface IRefSetter
	{
		void set( Persistent container, ObjectRef ref);
	}
	
	static Object OptionalRefGet( ObjectRef ref)
	{
		Object result=null;
		if ( ref!=null)
			result=ref.getReferenced();
		return result;
	}
	
	static void OptionalRefSet( Persistent container, IRefSetter setter, ObjectRef ref, Persistent to_set)
	{
		if ( to_set!=null)
		{
			if ( ref==null)
			{
				ref=new ObjectRef( to_set);
				setter.set( container, ref);
			}
			else
			{
				if ( to_set.equals( ref.getReferenced()))
					return;
				ref.setReferenced(to_set);
			}
			ObjectDB.makeDirty(container);
		}
	}
}
