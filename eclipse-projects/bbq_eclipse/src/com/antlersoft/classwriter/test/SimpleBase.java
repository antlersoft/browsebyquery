
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

public class SimpleBase implements QuestionBase
{
    private QuestionNode topNode;

    public SimpleBase()
    {
    }

    public QuestionNode getTopNode()
    {
        return topNode;
    }

    public void openDB(String[] args)
    {
        AnimalNode no=new AnimalNode( "squirrel");
        AnimalNode yes=new AnimalNode( "moose");
        topNode=new BinaryNode( "Does it have antlers?", yes, no);
    }

    public void closeDB()
    {

    }

    public void commit()
    {

    }
}