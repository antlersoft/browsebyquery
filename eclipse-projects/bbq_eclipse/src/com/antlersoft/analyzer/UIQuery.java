package com.antlersoft.analyzer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.*;
//import javax.swing.filechooser.ExtensionFileFilter;
import java.awt.*;
import java.awt.event.*;

import com.antlersoft.analyzer.query.ParseException;
import com.antlersoft.analyzer.query.QueryParser;
import com.antlersoft.analyzer.query.SetExpression;

public class UIQuery
{
    JFileChooser chooser;
    AnalyzerDB analyzerDB;
    String analyzerDBOpenString;
    QueryParser qp;
    JTextArea queryArea;
    JTextArea outputArea;
    HistoryList historyList;
    Window frameWindow;

    UIQuery()
    {
        analyzerDB=new ObjectAnalyzeDB();
        qp=new QueryParser();
        chooser=new JFileChooser();
        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
        //chooser.setMultiSelectionEnabled( true); // Doesn't work yet
        //ExtensionFileFilter filter=new ExtensionFileFilter();
        //filter.addExtension( "class");
        //filter.addExtension( "zip");
        //filter.addExtension( "jar");
        //chooser.setFileFilter( filter);
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
        JScrollPane storedArea=new JScrollPane( new StoredValuesList());
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
                        historyList.addQuery( line);
                        SetExpression se=qp.getExpression();
                        Enumeration e=se.execute( analyzerDB);
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
        t.printStackTrace( pw);
        pw.close();
        JOptionPane.showMessageDialog( frameWindow, t.getMessage()+" \n"+sw.toString(),
            caption, JOptionPane.ERROR_MESSAGE);
    }

    public static void main( String argv[])
        throws Exception
    {
        final UIQuery app = new UIQuery();
        app.analyzerDBOpenString=argv[0];
        app.analyzerDB.openDB( app.analyzerDBOpenString);

        JFrame appFrame=new JFrame( "Querying "+argv[0]);
        app.frameWindow=appFrame;
        Component contents = app.createComponents();
        appFrame.getContentPane().add(contents, BorderLayout.CENTER);
        appFrame.setJMenuBar( app.createMenuBar());
        //Finish setting up the frame, and show it.
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try
                {
                    app.analyzerDB.closeDB();
                }
                catch ( Exception exception)
                {
                }
                System.exit(0);      }        });
        appFrame.pack();
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
            super( "Save");
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

    class AnalyzeAction extends AbstractAction
    {
        AnalyzeAction()
        {
            super( "Analyze");
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

    class StoredValuesList extends JList implements PropertyChangeListener
    {
        StoredValuesList()
        {
            super();
            qp.addStoredValuesListener( this);
            setVisibleRowCount(2);
            setListData( qp.getStoredValues());
        }

        public void propertyChange( PropertyChangeEvent pce)
        {
            setListData( qp.getStoredValues());
        }
    }

    class HistoryList extends JList
    {
    	private DefaultListModel dlm;
        private JTextArea text;

    	HistoryList( JTextArea toChange)
        {
        	super( new DefaultListModel());
            text=toChange;
            dlm=(DefaultListModel)getModel();
            setVisibleRowCount(2);
            addMouseListener( new MouseAdapter() {
            	public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
     					synchronized( dlm) {
				        	int index = locationToIndex(e.getPoint());
	            			text.setText( (String)dlm.elementAt(index));
	                		moveToTop( index);
                  		}
			        }
             	}
            });
        }

        synchronized void addQuery( String toAdd)
        {
        	synchronized ( dlm)
         	{
          		int j=0;
        		for ( Enumeration i=dlm.elements();
          			i.hasMoreElements(); j++)
             	{
              		if ( toAdd.equals( i.nextElement()))
                	{
                 		moveToTop( j);
                   		return;
                 	}
            	}
             	dlm.insertElementAt( toAdd, 0);
            }
        }

        private void moveToTop( int index)
        {
        	Object o=dlm.elementAt( index);
         	dlm.remove( index);
            dlm.insertElementAt( o, 0);
            setSelectedIndex( 0);
        }
    }
}
