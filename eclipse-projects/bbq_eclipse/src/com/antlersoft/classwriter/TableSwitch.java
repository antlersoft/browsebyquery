
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
import java.util.Stack;

public class TableSwitch extends OpCode
{
	TableSwitch( int v, String m)
	{
	    super( v, m);
	}

 	Stack stackUpdate( Instruction instruction, Stack old_stack, CodeAttribute
 		attribute)
   		throws CodeCheckException
    {
		Stack new_stack=(Stack)old_stack.clone();
  		if ( new_stack.size()<1)
    	{
         	throw new CodeCheckException( "Stack too small in tableswitch");
        }
		if ( new_stack.pop()!=ProcessStack.CAT1)
  		{
        	throw new CodeCheckException( "tableswitch: stack operand is wrong size");
        }
        return new_stack;
    }

    void traverse( Instruction instruction, Collection next, CodeAttribute
    	attribute)
    	throws CodeCheckException
    {
        int offset=instruction.operands.length%4;
        next.add( new InstructionPointer( quadToInt( instruction.operands, offset)+
        	instruction.instructionStart));
        offset+=4;
        int lowend=quadToInt( instruction.operands, offset);
        offset+=4;
        int highend=quadToInt( instruction.operands, offset);
        offset+=4;
        for ( int i=lowend; i<=highend; i++)
        {
            next.add( new InstructionPointer( quadToInt( instruction.operands, offset+4*(i-lowend))+
            	instruction.instructionStart));
        }
    }

    void fixDestinationAddress( Instruction instruction,
        int start, int oldPostEnd, int newPostEnd)
        throws CodeCheckException
    {
        throw new CodeCheckException( "Unsupported address fix-up- tableswitch");
    }

 	Instruction read( InstructionPointer cr, byte[] code)
  		throws CodeCheckException
	{
	    cr.currentPos++;
     	int operandStart=cr.currentPos;
	    cr.currentPos+=(4-( cr.currentPos%4))%4;
	    cr.currentPos+=4;
	    int lowend=( mU(code[cr.currentPos++])<<24)|(mU(code[cr.currentPos++])<<16)|(mU(code[cr.currentPos++])<<8)|mU(code[cr.currentPos++]);
	    int highend=( mU(code[cr.currentPos++])<<24)|(mU(code[cr.currentPos++])<<16)|(mU(code[cr.currentPos++])<<8)|mU(code[cr.currentPos++]);
	    cr.currentPos+=4*(highend-lowend+1);
     	return new Instruction( this, operandStart-1, getSubArray( code,
      		operandStart, cr.currentPos-operandStart), false);
	}
}