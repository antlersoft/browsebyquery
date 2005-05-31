
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