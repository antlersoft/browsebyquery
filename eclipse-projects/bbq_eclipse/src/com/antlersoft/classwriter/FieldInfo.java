
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

	void write( DataOutputStream classStream)
		throws IOException
	{
   		classStream.writeShort( accessFlags);
     	classStream.writeShort( nameIndex);
      	classStream.writeShort( descriptorIndex);
        attributes.write( classStream);
   	}
}