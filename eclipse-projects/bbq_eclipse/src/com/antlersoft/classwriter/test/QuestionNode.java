
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Serializable;

public abstract class QuestionNode implements Serializable
{
    static BufferedReader input=new BufferedReader( new InputStreamReader(
        System.in));
    static boolean askQuestion( String question)
        throws IOException
    {
        for (;;)
        {
            System.out.print( question);
            String line=input.readLine();
            line=line.toUpperCase();
            if ( line.startsWith( "Y"))
                return true;
            if ( line.startsWith( "N"))
                return false;
        }
    }

    public static void main( String[] args)
        throws Exception
    {
        QuestionBase base=(QuestionBase)
            Class.forName( args[0]).getConstructor( new Class[0]).newInstance(
            new Object[0]);
        base.openDB( args);
        do
        {
            base.getTopNode().ask( null);
            base.commit();
        }
        while ( askQuestion( "Try again? "));
        base.closeDB();
    }

    String prompt;

    abstract void ask( QuestionNode parent)
        throws IOException;
}