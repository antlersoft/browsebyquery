
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
import com.borland.primetime.node.ProjectAdapter;

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.ObjectAnalyzeDB;
import com.antlersoft.analyzer.query.QueryParser;

class ProjectAnalyzer extends ProjectAdapter
{
    JBProject project;
    ObjectAnalyzeDB db;
    QueryParser qp;

    ProjectAnalyzer( JBProject toAnalyze)
        throws Exception
    {
System.out.println( "<init>ProjectAnalyzer");
        project=toAnalyze;
        db=new ObjectAnalyzeDB();
        db.openDB( new File( project.getUrl().getFileObject().getParent(),
            "analyzer.pj").getCanonicalPath());
        qp=new QueryParser();
    }

    void updateDB()
        throws Exception
    {
System.out.println( project.getPaths().getOutPath().getFileObject().getCanonicalPath());
        DBClass.addFileToDB( project.getPaths().getOutPath().getFileObject(),
            db);
        db.closeDB();
        db.openDB( new File( project.getUrl().getFileObject().getParent(),
            "analyzer.pj").getCanonicalPath());
    }
}