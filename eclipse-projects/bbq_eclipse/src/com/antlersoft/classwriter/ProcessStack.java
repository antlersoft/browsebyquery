
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.util.Stack;

interface ProcessStack
{
	final static Object CAT1=new Object();
 	final static Object CAT2=new Object();

	public Stack stackUpdate( Stack old_stack)
 		throws CodeCheckException;
}