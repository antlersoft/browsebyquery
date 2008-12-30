/*
 * <p>Copyright (c) 2000-2005,2007-2008  Michael A. MacDonald<p>
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

import com.antlersoft.analyzer.query.QueryParser;

import com.antlersoft.query.environment.ui.AbstractDBContainer;
import com.antlersoft.query.environment.ui.QueryFrame;

import com.antlersoft.util.ExtensionFileFilter;

public class UIQuery extends QueryFrame
{
	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getAnalyzeMenuItem()
	 */
	@Override
	protected String getAnalyzeMenuItem() {
		return "Analyze jar / directory / class files";
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getExtensionFileFilter()
	 */
	@Override
	protected ExtensionFileFilter getExtensionFileFilter() {
        ExtensionFileFilter filter=new ExtensionFileFilter( ".class files, dirs and jars");
        filter.addExtension( "class");
        filter.addExtension( "zip");
        filter.addExtension( "jar");
        return filter;
	}

    /* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getDBExtensionFileFilter()
	 */
	@Override
	protected ExtensionFileFilter getDBExtensionFileFilter() {
        ExtensionFileFilter filter=new ExtensionFileFilter( "Browse-by-Query database");
        filter.addExtension( "pj");
        filter.addExtension( "bbq");
        return filter;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getOpenMenuItem()
	 */
	@Override
	protected String getOpenMenuItem() {
		return "Open a different database...";
	}

	IndexAnalyzeDB analyzerDB;

    UIQuery( IndexAnalyzeDB db)
    {
    	super( new QueryParser(), db);
    	analyzerDB=db;
    }
 
    public static void main( String argv[])
        throws Exception
    {
    	IndexAnalyzeDB db=new IndexAnalyzeDB();
    	db.initializeFromContext(AbstractDBContainer.createContextWithLegacyCommandLine(argv), "test.bbq", "com.antlersoft.browsebyquery.java");
        final UIQuery app = new UIQuery(db);
        
        app.showFrame("/icons/weber-small.gif");
    }
}
