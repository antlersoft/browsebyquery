
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter.test;

import java.io.File;
import com.antlersoft.odb.transp.TransparentDB;
import com.antlersoft.odb.DiskAllocatorStore;

public class TranspBase implements QuestionBase
{
    private TransparentDB db;

    public QuestionNode getTopNode()
    {
        QuestionNode result=(QuestionNode)db.getRootObject( "Top");
        if ( result==null)
        {
            AnimalNode no=new AnimalNode( "squirrel");
            AnimalNode yes=new AnimalNode( "moose");
            result=new BinaryNode( "Does it have antlers?", yes, no);
            db.makeRootObject( "Top", result);
            db.commit();
        }
        return result;
    }

    public void openDB(String[] args)
    {
        db=new TransparentDB( new DiskAllocatorStore( new File( args[1])));
    }

    public void closeDB()
    {
        //TODO: Implement this com.antlersoft.classwriter.test.QuestionBase method
        db.commit();
    }

    public void commit()
    {
        //TODO: Implement this com.antlersoft.classwriter.test.QuestionBase method
        db.commit();
    }
}