package com.antlersoft.analyzecxx.db;

import com.antlersoft.odb.IndexObjectDB;

public class IncludeFile extends SourceFile {
	static final String INCLUDE_FILE_INDEX="IncludeFile.m_name";
	private IncludeFile( String f)
	{
		super(f);
	}
	static IncludeFile get( IndexObjectDB db, String f)
	{
		IncludeFile result=(IncludeFile)db.findObject( INCLUDE_FILE_INDEX,
			f);
		if ( result==null)
			result=new IncludeFile( f);
		return result;
	}
}