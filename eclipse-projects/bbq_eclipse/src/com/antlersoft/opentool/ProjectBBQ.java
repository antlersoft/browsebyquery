
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.opentool;

import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.ide.Browser;

import com.borland.primetime.node.FileNode;
import com.borland.primetime.node.TextFileNode;

import com.borland.primetime.vfs.Url;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

import javax.swing.*;
//import javax.swing.filechooser.ExtensionFileFilter;
import java.awt.*;
import java.awt.event.*;

import com.antlersoft.analyzer.SourceObject;
import com.antlersoft.analyzer.query.ParseException;
import com.antlersoft.analyzer.query.QueryParser;
import com.antlersoft.analyzer.query.SetExpression;

public class ProjectBBQ implements NodeViewer
{
    private ProjectAnalyzer analyzer;
    private JComponent component;
    private PropertyChangeSupport changeSupport;
    private JTextArea queryArea;
    private ResultArea outputArea;
    private JScrollPane storedArea;
    private HistoryList historyList;
    private StoredValuesList storedValuesList;
    private Browser browser;
    private BBQNode node;

    ProjectBBQ( ProjectAnalyzer a, Browser b, BBQNode n)
    {
        analyzer=a;
        browser=b;
        node=n;
        component=createComponents();
    }

    public void releaseViewer()
    {
        analyzer.qp.removeStoredValuesListener( storedValuesList);
    }

    public void browserDeactivated()
    {
        //TODO: Implement this com.borland.primetime.ide.NodeViewer method
    }

    public void browserActivated()
    {
        //TODO: Implement this com.borland.primetime.ide.NodeViewer method
    }

    public void viewerDeactivated()
    {
        //TODO: Implement this com.borland.primetime.ide.NodeViewer method
    }

    public void viewerDeactivating() throws com.borland.primetime.util.VetoException
    {
        //TODO: Implement this com.borland.primetime.ide.NodeViewer method
    }

    public void viewerActivated(boolean parm1)
    {
        analyzer.db.makeCurrent();
    }

    public void viewerNodeChanged()
    {
        //TODO: Implement this com.borland.primetime.ide.NodeViewer method
    }

    public void removePropertyChangeListener(PropertyChangeListener parm1)
    {
        changeSupport.removePropertyChangeListener( parm1);
    }

    public void addPropertyChangeListener(PropertyChangeListener parm1)
    {
        changeSupport.addPropertyChangeListener( parm1);
    }

    public JComponent getStructureComponent()
    {
        return storedArea;
    }

    public JComponent getViewerComponent()
    {
        return component;
    }

    public Icon getViewerIcon()
    {
        return null;
    }

    public String getViewerDescription()
    {
        //TODO: Resource this
        return "Browse-By-Query for project "+analyzer.project.getDisplayName();
    }

    public String getViewerTitle()
    {
        //TODO: Resource this
        return "BBQ";
    }

    void displayException( String caption, Throwable t)
    {
        StringWriter sw=new StringWriter( 1000);
        PrintWriter pw=new PrintWriter( sw);
        t.printStackTrace( pw);
        pw.close();
        JOptionPane.showMessageDialog( component, t.getMessage()+" \n"+sw.toString(),
            caption, JOptionPane.ERROR_MESSAGE);
    }

    private JComponent createComponents()
    {
        // Create components
        JButton queryButton=new JButton( "Query");
        final JButton analyzeButton=new JButton( "Analyze");
        Dimension buttonDimension=analyzeButton.getPreferredSize();
        queryButton.setMinimumSize( buttonDimension);
        queryButton.setMaximumSize( buttonDimension);
        analyzeButton.setMinimumSize( buttonDimension);
        analyzeButton.setMaximumSize( buttonDimension);
        queryArea=new JTextArea( 2, 60);
        JScrollPane queryScroll=new JScrollPane( queryArea);
        buttonDimension.height*=2;
        buttonDimension.width*=3;
        queryScroll.setMinimumSize( buttonDimension);
        historyList=new HistoryList( queryArea);
        storedValuesList=new StoredValuesList();
        JScrollPane historyScroll=new JScrollPane( historyList);
        storedArea=new JScrollPane( storedValuesList);
        Box buttonBox=Box.createVerticalBox();
        buttonBox.add( queryButton);
        buttonBox.add( Box.createVerticalStrut( 2*(int)
            buttonDimension.getHeight()));
        buttonBox.add( Box.createVerticalGlue());
        buttonBox.add( analyzeButton);
        Box upperPane=Box.createHorizontalBox();
        upperPane.add( buttonBox);
        Box textBox=Box.createVerticalBox();
        textBox.add( new JSplitPane( JSplitPane.VERTICAL_SPLIT, queryScroll,
            historyScroll));
        upperPane.add( textBox);
        outputArea=new ResultArea( 16, 80);
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
                        analyzer.qp.setLine( line);
                        SetExpression se=analyzer.qp.getExpression();
                        historyList.addQuery( line);
                        outputArea.setResults( se.execute( analyzer.db));
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
        analyzeButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent ae)
                {
                    try
                    {
                        analyzer.updateDB( node, analyzeButton);
                    }
                    catch ( Exception e)
                    {
                        e.printStackTrace();
                        //displayException( "Analyzer error:", e);
                    }
                }
            });

        return mainPane;
    }

    class StoredValuesList extends JList implements PropertyChangeListener
    {
        StoredValuesList()
        {
            super();
            analyzer.qp.addStoredValuesListener( this);
            setVisibleRowCount(6);
            setListData( analyzer.qp.getStoredValues());
        }

        public void propertyChange( PropertyChangeEvent pce)
        {
            setListData( analyzer.qp.getStoredValues());
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
					if (e.getClickCount() >1) {
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

    class ResultArea extends JTextArea
    {
        ResultArea( int lines, int columns)
        {
            super( lines, columns);
            indexMap=new TreeMap( Collections.reverseOrder());
            selectedObject=null;
            addMouseListener( new ResultListener());
        }

        void setResults( Enumeration e)
        {
            selectedObject=null;
            indexMap.clear();
            TreeMap resultSorter=new TreeMap();
            while ( e.hasMoreElements())
            {
                Object sourceObject=e.nextElement();
                resultSorter.put( sourceObject.toString(),
                    sourceObject);
            }
            Iterator iterator=resultSorter.entrySet().iterator();
            StringBuffer results=new StringBuffer();
            while ( iterator.hasNext())
            {
                Map.Entry entry=(Map.Entry)iterator.next();
                indexMap.put( new Integer( results.length()), entry.getValue());
                results.append( entry.getKey());
                results.append( '\n');
            }
            setText( results.toString());
        }

        void jumpToSelectedObject()
        {
            if ( selectedObject!=null)
            {
                char[] nameArray=selectedObject.getDBClass().getName().
                    toCharArray();
                for ( int i=0; i<nameArray.length; i++)
                {
                    if ( nameArray[i]=='.')
                        nameArray[i]='/';
                }
                String path=new String( nameArray);
System.out.println( "Trying path "+path);
                Url fileUrl;
                for ( fileUrl=analyzer.project.findSourcePathUrl( path+".java");
                    fileUrl==null;)
                {
                    int lastIndex=path.lastIndexOf( '/');
                    if ( lastIndex== -1)
                        break;
                    path=path.substring( 0, path.lastIndexOf( '/'));
System.out.println( "Trying path "+path);
                    fileUrl=analyzer.project.findSourcePathUrl( path+".java");
                }
                if ( fileUrl!=null)
                {
System.out.println( "fileUrl is "+fileUrl.getName());
                    FileNode node=analyzer.project.getNode( fileUrl);
                    try
                    {
                        browser.setActiveNode( node, true);
                        if ( node instanceof TextFileNode)
                        {
                            ((TextFileNode)node).setCaretPosition(
                                selectedObject.getLineNumber(), 1, true);
                        }
                    }
                    catch ( Exception e)
                    {
e.printStackTrace();
                    }
                }
            }
        }

        private TreeMap indexMap; // Integer, Class
        private SourceObject selectedObject;

        class ResultListener extends MouseAdapter
        {
            public void mousePressed( MouseEvent e)
            {
                int count=e.getClickCount();
                if ( count==1)
                {
                    selectedObject=null;
                    Iterator i=indexMap.tailMap(
                        new Integer( viewToModel( e.getPoint()))).
                        values().iterator();
                    if ( i.hasNext())
                    {
                        Object resultObject=i.next();
                        if ( resultObject instanceof SourceObject)
                            selectedObject=(SourceObject)resultObject;
                    }
                }
                if ( count>1)
                {
                    jumpToSelectedObject();
                }
            }
        }
    }
}