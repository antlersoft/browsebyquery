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

import java.util.Date;

import com.antlersoft.odb.IndexObjectDB;

public class TranslationUnit extends SourceFile {
	static final String TRANSLATION_UNIT_NAME_INDEX="TranslationUnit.m_file_name";

	private Date m_translation_date;

	private TranslationUnit( String name)
	{
		super( name);
	}

	public Date getTranslationDate()
	{
		return m_translation_date;
	}

	public void translate()
	{
		m_translation_date=new Date();
		IndexObjectDB.makeDirty( this);
	}

	static TranslationUnit get( String file_name, IndexObjectDB db)
	{
		TranslationUnit result=(TranslationUnit)db.findObject( TRANSLATION_UNIT_NAME_INDEX, file_name);
		if ( result==null)
			result=new TranslationUnit( file_name);
		return result;
	}
}