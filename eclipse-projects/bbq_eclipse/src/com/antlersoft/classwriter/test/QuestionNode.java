
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * <p>Copyright (c) 2000, 2003  Michael A. MacDonald<p>
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