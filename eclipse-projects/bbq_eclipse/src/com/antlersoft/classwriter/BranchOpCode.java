
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

class BranchOpCode extends SimpleOpCode
{
	BranchOpCode( int v, int l, String m, int params)
 	{
  		super( v, l, m, new Cat1Stack( params, 0));
    }

    void traverse(Instruction instruction, Collection next,
    	CodeAttribute attribute)
    	throws classwriter.CodeCheckException
    {
        super.traverse( instruction, next, attribute);
        next.add( new InstructionPointer( instruction.getOffsetDestination()));
    }

    void fixDestinationAddress( Instruction instruction,
        int start, int oldPostEnd, int newPostEnd)
        throws CodeCheckException
    {
        instruction.fixDestinationAddress( start, oldPostEnd, newPostEnd);
    }
}