
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

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

    /**
     *  Update offset destination
     */
    void fixDestinationAddress( int start, int oldPostEnd, int newPostEnd)
        throws CodeCheckException
    {
        int oldDestination=getOffsetDestination();
        if ( instructionStart>=oldPostEnd)
        {
            instructionStart+=newPostEnd-oldPostEnd;
        }
        if ( oldDestination>start && oldDestination<oldPostEnd)
        {
            throw new CodeCheckException(
                "Branch into code replaced by an inserted segment");
        }
        if ( oldDestination>=oldPostEnd)
        {
            int newDestination=oldDestination+newPostEnd-oldPostEnd-
                instructionStart;
            if ( operands.length==4)
                OpCode.intToQuad( newDestination, operands, 0);
            else
                OpCode.intToPair( newDestination, operands, 0);
        }
    }

    public static Instruction appropriateLdc( int index, boolean wide)
        throws CodeCheckException
    {
        byte[] operands;
        String mnemonic;

        if ( wide)
        {
            mnemonic="ldc2_w";
            operands=new byte[2];
            OpCode.intToPair( index, operands, 0);
        }
        else
        {
            if ( index<256)
            {
                mnemonic="ldc";
                operands=new byte[1];
                operands[0]=(byte)index;
            }
            else
            {
                mnemonic="ldc_w";
                operands=new byte[2];
                OpCode.intToPair( index, operands, 0);
            }
        }
        return new Instruction( OpCode.getOpCodeByMnemonic( mnemonic), 0,
            operands, false);
    }
}