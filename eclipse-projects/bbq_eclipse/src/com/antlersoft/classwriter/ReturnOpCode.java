
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.util.Collection;

class ReturnOpCode extends SimpleOpCode
{
	ReturnOpCode( int v, int l, String m, Object return_type)
 	{
      	super( v, l, m, new HomogStack( return_type, 1, 0));
    }

    void traverse( Instruction instruction, Collection next)
    	throws CodeCheckException
    {
    }
}