
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *  List of exceptions that a method may throw; the index numbers
 * represents ClassInfo constants in the constant pool.  Found in
 * the attribute list of a FieldInfo that represents a method.
 */
class ExceptionsAttribute implements Attribute
{
	public final static String typeString="Exceptions";

	private ArrayList exceptions;

	ExceptionsAttribute( DataInputStream classStream)
	    throws IOException
	{
	    int exceptionCount=classStream.readUnsignedShort();
	    exceptions=new ArrayList( exceptionCount);
	    for ( int i=0; i<exceptionCount; i++)
	    {
			exceptions.add( new Integer( classStream.readUnsignedShort()));
	    }
	}

 	public ExceptionsAttribute( Collection integerCollection)
    {
        exceptions=new ArrayList( integerCollection.size());
        exceptions.addAll( integerCollection);
    }

	public String getTypeString() { return typeString; }

	public void write( DataOutputStream classStream)
		throws IOException
	{
 		classStream.writeShort( exceptions.size());
   		for ( Iterator i=exceptions.iterator(); i.hasNext();)
     	{
      		classStream.writeShort( ((Integer)i.next()).intValue());
      	}
	}
}
