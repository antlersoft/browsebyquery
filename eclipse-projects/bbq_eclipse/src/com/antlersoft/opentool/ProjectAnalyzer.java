
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

import java.io.File;

import com.borland.jbuilder.node.JBProject;

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

    void updateDB()
        throws Exception
    {
System.out.println( project.getPaths().getOutPath().getFileObject().getCanonicalPath());
        DBClass.addFileToDB( project.getPaths().getOutPath().getFileObject(),
            db);
        db.closeDB();
        db.openDB( goodPath);
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
}