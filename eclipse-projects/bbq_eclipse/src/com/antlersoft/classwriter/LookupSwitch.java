
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

class LookupSwitch extends OpCode
{
	LookupSwitch( int v, String m)
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
         	throw new CodeCheckException( "Stack too small in lookupswitch");
        }
		if ( new_stack.pop()!=ProcessStack.CAT1)
  		{
        	throw new CodeCheckException( "lookupswitch: stack operand is wrong size");
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
        int npairs=quadToInt( instruction.operands, offset);
        offset+=8;
        for ( int i=0; i<npairs; i++)
        {
            next.add( new InstructionPointer( quadToInt( instruction.operands, offset+8*i)+
            	instruction.instructionStart));
        }
    }

	Instruction read( InstructionPointer cr, byte[] code)
 		throws CodeCheckException
	{
     	try
      	{
		    cr.currentPos++;
	     	int operandStart=cr.currentPos;
		    cr.currentPos+=(4-( cr.currentPos%4))%4;
		    cr.currentPos+=4;
		    int npairs=( mU(code[cr.currentPos++])<<24)|(mU(code[cr.currentPos++])<<16)|(mU(code[cr.currentPos++])<<8)|mU(code[cr.currentPos++]);
		    cr.currentPos+=8*npairs;
	     	return new Instruction( this, operandStart-1,
	      		getSubArray( code, operandStart,
	        	cr.currentPos-operandStart), false);
        }
        catch ( ArrayIndexOutOfBoundsException bounds)
        {
            throw new CodeCheckException( "Code segment too short for lookupswitch");
        }
	}
}