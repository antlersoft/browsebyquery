package com.antlersoft.analyzer;

import java.io.File;
import java.io.IOException;

public class TestAnalyze
{
    public static void main( String argv[])
	throws Exception
    {
	IndexAnalyzeDB sadb=new IndexAnalyzeDB();
	sadb.openDB( argv[0]);
	try
	{
		DBClass.addFileToDB( new File( argv[1]), sadb);
	}
	finally
	{
	    sadb.closeDB();
	}
    }
}
