
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

public class WSimpleOpCode extends SimpleOpCode
{
	WSimpleOpCode( int v, int l, String m, ProcessStack stacker)
	{
 		super( v, l, m, stacker);
	}

 	WSimpleOpCode( int v, int l, String m)
    {
    	super( v, l, m);
    }

    boolean isValidOperandLength( int len, boolean wide)
    {
        int operand_length=getDefaultLength()-1;
        if ( wide)
            operand_length*=2;
        return len==operand_length;
    }

	Instruction read( InstructionPointer cr, byte[] code)
 		throws CodeCheckException
	{
     	int wideLength;
      	if ( cr.wide)
         	wideLength=2*(getDefaultLength()-1)+1;
        else
            wideLength=getDefaultLength();
		Instruction result=new Instruction( this, cr.currentPos,
  			getSubArray( code, cr.currentPos+1, wideLength-1), cr.wide);
	    cr.currentPos+=wideLength;
     	return result;
	}
}