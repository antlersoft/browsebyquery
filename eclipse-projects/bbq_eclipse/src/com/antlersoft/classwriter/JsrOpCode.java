
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

class JsrOpCode extends SimpleOpCode
{
	JsrOpCode( int v, int l, String m)
 	{
  		super( v, l, m, new Cat1Stack( 0, 0));
    }

    void fixDestinationAddress( Instruction instruction,
        int start, int oldPostEnd, int newPostEnd)
        throws CodeCheckException
    {
        instruction.fixDestinationAddress( start, oldPostEnd, newPostEnd);
    }
}