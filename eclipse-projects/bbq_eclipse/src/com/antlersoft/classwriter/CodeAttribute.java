
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.util.ArrayList;
import java.util.Iterator;

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
	ArrayList exceptions;
	AttributeList attributes;

	CodeAttribute( DataInputStream classStream, ClassWriter contains)
	    throws IOException
	{
	    maxStack=classStream.readUnsignedShort();
	    maxLocals=classStream.readUnsignedShort();
	    codeLength=classStream.readUnsignedShort()*65536+classStream.readUnsignedShort();
	    code=new byte[codeLength];
	    classStream.readFully( code);
	    int exceptionCount=classStream.readUnsignedShort();
	    exceptions=new ArrayList( exceptionCount);
	    int i;
	    for ( i=0; i<exceptionCount; i++)
		{
			exceptions.add( new CodeException( classStream));
	    }
		attributes=new AttributeList( contains);
		attributes.read( classStream);
	}

	public String getTypeString() { return typeString; }

	public void write( DataOutputStream classStream)
 		throws IOException
   	{
    	classStream.writeShort( maxStack);
     	classStream.writeShort( maxLocals);
      	classStream.writeShort( codeLength >>16);
       	classStream.writeShort( codeLength & 0xffff);
        classStream.write( code);
        classStream.writeShort( exceptions.size());
        for ( Iterator i=exceptions.iterator(); i.hasNext();)
        {
        	((CodeException)i.next()).write( classStream);
        }
        attributes.write( classStream);
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

  		void write( DataOutputStream classStream)
    		throws IOException
    	{
     		classStream.writeShort( start);
       		classStream.writeShort( end);
         	classStream.writeShort( handler);
          	classStream.writeShort( catchType);
     	}
	}
}