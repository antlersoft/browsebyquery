
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

import java.awt.EventQueue;

import java.io.File;
import java.io.BufferedInputStream;

import java.util.Stack;

import javax.swing.JButton;

import com.borland.jbuilder.node.JBProject;

import com.borland.primetime.vfs.Filesystem;
import com.borland.primetime.vfs.Url;

import com.antlersoft.analyzer.AnalyzeClass;
import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.ObjectAnalyzeDB;
import com.antlersoft.analyzer.query.QueryParser;

class ProjectAnalyzer
{
    JBProject project;
    ObjectAnalyzeDB db;
    QueryParser qp;
    private String goodPath;

    ProjectAnalyzer( BBQNode node)
        throws Exception
    {
System.out.println( "<init>ProjectAnalyzer");
        project=(JBProject)node.getProject();
        db=new ObjectAnalyzeDB();
        String dbPath=BBQPathsGroup.pathsProperty.getValue( node);
        File analyzerFile;
        if ( dbPath.length()==0)
        {
            analyzerFile=new File(
                project.getProjectPath().getFileObject(), "analyzer.pj");
            BBQPathsGroup.pathsProperty.setValue( node, analyzerFile.getPath());
        }
        else
        {
            analyzerFile=new File( dbPath);
        }
        db.openDB( analyzerFile.getCanonicalPath());
        qp=new QueryParser();
        goodPath=analyzerFile.getCanonicalPath();
    }

    void updateDB( final BBQNode node, final JButton button)
        throws Exception
    {
        long lastModified=Long.parseLong( BBQPathsGroup.updateTimeProperty.
            getValue( node));
        String classPath=BBQPathsGroup.classPathProperty.getValue( node);
        Url classUrl;
        if ( classPath.length()==0)
        {
            classUrl=project.getPaths().getOutPath();
        }
        else
        {
            classUrl=new Url( classPath);
        }
        button.setEnabled( false);
        ( new UpdateDBThread( classUrl, button, lastModified, node)).start();
    }

    void setAnalyzerPath( BBQNode node, String newPath)
    {
        String testPath="";
        try
        {
            testPath=new File( newPath).getCanonicalPath();
            db.openDB( testPath);
        }
        catch ( Exception e)
        {
            try
            {
                db.openDB( goodPath);
            }
            catch ( Exception e2)
            {
                e2.printStackTrace();
                db=null;
            }
        }
        BBQPathsGroup.pathsProperty.setValue( node, newPath);
        goodPath=testPath;
    }

    class UpdateDBThread extends Thread
    {
        private Url classUrl;
        private JButton button;
        private long lastModified;
        private BBQNode node;

        UpdateDBThread( Url url, JButton b, long l, BBQNode n)
        {
            classUrl=url;
            button=b;
            lastModified=l;
            node=n;
        }

        public void run()
        {
            Stack directoryStack=new Stack();
            directoryStack.push( classUrl);
            Filesystem system=classUrl.getFilesystem();
            final long startTime=System.currentTimeMillis();
            try
            {
                while ( ! directoryStack.isEmpty())
                {
                    Url currentDirectory=(Url)directoryStack.pop();
                    Url[] contents=system.getChildren( currentDirectory, null,
                        Filesystem.TYPE_BOTH);
                    for ( int i=0; i<contents.length; i++)
                    {
                        if ( system.isDirectory( contents[i]))
                            directoryStack.push( contents[i]);
                        else
                        {
                            if ( contents[i].getFileExtension().
                                equals( "class") &&
                                system.getLastModified( contents[i])>
                                lastModified)
                            {
System.out.println( "Analyzing "+contents[i].toString());
                                BufferedInputStream bis=
                                    new BufferedInputStream(
                                    system.getInputStream( contents[i]));
                                DBClass.addClassToDB( new AnalyzeClass( bis),
                                    db);
                                bis.close();
                            }
                        }
                    }
                }
                db.closeDB();
                db.openDB( goodPath);
                EventQueue.invokeLater( new Runnable() {
                        public void run()
                        {
                            BBQPathsGroup.updateTimeProperty.setValue( node,
                                Long.toString( startTime));
                        }
                    });
            }
            catch ( Exception ioe)
            {
ioe.printStackTrace();
            }
            finally
            {
                EventQueue.invokeLater( new Runnable() {
                        public void run()
                        {
                            button.setEnabled( true);
                        }
                    });
            }
        }
    }
}