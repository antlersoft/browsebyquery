
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

class ExceptionsAttribute implements Attribute
{
	public final static String typeString="Exceptions";

	int exceptionCount;
	int[] exceptions;

	ExceptionsAttribute( DataInputStream classStream)
	    throws IOException
	{
	    exceptionCount=classStream.readUnsignedShort();
	    exceptions=new int[exceptionCount];
	    for ( int i=0; i<exceptionCount; i++)
	    {
			exceptions[i]=classStream.readUnsignedShort();
	    }
	}

	public String getTypeString() { return typeString; }
	public void write( DataOutputStream classStream)
		throws IOException
	{
	}
}
