
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

public class SimpleOpCode extends OpCode
{
	private int length;
 	private int stack_change;

	SimpleOpCode( int v, int l, String m, int sc)
	{
 		super( v, m);
	    length=l;
        stack_change=sc;
	}

 	SimpleOpCode( int v, int l, String m)
    {
    	this( v, l, m, 0);
    }

	Instruction read( InstructionPointer cr, byte[] code)
	{
     	int wideLength;
      	if ( cr.wide)
         	wideLength=2*(length-1)+1;
        else
            wideLength=length;
		Instruction result=new Instruction( this, cr.currentPos,
  			getSubArray( code, cr.currentPos+1, wideLength-1), cr.wide);
	    cr.currentPos+=wideLength;
     	return result;
	}
}