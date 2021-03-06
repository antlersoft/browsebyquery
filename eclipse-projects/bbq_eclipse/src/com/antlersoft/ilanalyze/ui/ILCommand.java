/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

import com.antlersoft.appcontext.AppContext;
import com.antlersoft.ilanalyze.query.ILQueryParser;
import com.antlersoft.query.SetExpression;
import com.antlersoft.query.environment.ParseException;
import com.antlersoft.query.environment.AnalyzerQuery;

/**
 * main() function implements a command-line interface to the Browse-by-Query functionality.
 * Prints a > on a line by itself as a prompt.  Enter a query on a single line.  Results are returned
 * one per line. 
 * @author Michael A. MacDonald
 *
 */
public class ILCommand {

	/**
	 * @param args Command-line arguments; database directory, other values in AppContext
	 */
	public static void main(String[] args) throws Exception {
    	ILDBContainer c=new ILDBContainer();
    	AppContext ac = ILDBContainer.createContextWithLegacyCommandLine(args);
    	
    	c.initializeFromContext( ac,
    			"test.bbq", "com.antlersoft.browsebyquery.il");
    	
    	BufferedReader in=new BufferedReader( new InputStreamReader( System.in));
    	ILQueryParser parser = new ILQueryParser();
    	AnalyzerQuery env = new AnalyzerQuery(parser);
    	
		try
		{
		    String line="1";
		    while ( line.length()>0)
		    {
				try
				{
				    System.out.println( ">");
				    line=in.readLine();
				    if ( line==null || line.length()==0)
				    	break;
				    env.setLine(line);
				    SetExpression se=env.getExpression();
				    Enumeration<?> e=se.evaluate( c.getDataSource());
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
		    c.closeDB();
		}
	}

}
