package com.antlersoft.analyzecxx.db;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class SourceFile implements Persistent {
	private transient PersistentImpl _persistentImpl;
	private String m_file_name;

	SourceFile( String file_name)
	{
		m_file_name=file_name;
		ObjectDB.makePersistent( this);
	}

	public PersistentImpl _getPersistentImpl()
	{
		if ( _persistentImpl==null)
				_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}

	public String getName()
	{
		return m_file_name;
	}

	public String toString()
	{
		return getName();
	}

	static class FileKeyGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object o)
		{
			return ((SourceFile)o).m_file_name;
		}
	}
}