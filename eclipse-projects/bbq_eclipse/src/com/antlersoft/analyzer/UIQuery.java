/*
 * <p>Copyright (c) 2000-2005,2007  Michael A. MacDonald<p>
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

import java.io.File;

import com.antlersoft.analyzer.query.QueryParser;

import com.antlersoft.odb.ObjectDBException;

import com.antlersoft.query.DataSource;
import com.antlersoft.query.environment.ui.QueryFrame;

import com.antlersoft.util.ExtensionFileFilter;

public class UIQuery extends QueryFrame
{
    /* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#analyze(java.io.File[])
	 */
	protected void analyze(File[] selectedFiles) throws Exception {
        for ( int i=0; i<selectedFiles.length; i++)
        {
            DBClass.addFileToDB( selectedFiles[i], analyzerDB);
        }
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#clearDB()
	 */
	protected void clearDB() throws Exception {
        analyzerDB.clearDB( analyzerDBOpenString);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#closeDB()
	 */
	protected void closeDB() throws Exception {
        analyzerDB.closeDB();		
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getAnalyzeMenuItem()
	 */
	protected String getAnalyzeMenuItem() {
		return "Analyze jar / directory / class files";
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getDataSource()
	 */
	protected DataSource getDataSource() {
		return analyzerDB;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getExtensionFileFilter()
	 */
	protected ExtensionFileFilter getExtensionFileFilter() {
        ExtensionFileFilter filter=new ExtensionFileFilter( ".class files, dirs and jars");
        filter.addExtension( "class");
        filter.addExtension( "zip");
        filter.addExtension( "jar");
        return filter;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#saveDB()
	 */
	protected void saveDB() throws Exception {
		analyzerDB.commit();
	}

    IndexAnalyzeDB analyzerDB;
    String analyzerDBOpenString;

    UIQuery()
    {
    	super( new QueryParser());
        analyzerDB=new IndexAnalyzeDB();
    }

 
    public static void main( String argv[])
        throws Exception
    {
        final UIQuery app = new UIQuery();
        boolean db_cleared=false;
        if ( argv.length<=0)
        	app.analyzerDBOpenString="test.bbq";
		else
			app.analyzerDBOpenString=argv[0];
        try
        {
            app.analyzerDB.openDB(app.analyzerDBOpenString);
        }
        catch ( ObjectDBException odb)
        {
        	db_cleared=true;
            app.analyzerDB.clearDB( app.analyzerDBOpenString);
        }
        catch ( RuntimeException rte)
        {
        	db_cleared=true;
            app.analyzerDB.clearDB( app.analyzerDBOpenString);
        }
        
        app.showFrame((argv.length>0 ? "Querying " : "Querying default DB ")+app.analyzerDBOpenString, new File(app.analyzerDBOpenString), db_cleared, "/icons/weber-small.gif");
    }
}
