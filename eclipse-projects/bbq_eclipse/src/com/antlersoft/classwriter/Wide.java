
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

/**
 *  Needs no special processing (other than SimpleOpCode) but a separate
 *  class to mark that next opcode made need special handling
 */
public class Wide extends SimpleOpCode
{
	Wide( int v, String m)
 	{
        super( v, 1, m, 0);
    }
}