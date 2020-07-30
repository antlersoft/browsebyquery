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

import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;

public class IncludeFile extends SourceFile {
	static final String INCLUDE_FILE_INDEX="IncludeFile.m_name";
	private IncludeFile(ObjectDB db, String f)
	{
		super(db, f);
	}
	static IncludeFile get( IndexObjectDB db, String f)
	{
		IncludeFile result=(IncludeFile)db.findObject( INCLUDE_FILE_INDEX,
			f);
		if ( result==null)
			result=new IncludeFile(db, f);
		return result;
	}
}