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