
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

public interface QuestionBase
{
    public abstract QuestionNode getTopNode();
    public abstract void openDB( String[] args);
    public abstract void closeDB();
    public void commit();
}