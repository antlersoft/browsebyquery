package analyzer;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

public class TestAnalyze
{
    public static void main( String argv[])
	throws Exception
    {
	int i=0;
	SimpleAnalyzerDB sadb=new SimpleAnalyzerDB();
	sadb.openDB( argv[0]);
	BufferedReader in=new BufferedReader( new FileReader( argv[1]));
	String line="1";
	try
	{
	    while ( line.length()>0)
	    {
		line=in.readLine();
		if ( line==null)
		    break;
		BufferedInputStream bis=new BufferedInputStream( new FileInputStream( line));
		DBClass.addClassToDB( new AnalyzeClass( bis), sadb);
		bis.close();
	    }
	}
	finally
	{
	    sadb.closeDB();
	}
    }
}
