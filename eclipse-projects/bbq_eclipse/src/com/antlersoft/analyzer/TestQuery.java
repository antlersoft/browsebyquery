package com.antlersoft.analyzer;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;

import com.antlersoft.analyzer.query.QueryParser;
import com.antlersoft.analyzer.query.SetExpression;
import com.antlersoft.analyzer.query.ParseException;

public class TestQuery
{
    public static void main( String argv[])
	throws Exception
    {
	int i=0;
	SimpleAnalyzerDB sadb=new SimpleAnalyzerDB();
	BufferedReader in=new BufferedReader( new InputStreamReader( System.in));
	sadb.openDB( argv[0]);
	QueryParser qp=new QueryParser();
	try
	{
	    String line="1";
	    while ( line.length()>0)
	    {
		try
		{
		    System.out.print( ">");
		    line=in.readLine();
		    if ( line==null || line.length()==0)
			break;
		    qp.setLine( line);
		    SetExpression se=qp.getExpression();
		    Enumeration e=se.execute( sadb);
		    while ( e.hasMoreElements())
		    {
			System.out.println( e.nextElement().toString());
		    }
		}
		catch ( ParseException pe)
		{
		    System.out.println( pe.getMessage());
		}
	    }
	}
	finally
	{
	    sadb.closeDB();
	}
    }
}
