
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

public class TableSwitch extends OpCode
{
	TableSwitch( int v, String m)
	{
	    super( v, m);
	}

 	Instruction read( InstructionPointer cr, byte[] code)
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