
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

public class AnimalNode extends QuestionNode
{
    AnimalNode( String animal)
    {
        prompt=animal;
    }

    void ask(QuestionNode parent) throws java.io.IOException
    {
        if ( askQuestion( "Is the answer "+prompt+"? "))
        {
            System.out.println( "I knew it");
        }
        else
        {
            BinaryNode prevQuestion=(BinaryNode)parent;
            String newAnimal;
            String newQuestion;
            boolean questionYes;
            do
            {
                System.out.print( "I give up. What is your animal? ");
                newAnimal=input.readLine();
            }
            while ( ! askQuestion( "So you were thinking of "+newAnimal+"? "));
            do
            {
                System.out.println(
                    "Type a yes/no question that distinguishes a "+prompt);
                System.out.println( "from a "+newAnimal);
                newQuestion=input.readLine();
                questionYes=askQuestion( "Is the answer yes or no for a "+newAnimal+"? ");
            }
            while ( ! askQuestion( "To confirm: The answer to\n"+newQuestion+
                "\nis "+(questionYes ? "Yes" : "No")+" for the animal "+
                newAnimal+"? "));
            AnimalNode animalNode=new AnimalNode( newAnimal);
            BinaryNode questionNode=new BinaryNode( newQuestion,
                ( questionYes ? animalNode : this),
                ( questionYes ? this : animalNode));
            if ( prevQuestion.yesNode==this)
                prevQuestion.yesNode=questionNode;
            else
                prevQuestion.noNode=questionNode;
        }
    }
}