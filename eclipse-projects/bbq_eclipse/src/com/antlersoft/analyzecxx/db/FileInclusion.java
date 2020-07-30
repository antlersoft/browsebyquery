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

import com.antlersoft.odb.CompoundKey;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;

public class FileInclusion extends SourceObject {
	private ObjectRef m_included_file;
	static final String UNIQUE_KEY="FileInclusion.uk";

	private FileInclusion(ObjectDB db, IncludeFile included, SourceFile from, int line)
	{
		super(from, line);
		m_included_file=new ObjectRef( included);
		db.makePersistent( this);
	}

	public IncludeFile getIncluded()
	{
		return (IncludeFile)m_included_file.getReferenced();
	}

	public static FileInclusion get( IndexObjectDB db, IncludeFile included,
									 SourceFile from, int line)
	{
		FileInclusion fi=(FileInclusion)db.findObject( UNIQUE_KEY,
			new CompoundKey( new ObjectRefKey( included),
							 new ObjectRefKey( from)));
		if ( fi==null)
			fi=new FileInclusion(db, included, from, line);
		else
		{
			if ( fi.m_line!=line)
			{
				fi.m_line = line;
				db.makeDirty(fi);
			}
		}
		return fi;
	}

	static class InclusionKeyGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object o)
		{
			FileInclusion fi=(FileInclusion)o;
			return new CompoundKey(
									new ObjectRefKey( fi.m_included_file),
									new ObjectRefKey( fi.m_file));
		}
	}
}