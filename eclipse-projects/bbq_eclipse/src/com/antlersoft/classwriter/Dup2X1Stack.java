
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.util.EmptyStackException;
import java.util.Stack;

public class Dup2X1Stack implements ProcessStack
{

    public Dup2X1Stack()
    {
    }

    public Stack stackUpdate(Stack old_stack) throws CodeCheckException
    {
        try
        {
	        Stack new_stack=(Stack)old_stack.clone();
	        Object top1=new_stack.pop();
	        if ( top1==ProcessStack.CAT1)
         	{
              	Object top2=new_stack.pop();
                if ( top2!=ProcessStack.CAT1)
	        		throw new CodeCheckException( "dup2_x1; second object is not CAT1");
           		Object top3=new_stack.pop();
             	if ( top3!=ProcessStack.CAT1)
              		throw new CodeCheckException( "dup2_x1; third object is not CAT1");
           		new_stack.push( top2);
             	new_stack.push( top1);
              	new_stack.push( top3);
              	new_stack.push( top2);
               	new_stack.push( top1);
            }
            else
            {
                Object top2=new_stack.pop();
                if ( top2!=ProcessStack.CAT1)
                    throw new CodeCheckException( "dup2_x1; object under CAT2 is not CAT1");
                new_stack.push( top1);
                new_stack.push( top2);
                new_stack.push( top1);
            }
         	return new_stack;
    	}
     	catch ( EmptyStackException ese)
      	{
             throw new CodeCheckException( "dup2_x1; stack not deep enough");
        }
    }
}