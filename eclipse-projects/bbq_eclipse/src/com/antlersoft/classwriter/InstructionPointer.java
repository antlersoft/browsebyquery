
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

class InstructionPointer
{
	int currentPos;
    boolean wide;

    public InstructionPointer( int p)
    {
    	currentPos=p;
        wide=false;
    }
}