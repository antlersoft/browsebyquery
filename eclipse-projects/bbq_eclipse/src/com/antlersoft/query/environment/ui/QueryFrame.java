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
import com.antlersoft.query.DataSource;
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
public abstract class QueryFrame
{
    JFileChooser chooser;
    AnalyzerQuery qp;
    protected JTextArea queryArea;
    protected JTextArea outputArea;
    protected HistoryList historyList;
    protected Window frameWindow;
    protected File environmentFile;
    
    /**
     * Get filter for open file dialog
     * @return File filter for files this app can analyze, or null if this app doesn't analyze file
     */
    protected ExtensionFileFilter getExtensionFileFilter()
    {
    	return null;
    }
    
    /**
     * Get the DataSource used for this app
     * @return Object implementing DataSource
     */
    abstract protected DataSource getDataSource();
    
    /**
     * Close the datasource on shutdown
     *
     */
    abstract protected void closeDB() throws Exception;
    
    /**
     * Make sure query db is sync'd to filesystem
     *
     */
    abstract protected void saveDB() throws Exception;
    
    /**
     * Clear the queried db
     */
    abstract protected void clearDB() throws Exception;

    /**
     * Get string to associate with analyze menu action (if any)
     * Default implementation returns null
     * @return String to put in menu, or null if no analyze action
     */
    protected String getAnalyzeMenuItem(){
    	return null;
    }
    
    /**
     * Enter the selected files into the database to be queried.  The default implementation does nothing.
     * @param selected_files
     */
    protected void analyze( File[] selected_files) throws Exception
    {
    }

    protected QueryFrame( BasicBase parser)
    {
        qp=new AnalyzerQuery( parser);
        ExtensionFileFilter filter=getExtensionFileFilter();
        if ( filter!=null)
        {
	        chooser=new JFileChooser();
	        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
	        chooser.setMultiSelectionEnabled( true);
	        chooser.setFileFilter( filter);
        }
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
                        Enumeration e=se.evaluate( getDataSource());
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

    JMenuBar createMenuBar()
    {
        JMenuBar menuBar=new JMenuBar();

        // Create File menu
        JMenu fileMenu=new JMenu( "File");

        // Create analyze action
        if ( chooser!=null)
        	fileMenu.add( new AnalyzeAction());

        // Create save action
        fileMenu.add( new SaveAction());

        // Create clear action
        fileMenu.addSeparator();
        fileMenu.add( new ClearAction());
        
        // Create Exit action
        fileMenu.addSeparator();
        fileMenu.add( new ExitAction());

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
    
    protected void showFrame( String title, File environment_dir, boolean db_cleared, String icon_resource_path)
    {
    	showFrame( title, environment_dir, db_cleared, getClass().getResource(icon_resource_path));
    }
    
    protected void showFrame( String title, File environment_dir, boolean db_cleared, URL icon_resource)
    {
        JFrame appFrame=new JFrame( title );
        if ( icon_resource!=null)
        	appFrame.setIconImage(appFrame.getToolkit().getImage(icon_resource));
        frameWindow=appFrame;
        if ( db_cleared)
        	JOptionPane.showMessageDialog( appFrame, "There was a problem opening the existing database, so it was cleared,\nand you must rebuild it by analyzing classes.", "Corrupt Database", JOptionPane.ERROR_MESSAGE);
        Component contents = createComponents();
        appFrame.getContentPane().add(contents, BorderLayout.CENTER);
        appFrame.setJMenuBar( createMenuBar());
        
		environmentFile=new File( environment_dir, "environment.xml");
		try
		{
			if ( environmentFile.canRead())
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

        //Finish setting up the frame, and show it.
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try
                {
                	closeDB();
                    saveEnvironment();
                }
                catch ( Exception exception)
                {
                }
                System.exit(0);      }        });
        appFrame.pack();
        appFrame.setLocation( 10, 10);
        appFrame.setVisible(true);
    }

    /**
	 * @param environment_file
	 * @throws IOException
	 * @throws QueryException
	 */
	void saveEnvironment() throws IOException, QueryException {
		FileWriter writer=new FileWriter( environmentFile);
		qp.writeEnvironment( writer);
		writer.close();
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
                saveDB();
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
                    clearDB();
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
                    Cursor oldCursor=frameWindow.getCursor();
                    frameWindow.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR));
                    try
                    {
                        analyze( chooser.getSelectedFiles());
                    }
                    finally
                    {
                        frameWindow.setCursor( oldCursor);
                    }
                }
            }
            catch ( Exception e)
            {
                displayException( "Analyze Error", e);
            }
        }
    }
}
