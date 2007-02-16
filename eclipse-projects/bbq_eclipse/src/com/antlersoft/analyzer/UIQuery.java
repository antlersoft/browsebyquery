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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.antlersoft.analyzer.query.AnalyzerQuery;


import com.antlersoft.odb.ObjectDBException;

import com.antlersoft.query.SetExpression;
import com.antlersoft.query.environment.ParseException;
import com.antlersoft.query.environment.ui.HistoryList;
import com.antlersoft.query.environment.ui.StoredValuesList;

import com.antlersoft.util.ExtensionFileFilter;

public class UIQuery
{
    JFileChooser chooser;
    AnalyzerDB analyzerDB;
    String analyzerDBOpenString;
    AnalyzerQuery qp;
    JTextArea queryArea;
    JTextArea outputArea;
    HistoryList historyList;
    Window frameWindow;

    UIQuery()
    {
        analyzerDB=new IndexAnalyzeDB();
        qp=new AnalyzerQuery();
        chooser=new JFileChooser();
        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled( true);
        ExtensionFileFilter filter=new ExtensionFileFilter( ".class files, dirs and jars");
        filter.addExtension( "class");
        filter.addExtension( "zip");
        filter.addExtension( "jar");
        chooser.setFileFilter( filter);
    }

    Component createComponents()
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
                        Enumeration e=se.evaluate( analyzerDB);
                        TreeSet resultSorter=new TreeSet();
                        while ( e.hasMoreElements())
                            resultSorter.add( e.nextElement().toString());
                        Iterator iterator=resultSorter.iterator();
                        StringBuffer results=new StringBuffer();
                        while ( iterator.hasNext())
                        {
                            results.append( (String)iterator.next());
                            results.append( '\n');
                        }
                        outputArea.setText( results.toString());
                    }
                    catch ( ParseException pe)
                    {
                        displayException( "Parse Error", pe);
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

        JFrame appFrame=new JFrame( (argv.length>0 ? "Querying " : "Querying default DB ")+app.analyzerDBOpenString );
        app.frameWindow=appFrame;
        if ( db_cleared)
        	JOptionPane.showMessageDialog( appFrame, "There was a problem opening the existing database, so it was cleared,\nand you must rebuild it by analyzing classes.", "Corrupt Database", JOptionPane.ERROR_MESSAGE);
        Component contents = app.createComponents();
        appFrame.getContentPane().add(contents, BorderLayout.CENTER);
        appFrame.setJMenuBar( app.createMenuBar());
        
		final File environment_file=new File( new File( app.analyzerDBOpenString), "environment.xml");
		try
		{
			if ( environment_file.canRead())
			{
				FileReader reader=new FileReader( environment_file);
				app.qp.readEnvironment( reader);
				reader.close();
			}
		}
		catch ( Exception e)
		{
			app.displayException( "Error loading saved expressions", e);
		}

        //Finish setting up the frame, and show it.
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try
                {
                    app.analyzerDB.closeDB();
                    FileWriter writer=new FileWriter( environment_file);
                    app.qp.writeEnvironment( writer);
                    writer.close();
                }
                catch ( Exception exception)
                {
                }
                System.exit(0);      }        });
        appFrame.pack();
        appFrame.setLocation( 10, 10);
        appFrame.setVisible(true);
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
            if ( analyzerDB!=null)
            {
                try
                {
                    analyzerDB.closeDB();
                    analyzerDB.openDB( analyzerDBOpenString);
                }
                catch ( Exception e)
                {
                    displayException( "Save Error", e);
                }
            }
        }
    }

    /*
    class SaveStateAction extends AbstractAction
    {
    	SaveStateAction()
    	{
    		super( "Save stored expressions");
    	}
    	
    	public void actionPerformed( ActionEvent ae)
    	{
    	}
    }
    */

    class ClearAction extends AbstractAction
    {
        ClearAction()
        {
            super( "Clear the Database");
        }

        public void actionPerformed( ActionEvent event)
        {
            if ( analyzerDB!=null)
            {
                try
                {
                    if ( JOptionPane.showConfirmDialog( frameWindow,
                        "Are you sure you want to clear all the program data from the database?",
                        "Confirm Clear Database", JOptionPane.YES_NO_OPTION)==
                         JOptionPane.YES_OPTION)
                        analyzerDB.clearDB( analyzerDBOpenString);
                }
                catch ( Exception e)
                {
                    displayException( "Clear Error", e);
                }
            }
        }
    }

    class AnalyzeAction extends AbstractAction
    {
        AnalyzeAction()
        {
            super( "Analyze jar / directory / class files");
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
                        // File[] selectedFiles=chooser.getSelectedFiles();
                        // Multi-file selection is broken
                        File[] selectedFiles=new File[] { chooser.getSelectedFile() };

                        for ( int i=0; i<selectedFiles.length; i++)
                        {
                            DBClass.addFileToDB( selectedFiles[i], analyzerDB);
                        }
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
