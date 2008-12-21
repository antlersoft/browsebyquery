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

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.antlersoft.ilanalyze.db.*;

import com.antlersoft.ilanalyze.parseildasm.IldasmReader;

import com.antlersoft.ilanalyze.query.ILQueryParser;

import com.antlersoft.ilanalyze.xmlintf.BrowseByQueryXml;

import com.antlersoft.odb.ObjectDBException;

import com.antlersoft.query.DataSource;

import com.antlersoft.query.environment.ui.QueryFrame;

import com.antlersoft.util.ExtensionFileFilter;

public class UIQuery extends QueryFrame
{
	private ILDBContainer container;
    JCheckBoxMenuItem listeningCheckBox;
    QuerySocketServer server;

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
	 * @see com.antlersoft.query.environment.ui.QueryFrame#getAnalyzeMenuItem()
	 */
	@Override
	protected String getAnalyzeMenuItem() {
		return "Analyze directories or assembly files";
	}


	/**
	 * Add server menu to the menubar
	 */
	protected JMenuBar createMenuBar()
	{
		JMenuBar result=super.createMenuBar();
		
		JMenu server_menu=new JMenu("Server");
		listeningCheckBox=new JCheckBoxMenuItem( new ListeningAction());
		server_menu.add( listeningCheckBox);
		result.add(server_menu);
		
		return result;
	}

	UIQuery( ILDBContainer c)
    {
    	super( new ILQueryParser(), c);
		container=c;
    }
	
	private synchronized void setListeningThread( boolean status)
	{
		if ( server==null && status)
		{
	        try
	        {
	        	server=new QuerySocketServer( new BrowseByQueryXml( getEnvironment(), container), QuerySocketServer.DEFAULT_PORT);
	        }
	        catch ( IOException ioe)
	        {
	        	
	        }
		}
		if ( server!=null)
		{
			if ( status)
			{
				if ( ! server.isRunning())
				{
					new Thread( server).start();
				}
			}
			else
				server.cancel();
		}
		if ( listeningCheckBox!=null)
		{
			listeningCheckBox.setSelected( status);
		}
		((ILDB)container.getDataSource()).makeRootObject( "RUN_SERVER", new Boolean(status));
	}
	
	class ListeningAction extends AbstractAction
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			setListeningThread( listeningCheckBox.isSelected());
		}

		ListeningAction()
		{
			super("Serve Visual Studio plugin requests");
		}
	}

    public static void main( String argv[])
    throws Exception
    {
    	ILDBContainer c=new ILDBContainer();
    	c.initializeFromContext(c.createContextWithLegacyCommandLine(argv),
    			"test.bbq", "com.antlersoft.browsebyquery.il");
        final UIQuery app = new UIQuery(c);
        
        app.showFrame("/icons/weber-small.gif");
        
        ILDB db=(ILDB)c.getDataSource();
        
        if ( db!=null)
        {
	        Boolean b=(Boolean)((ILDB)c.getDataSource()).getRootObject( "RUN_SERVER");
	        app.setListeningThread( b==null ? false : b.booleanValue());
        }
    }
}
