
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
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

import java.util.ArrayList;
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

import com.antlersoft.query.SetExpression;

import com.antlersoft.query.environment.ParseException;

import com.antlersoft.query.environment.ui.HistoryList;
import com.antlersoft.query.environment.ui.StoredValuesList;

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

        // Initialize the history list with the recent queries
        String[] recent_queries=BBQPathsGroup.recentQueriesProperty.getValues( n);
        for ( int i=recent_queries.length-1; i>=0; --i)
            historyList.addQuery( recent_queries[i]);
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
        // Save recent query list
        ArrayList temp_list=new ArrayList();
        try
        {
            SetExpression se=analyzer.qp.getExpression();
            for ( Enumeration i=se.evaluate( analyzer.db);
                  i.hasMoreElements();)
            {
                temp_list.add( i.nextElement());
            }
            String[] imports=new String[temp_list.size()];
            temp_list.toArray( imports);
            BBQPathsGroup.importsProperty.setValues( node, imports);
            analyzer.saveEnvironment();
        }
        catch ( Exception e)
        {
e.printStackTrace();
        }
        temp_list.clear();
        for ( Enumeration i=historyList.getContents(); i.hasMoreElements() &&
              temp_list.size()<10;)
        {
            temp_list.add( i.nextElement());
        }
        String[] recent=new String[temp_list.size()];
        temp_list.toArray( recent);
        BBQPathsGroup.recentQueriesProperty.setValues( node, recent);
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
        storedValuesList=new StoredValuesList( analyzer.qp);
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
                        outputArea.setResults( se.evaluate( analyzer.db));
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
                        analyzer.saveEnvironment();
                    }
                    catch ( Exception e)
                    {
                        displayException( "Analyzer error:", e);
                    }
                }
            });

        return mainPane;
    }

    class ResultArea extends JTextArea
    {
        private JPopupMenu popupMenu;
        ResultArea( int lines, int columns)
        {
            super( lines, columns);
            indexMap=new TreeMap( Collections.reverseOrder());
            selectedObject=null;
            addMouseListener( new ResultListener());

            popupMenu=new JPopupMenu();
            popupMenu.add( new JumpToSelectedObjectAction());
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
                String path=selectedObject.getDBClass().getInternalName();
                int lastSlash=path.lastIndexOf( '/');
                if ( lastSlash!= -1)
                    path=path.substring( 0, lastSlash+1);
                path=path+selectedObject.getDBClass().getSourceFile();
                Url fileUrl=analyzer.project.findSourcePathUrl( path);
                if ( fileUrl!=null)
                {
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

        class JumpToSelectedObjectAction extends AbstractAction
        {
            JumpToSelectedObjectAction()
            {
                super( "Jump to Object");
            }
            public void actionPerformed( ActionEvent ae)
            {
                jumpToSelectedObject();
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
                showPopup(e);
            }
            public void mouseReleased( MouseEvent e) {
                showPopup( e);
            }
            void showPopup( MouseEvent e)
            {
                if (e.isPopupTrigger()) {
                    if (selectedObject!=null)
                        popupMenu.show(ResultArea.this, e.getX(), e.getY());
                }
            }
        }
    }
}
