
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

import java.util.Collection;

class ReturnOpCode extends SimpleOpCode
{
	ReturnOpCode( int v, int l, String m, ProcessStack stack)
 	{
      	super( v, l, m, stack);
    }

    void traverse( Instruction instruction, Collection next,
    	CodeAttribute attribute)
    	throws CodeCheckException
    {
    }
}