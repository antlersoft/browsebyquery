
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

public class Dup2Stack implements ProcessStack
{
    public Dup2Stack()
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
	        		throw new CodeCheckException( "dup2; second object is not CAT1");
           		new_stack.push( top2);
             	new_stack.push( top1);
              	new_stack.push( top2);
               	new_stack.push( top1);
            }
            else
            {
                new_stack.push( top1);
                new_stack.push( top1);
            }
         	return new_stack;
    	}
     	catch ( EmptyStackException ese)
      	{
             throw new CodeCheckException( "dup2; stack not deep enough");
        }
    }
}