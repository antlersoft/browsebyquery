
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

import java.util.Stack;

class ComboStack implements ProcessStack
{
	private ProcessStack first;
 	private ProcessStack second;

	ComboStack( ProcessStack ps1, ProcessStack ps2)
 	{
		first=ps1;
  		second=ps2;
  	}

    public Stack stackUpdate( Stack old_stack)
        throws CodeCheckException
    {
        return second.stackUpdate( first.stackUpdate( old_stack));
    }
}