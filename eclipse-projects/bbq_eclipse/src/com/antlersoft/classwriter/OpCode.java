
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
 	abstract Stack stackUpdate( Instruction instruction, Stack current)
  		throws CodeCheckException;
    abstract void traverse( Instruction instruction, Collection next)
    	throws CodeCheckException;

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

    static OpCode getOpCodeByMnemonic( String mnemonic)
    {
    	for ( int i=0; i<256; i++)
     	{
      		if ( opCodes[i].mnemonic.equals( mnemonic))
        	{
         		return opCodes[i];
         	}
      	}
        throw new IllegalStateException( mnemonic);
    }

    static byte[] getSubArray( byte[] code, int offset, int length)
    {
        if ( length==0)
        	return null;
        byte[] result=new byte[length];
        for ( int i=0; i<length; i++)
        {
            result[i]=code[offset+i];
        }
        return result;
    }
}