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
	IndexAnalyzeDB sadb=new IndexAnalyzeDB();
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
