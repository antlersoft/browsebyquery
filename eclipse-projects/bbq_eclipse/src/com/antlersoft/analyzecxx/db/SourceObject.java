package com.antlersoft.analyzecxx.db;

import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class SourceObject implements Persistent {
	private transient PersistentImpl _persistentImpl;

	public PersistentImpl _getPersistentImpl()
	{
		if ( _persistentImpl==null)
				_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}

	private ObjectRef m_file;
	private int m_line;

	SourceObject( SourceFile file, int line)
	{
		m_file=new ObjectRef( file);
		m_line=line;
	}

	public SourceFile getFile()
	{
		return (SourceFile)m_file.getReferenced();
	}

	public int getLine()
	{
		return m_line;
	}
}