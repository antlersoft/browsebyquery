
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

public class Instruction
{
	OpCode opCode;
 	int instructionStart;
	byte[] operands;
 	boolean wideFlag;

    public Instruction( OpCode o, int is, byte[] ops, boolean wide)
    {
    	opCode=o;
     	instructionStart=is;
     	operands=ops;
      	wideFlag=wide;
    }

    public int getLength()
    {
        if ( operands==null)
        	return 1;
        return operands.length+1;
    }

    /**
     * Interpret operands as symbolic reference
     */
    ClassWriter.CPTypeRef getSymbolicReference( ClassWriter writer)
    	throws CodeCheckException
    {
        return (ClassWriter.CPTypeRef)
        	writer.constantPool.get( OpCode.pairToInt(
         	operands, 0));
    }

    /**
     * Interpret operands as destination opcode address
     */
    int getOffsetDestination()
    {
        int offset;
        if ( operands.length==4)
        {
            offset=OpCode.quadToInt( operands, 0);
        }
        else
        	offset=OpCode.pairToInt( operands, 0);
        return instructionStart+offset;
    }
}