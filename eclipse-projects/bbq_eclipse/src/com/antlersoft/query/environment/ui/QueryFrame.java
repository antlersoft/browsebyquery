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
package com.antlersoft.query.environment.ui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import com.antlersoft.query.BasicBase;
import com.antlersoft.query.SetExpression;

import com.antlersoft.query.environment.AnalyzerQuery;
import com.antlersoft.query.environment.ParseException;
import com.antlersoft.query.environment.QueryException;

import com.antlersoft.util.ExtensionFileFilter;

/**
 * Base class for Swing-based UI of AnalyzerQuery environment query languages
 * 
 * @author Michael A. MacDonald
 *
 */
public class QueryFrame
{
	JFileChooser chooser;
	JFileChooser dbChooser;
    AnalyzerQuery qp;
    protected JTextArea queryArea;
    protected JTextArea outputArea;
    protected HistoryList historyList;
    protected JFrame frameWindow;
    private DBContainer container;
    private boolean analyzing;
    Action exitAction;
    Action openAction;
    Action clearAction;
    Action analyzeAction;
    
    /**
     * Get filter for open file dialog
     * @return File filter for files this app can analyze, or null if this app doesn't analyze file
     */
    protected ExtensionFileFilter getExtensionFileFilter()
    {
    	return null;
    }
    
    /**
     * Get string for Open Database menu item, or null if this app doesn't support
     * opening a new database.
     * @return Menu item string or null
     */
    protected String getOpenMenuItem()
    {
    	return null;
    }
    
    /**
     * Get filter for open db file dialog
     * @return Menu item string or null
     */
    protected ExtensionFileFilter getDBExtensionFileFilter()
    {
    	return null;
    }
    
    public AnalyzerQuery getEnvironment()
    {
    	return qp;
    }
    
    public DBContainer getDBContainer()
    {
    	return container;
    }
    
    /**
     * Get string to associate with analyze menu action (if any)
     * Default implementation returns null
     * @return String to put in menu, or null if no analyze action
     */
    protected String getAnalyzeMenuItem(){
    	return null;
    }
    
    /**
     * Create the base frame for a bbq ui with the provided parser environment
     * @param query_environment Query parser environment
     * @param cont Container of the database connection
     */
    protected QueryFrame( AnalyzerQuery query_environment, DBContainer cont)
    {
        qp=query_environment;
        container=cont;
        ExtensionFileFilter filter=getExtensionFileFilter();
        if ( filter!=null)
        {
	        chooser=new JFileChooser();
	        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
	        chooser.setMultiSelectionEnabled( true);
	        chooser.setFileFilter( filter);
        }
        exitAction=new ExitAction();
    }
    
    /**
     * Convenience constructor for creating a frame from the query parser, with the standard AnalyzerQuery environment
     * @param parser Query parser
     * @param cont Container of the database connection
     */
    protected QueryFrame( BasicBase parser, DBContainer cont)
    {
    	this(new AnalyzerQuery(parser), cont);
    }

    protected Component createComponents()
    {
        // Create components
        JButton queryButton=new JButton( "Query");
        Dimension buttonDimension=queryButton.getPreferredSize();
        queryButton.setMinimumSize( buttonDimension);
        queryButton.setMaximumSize( buttonDimension);
        queryArea=new JTextArea( 2, 60);
        JScrollPane queryScroll=new JScrollPane( queryArea);
        historyList=new HistoryList( queryArea);
        JScrollPane historyScroll=new JScrollPane( historyList);
        JScrollPane storedArea=new JScrollPane( new StoredValuesList( qp));
        Box buttonBox=Box.createVerticalBox();
        buttonBox.add( queryButton);
        buttonBox.add( Box.createVerticalGlue());
        Box upperPane=Box.createHorizontalBox();
        upperPane.add( buttonBox);
        Box textBox=Box.createVerticalBox();
        textBox.add( new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
            new JSplitPane( JSplitPane.VERTICAL_SPLIT, queryScroll,
            historyScroll), storedArea));
        upperPane.add( textBox);
        outputArea=new JTextArea( 16, 80);
        JScrollPane lowerPane=new JScrollPane( outputArea);
        JSplitPane mainPane=new JSplitPane( JSplitPane.VERTICAL_SPLIT, upperPane, lowerPane);

        // Create actions
        outputArea.setEditable( false);
        queryButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent ae)
                {
                    try
                    {
                        String line=queryArea.getText();
                        if ( line==null || line.length()==0)
                            return;
                        qp.setLine( line);
                        SetExpression se=qp.getExpression();
                        historyList.addQuery( line);
                        Enumeration e=se.evaluate( container.getDataSource());
                        TreeSet resultSorter=new TreeSet();
                        while ( e.hasMoreElements())
                            resultSorter.add( e.nextElement().toString());
                        Iterator iterator=resultSorter.iterator();
                        StringBuilder results=new StringBuilder();
                        while ( iterator.hasNext())
                        {
                            results.append( (String)iterator.next());
                            results.append( '\n');
                        }
                        outputArea.setText( results.toString());
                    }
                    catch ( ParseException pe)
                    {
                        displayMessage( "Parse Error", pe.getMessage());
                    }
                    catch ( Exception unkn)
                    {
                        displayException( "Query Error", unkn);
                    }
                }
            });

        return mainPane;
    }

    protected JMenuBar createMenuBar()
    {
        JMenuBar menuBar=new JMenuBar();

        // Create File menu
        JMenu fileMenu=new JMenu( "File");

        // Create analyze action
        if ( chooser!=null)
        {
        	analyzeAction=new AnalyzeAction();
        	fileMenu.add( analyzeAction);
        }

        // Create save action
        fileMenu.add( new SaveAction());

        // Create clear action
        fileMenu.addSeparator();
        clearAction=new ClearAction();
        fileMenu.add( clearAction);
        
        // Create open action
        if ( getOpenMenuItem()!=null)
        {
        	dbChooser=new JFileChooser();
        	dbChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        	ExtensionFileFilter eff=getDBExtensionFileFilter();
        	if ( eff!=null)
        		dbChooser.setFileFilter(eff);
        	dbChooser.setMultiSelectionEnabled(false);
        	fileMenu.addSeparator();
        	openAction=new OpenAction();
        	fileMenu.add( openAction);
        }
        
        // Create Exit action
        fileMenu.addSeparator();
        fileMenu.add( exitAction);

        // Add menu to menu bar
        menuBar.add( fileMenu);

        return menuBar;
    }
    
    void displayMessage( String caption, String m)
    {
        JOptionPane.showMessageDialog( frameWindow, m,
                caption, JOptionPane.ERROR_MESSAGE);
    }

    void displayException( String caption, Throwable t)
    {
        StringWriter sw=new StringWriter( 1000);
        PrintWriter pw=new PrintWriter( sw);
t.printStackTrace();
        t.printStackTrace( pw);
        pw.close();
        JOptionPane.showMessageDialog( frameWindow, t.getMessage()+" \n"+sw.toString(),
            caption, JOptionPane.ERROR_MESSAGE);
    }
    
    private void readEnvironment()
    {	
		File environmentFile=container.getEnvironmentFile();
		try
		{
			if ( environmentFile!=null && environmentFile.canRead())
			{
				FileReader reader=new FileReader( environmentFile);
				qp.readEnvironment( reader);
				reader.close();
			}
		}
		catch ( Exception e)
		{
			displayException( "Error loading saved expressions", e);
		}
    }
    
    protected void showFrame( String icon_resource_path)
    {
    	showFrame( getClass().getResource(icon_resource_path));
    }
    
    protected void showFrame( URL icon_resource)
    {
        JFrame appFrame=new JFrame( container.titleOfDB() );
        if ( icon_resource!=null)
        	appFrame.setIconImage(appFrame.getToolkit().getImage(icon_resource));
        frameWindow=appFrame;
        Component contents = createComponents();
        appFrame.getContentPane().add(contents, BorderLayout.CENTER);
        appFrame.setJMenuBar( createMenuBar());

        readEnvironment();

        //Finish setting up the frame, and show it.
        appFrame.addWindowListener(new WindowAdapter() {
            /* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
		        if ( container.getOpenMessage()!=null)
		        	JOptionPane.showMessageDialog( frameWindow, container.getOpenMessage(), "Opening Database Issue", JOptionPane.ERROR_MESSAGE);
		        if ( getDBContainer().getDataSource()==null)
		        {
		        	if ( openAction!=null)
		        	{
		        		openAction.actionPerformed(null);
		        	}
		        	else
		        	{
		        		JOptionPane.showMessageDialog( frameWindow, "No datasource available");
		        		exitAction.actionPerformed(null);
		        	}
		        }
			}

			public void windowClosing(WindowEvent e) {
                try
                {
                	container.closeDB();
                    saveEnvironment();
                }
                catch ( Exception exception)
                {
                }
                System.exit(0);      }        });
        appFrame.pack();
        appFrame.setLocation( 10, 10);
        appFrame.setVisible(true);
        updateTitleBar();
    }
    
    void updateTitleBar()
    {
    	StringBuilder sb=new StringBuilder("Database ");
    	sb.append( container.titleOfDB());
    	if ( analyzing)
    	{
    		sb.append( " - analyzing");
    	}
    	frameWindow.setTitle( sb.toString());
    }

    /**
	 * @param environment_file
	 * @throws IOException
	 * @throws QueryException
	 */
	void saveEnvironment() throws IOException, QueryException {
		if ( container.getEnvironmentFile()!=null)
		{
			FileWriter writer=new FileWriter( container.getEnvironmentFile());
			qp.writeEnvironment( writer);
			writer.close();
		}
	}
	
	protected void setIsAnalyzing(boolean isAnalyzing)
	{
		analyzing=isAnalyzing;
		
		exitAction.setEnabled(!analyzing);
		if ( openAction!=null )
			openAction.setEnabled(!analyzing);
		clearAction.setEnabled(!analyzing);
		if ( analyzeAction!=null )
			analyzeAction.setEnabled(!analyzing);
		updateTitleBar();
	}

	class ExitAction extends AbstractAction
    {
        ExitAction()
        {
            super( "Exit");
        }

        public void actionPerformed( ActionEvent event)
        {
            frameWindow.dispatchEvent( new WindowEvent( frameWindow, WindowEvent.WINDOW_CLOSING));
        }
    }

    class SaveAction extends AbstractAction
    {
        SaveAction()
        {
            super( "Save the Database");
        }

        public void actionPerformed( ActionEvent event)
        {
            try
            {
                container.saveDB();
                saveEnvironment();
            }
            catch ( Exception e)
            {
                displayException( "Save Error", e);
            }
     }
    }

    class ClearAction extends AbstractAction
    {
        ClearAction()
        {
            super( "Clear the Database");
        }

        public void actionPerformed( ActionEvent event)
        {
            try
            {
                if ( JOptionPane.showConfirmDialog( frameWindow,
                    "Are you sure you want to clear all the program data from the database?",
                    "Confirm Clear Database", JOptionPane.YES_NO_OPTION)==
                     JOptionPane.YES_OPTION)
                    container.clearDB();
            }
            catch ( Exception e)
            {
                displayException( "Clear Error", e);
            }
         }
    }
    
    class AnalyzeAction extends AbstractAction
    {
		AnalyzeAction()
        {
            super( getAnalyzeMenuItem());
        }

        public void actionPerformed( ActionEvent event)
        {
            try
            {
                if ( chooser.showDialog( frameWindow, "Analyze")==JFileChooser.APPROVE_OPTION)
                {
                	final File[] files=chooser.getSelectedFiles();
                	setIsAnalyzing(true);
                	new Thread( new Runnable() {
                		public void run()
                		{
                			Exception excp=null;
                            try
                            {
                                container.analyze( files);
                            }
                            catch ( Exception e)
                            {
                            	excp=e;
                            }
                            finally
                            {
                            	SwingUtilities.invokeLater( new AnalyzeComplete( excp));
                            }
                		}
                	}).start();
                }
            }
            catch ( Exception e)
            {
                displayException( "Analyze Error", e);
            }
        }
        /**
         * Invoke this on the UI thread when the analyzing task is complete
         * @author Michael A. MacDonald
         *
         */
        class AnalyzeComplete implements Runnable
        {
        	Exception excp;
        	
        	/**
        	 * 
        	 * @param e Exception that ended analysis, or null if it ended normally
        	 */
        	AnalyzeComplete( Exception e)
        	{
        		excp=e;
        	}
        	
        	public void run()
        	{
        		setIsAnalyzing(false);
        		if ( excp!=null)
        			displayException( "Analyze Error", excp);
        	}
        }
    }
    
    /**
     * Open a new database
	 * @author Michael A. MacDonald
	 *
	 */
	class OpenAction extends AbstractAction {
		OpenAction()
		{
			super(getOpenMenuItem());
		}
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
            try
            {
                if ( dbChooser.showDialog( frameWindow, getOpenMenuItem())==JFileChooser.APPROVE_OPTION)
                {
                	final File new_dir=dbChooser.getSelectedFile();
                	container.openDB(new_dir);
                	updateTitleBar();
                }
            }
            catch ( Exception ex)
            {
                displayException( "Open Database Error", ex);
                exitAction.actionPerformed(e);
            }
		}

	}
}
