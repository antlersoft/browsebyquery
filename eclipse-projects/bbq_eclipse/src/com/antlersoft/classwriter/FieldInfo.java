
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

public class FieldInfo
{
	int accessFlags;
	int nameIndex;
	int descriptorIndex;
 	AttributeList attributes;

	FieldInfo( DataInputStream classStream, ClassWriter contains)
	    throws IOException
	{
	    accessFlags=classStream.readUnsignedShort();
	    nameIndex=classStream.readUnsignedShort();
	    descriptorIndex=classStream.readUnsignedShort();
     	attributes=new AttributeList( contains);
      	attributes.read( classStream);
	}

    FieldInfo( int flags, String name, String descriptor, ClassWriter contains)
    {
        nameIndex=contains.getStringIndex( name);
        descriptorIndex=contains.getStringIndex( descriptor);
        accessFlags=flags;
        attributes=new AttributeList( contains);
    }

    public String getName()
    {
        return attributes.getCurrentClass().getString( nameIndex);
    }

    public String getType()
    {
        return attributes.getCurrentClass().getString( descriptorIndex);
    }

    public void setType( String newType)
    {
        descriptorIndex=attributes.getCurrentClass().getStringIndex( newType);
    }

	void write( DataOutputStream classStream)
		throws IOException
	{
   		classStream.writeShort( accessFlags);
     	classStream.writeShort( nameIndex);
      	classStream.writeShort( descriptorIndex);
        attributes.write( classStream);
   	}
}