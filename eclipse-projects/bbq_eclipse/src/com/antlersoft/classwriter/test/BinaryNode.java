
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

public class BinaryNode extends QuestionNode
{
    QuestionNode yesNode;
    QuestionNode noNode;

    public BinaryNode( String question, QuestionNode yes, QuestionNode no)
    {
        prompt=question;
        yesNode=yes;
        noNode=no;
    }

    void ask(QuestionNode parent) throws java.io.IOException
    {
        if ( askQuestion( prompt))
            yesNode.ask( this);
        else
            noNode.ask( this);
    }
}