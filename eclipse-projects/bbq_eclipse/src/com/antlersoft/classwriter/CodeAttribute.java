
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class CodeAttribute implements Attribute
{
	public final static String typeString="Code";

	int maxStack;
	int maxLocals;
	int codeLength;
	byte[] code;
	int exceptionCount;
	CodeException[] exceptions;
	AttributeList attributes;

	CodeAttribute( DataInputStream classStream, ClassWriter contains)
	    throws IOException
	{
	    maxStack=classStream.readUnsignedShort();
	    maxLocals=classStream.readUnsignedShort();
	    codeLength=classStream.readUnsignedShort()*65536+classStream.readUnsignedShort();
	    code=new byte[codeLength];
	    classStream.readFully( code);
	    exceptionCount=classStream.readUnsignedShort();
	    exceptions=new CodeException[exceptionCount];
	    int i;
	    for ( i=0; i<exceptionCount; i++)
		{
			exceptions[i]=new CodeException( classStream);
	    }
		attributes=new AttributeList( contains);
		attributes.read( classStream);
	}

	public String getTypeString() { return typeString; }
	public void write( DataOutputStream classStream)
 		throws IOException
   	{
   	}

	class CodeException
	{
		int start;
		int end;
		int handler;
		int catchType;

		CodeException( DataInputStream classStream)
		    throws IOException
		{
		    start=classStream.readUnsignedShort();
		    end=classStream.readUnsignedShort();
		    handler=classStream.readUnsignedShort();
		    catchType=classStream.readUnsignedShort();
		}
	}
}