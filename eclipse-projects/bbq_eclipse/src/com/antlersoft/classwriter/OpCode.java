
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Collection;
import java.util.Stack;

public abstract class OpCode
{
	private int value;
	private String mnemonic;

	OpCode( int v, String m)
	{
	    value=v;
	    mnemonic=m;
	    if ( opCodes[v]!=null)
	    {
			throw new IllegalStateException();
	    }
	    opCodes[v]=this;
	}

 	public final String getMnemonic()
    {
        return mnemonic;
    }

	abstract Instruction read( InstructionPointer cr, byte[] code)
 		throws CodeCheckException;
 	abstract Stack stackUpdate( Instruction instruction, Stack current,
  		CodeAttribute attribute)
  		throws CodeCheckException;
    abstract void traverse( Instruction instruction, Collection next,
    	CodeAttribute attribute)
    	throws CodeCheckException;

    void fixDestinationAddress( Instruction instruction,
        int start, int oldPostEnd, int newPostEnd)
        throws CodeCheckException
    {
        if ( instruction.instructionStart>=oldPostEnd)
            instruction.instructionStart+=newPostEnd-oldPostEnd;
    }

 	void write( DataOutputStream out, Instruction instruction)
  		throws IOException
    {
    	out.writeByte( value);
        if ( instruction.operands!=null)
            out.write( instruction.operands);
    }

    static OpCode[] opCodes;

    static
    {
    	opCodes=new OpCode[256];
    }

    public static final int mU( byte b)
    {
		int retval=b;
		if ( retval<0)
		{
		    retval+=256;
		}
		return retval;
    }

    public static final void intToPair( int value, byte[] array, int offset)
    {
        array[offset+1]=(byte)(value&0xff);
        value>>>=8;
        array[offset]=(byte)value;
    }

    public static final void intToQuad( int value, byte[] array, int offset)
    {
        array[offset+3]=(byte)(value&0xff);
        value>>>=8;
        array[offset+2]=(byte)(value&0xff);
        value>>>=8;
        array[offset+1]=(byte)(value&0xff);
        value>>>=8;
        array[offset]=(byte)value;
    }

    public static final int pairToInt( byte[] array, int offset)
    {
        return (array[offset]<<8)|mU( array[offset+1]);
    }

    public static final int quadToInt( byte[] array, int offset)
    {
        return (array[offset]<<24)|( mU( array[offset+1])<<16)|
        	( mU( array[offset+2])<<8)|mU( array[offset+3]);
    }

    public static OpCode getOpCodeByMnemonic( String mnemonic)
        throws CodeCheckException
    {
    	for ( int i=0; i<256; i++)
     	{
      		if ( opCodes[i].mnemonic.equals( mnemonic))
        	{
         		return opCodes[i];
         	}
      	}
        throw new CodeCheckException( mnemonic+" not found");
    }

    static byte[] getSubArray( byte[] code, int offset, int length)
    	throws CodeCheckException
    {
        if ( length==0)
        	return null;
        byte[] result=new byte[length];
        if ( code.length<offset+length)
        	throw new CodeCheckException(
         	"Code segment is too short for instruction");
        for ( int i=0; i<length; i++)
        {
            result[i]=code[offset+i];
        }
        return result;
    }
}