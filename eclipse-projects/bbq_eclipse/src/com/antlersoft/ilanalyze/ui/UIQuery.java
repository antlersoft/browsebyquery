/*
 * <p>Copyright (c) 2000-2007  Michael A. MacDonald<p>
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
package com.antlersoft.ilanalyze.ui;

import java.io.File;
import java.util.Date;

import com.antlersoft.ilanalyze.db.*;

import com.antlersoft.ilanalyze.parseildasm.IldasmReader;

import com.antlersoft.ilanalyze.query.ILQueryParser;

import com.antlersoft.odb.ObjectDBException;

import com.antlersoft.query.DataSource;

import com.antlersoft.query.environment.ui.QueryFrame;

import com.antlersoft.util.ExtensionFileFilter;

public class UIQuery extends QueryFrame
{
    private ILDB analyzerDB;
    private File analyzerDBOpenFile;

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#clearDB()
	 */
	protected void clearDB() throws Exception {
        analyzerDB=ILDB.clearDB( analyzerDB, analyzerDBOpenFile);		
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#closeDB()
	 */
	protected void closeDB() throws Exception {
		analyzerDB.close();
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
        ExtensionFileFilter filter=new ExtensionFileFilter( "Directories and assembly (.exe, .dll, .il) files");
        filter.addExtension( "exe");
        filter.addExtension( "dll");
        filter.addExtension("il");
        return filter;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#saveDB()
	 */
	protected void saveDB() throws Exception {
        analyzerDB.commit();		
	}


    /* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getAnalyzeMenuItem()
	 */
	protected String getAnalyzeMenuItem() {
		return "Analyze directories or assembly files";
	}


	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#analyze(java.io.File[])
	 */
	protected void analyze(File[] selectedFiles) throws Exception {
   	ILDBDriver driver=new ILDBDriver(analyzerDB);
   	IldasmReader reader=new IldasmReader();
   	reader.setOldestFileDate((Date)analyzerDB.getRootObject("LAST_SCANNED"));
   	Date last_scanned=new Date();
   	

       for ( int i=0; i<selectedFiles.length; i++)
       {
       	reader.sendFileToDriver(selectedFiles[i], driver);
       }
       
       analyzerDB.makeRootObject("LAST_SCANNED", last_scanned);
       analyzerDB.commitAndRetain();
	}

	UIQuery()
    {
    	super( new ILQueryParser());
    }

    public static void main( String argv[])
        throws Exception
    {
        final UIQuery app = new UIQuery();
        boolean db_cleared=false;
        String openString;
        if ( argv.length<=0)
        	openString="test.bbq";
		else
			openString=argv[0];
        app.analyzerDBOpenFile=new File( openString);
        try
        {
            app.analyzerDB=new ILDB(app.analyzerDBOpenFile);
        }
        catch ( ObjectDBException odb)
        {
        	db_cleared=true;
            app.analyzerDB=ILDB.clearDB( app.analyzerDB, app.analyzerDBOpenFile);
        }
        catch ( RuntimeException rte)
        {
        	db_cleared=true;
            app.analyzerDB=ILDB.clearDB( app.analyzerDB, app.analyzerDBOpenFile);
        }
        
        app.showFrame((argv.length>0 ? "Querying " : "Querying default DB ")+openString, app.analyzerDBOpenFile, db_cleared, "/icons/weber-small.gif");
    }
}
