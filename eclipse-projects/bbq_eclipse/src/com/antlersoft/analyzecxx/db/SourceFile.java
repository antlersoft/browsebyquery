/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.analyzecxx.db;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class SourceFile implements Persistent {
	private transient PersistentImpl _persistentImpl;
	private String m_file_name;

	SourceFile( ObjectDB db, String file_name)
	{
		m_file_name=file_name;
		db.makePersistent( this);
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