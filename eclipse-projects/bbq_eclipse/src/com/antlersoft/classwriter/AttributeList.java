
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class AttributeList implements Cloneable
{
	private ArrayList attributes;
 	private ClassWriter containing;

 	public AttributeList( ClassWriter contains)
  	{
   		attributes=null;
     	containing=contains;
   	}

    public Collection getAttributes()
    {
    	return (Collection)attributes.clone();
    }

    public Attribute getAttributeByType( String type)
    {
    	for ( Iterator i=attributes.iterator(); i.hasNext();)
        {
        	Attribute next=(Attribute)i.next();
         	if ( next.getTypeString().equals( type))
            {
            	return next;
            }
        }
        return null;
    }

    public void read( DataInputStream classStream)
    	throws IOException
    {
   	    int attributesCount=classStream.readUnsignedShort();
	    attributes=new ArrayList( attributesCount);
	    for ( int i=0; i<attributesCount; i++)
	    {
			attributes.add( readAttribute( classStream));
	    }
	}

	private Attribute readAttribute( DataInputStream classStream)
	    throws IOException
	{
	    int nameIndex=classStream.readUnsignedShort();
	    int length=classStream.readInt();
	    String type=containing.getString( nameIndex);
     	Attribute value;
/*
	    if ( type.equals( "SourceFile"))
	    {
			value=getString(classStream.readUnsignedShort());
	    }
	    else if ( type.equals( "ConstantValue"))
	    {
			value=constantPool[classStream.readUnsignedShort()];
	    }
	    else */ if ( type.equals( CodeAttribute.typeString))
	    {
			value=new CodeAttribute( classStream, containing);
	    }
	    else if ( type.equals( ExceptionsAttribute.typeString))
	    {
			value=new ExceptionsAttribute( classStream);
	    }
	    else if ( type.equals( LineNumberTableAttribute.typeString))
	    {
			value=new LineNumberTableAttribute( classStream);
	    }
	    /* Local variable table unimplemented as of now
	    else if ( type.equals( "LocalVariableTable"))
	    {
			value=new LocalVariableTableAttribute( classStream);
	    }
	    * End of unimplemented type */
	    else
	    {
			/* Unknown type -- pass through silently */
   			value=new UnknownAttribute( length, type, classStream);
	    }

     	return value;
	}

 	public void write( DataOutputStream classStream)
  		throws IOException
    {
    	classStream.writeShort( attributes.size());
     	for ( Iterator i=attributes.iterator(); i.hasNext();)
      	{
       		Attribute attribute=(Attribute)i.next();
	 		classStream.writeShort( containing.findStringIndex(
    			attribute.getTypeString()));
			ByteArrayOutputStream byteStream=new ByteArrayOutputStream();

	  		attribute.write( new DataOutputStream( byteStream));
	    	byte[] valueBytes=byteStream.toByteArray();
	     	classStream.writeInt( valueBytes.length);
	    	classStream.write( valueBytes);
        }
    }
}