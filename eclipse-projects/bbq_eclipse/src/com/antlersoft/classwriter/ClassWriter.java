
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
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Iterator;

public class ClassWriter implements Cloneable
{
    int magic;
    int majorVersion;
    int minorVersion;
    int accessFlags;
    int thisClassIndex;
    int superClassIndex;
	ArrayList constantPool;
	ArrayList interfaces;
    ArrayList fields;
    ArrayList methods;
	AttributeList attributes;

    public static final short CONSTANT_Utf8=1;
    public static final short CONSTANT_Integer=3;
    public static final short CONSTANT_Float=4;
    public static final short CONSTANT_Long=5;
    public static final short CONSTANT_Double=6;
    public static final short CONSTANT_Class=7;
    public static final short CONSTANT_String=8;
    public static final short CONSTANT_Fieldref=9;
    public static final short CONSTANT_Methodref=10;
    public static final short CONSTANT_InterfaceMethodref=11;
    public static final short CONSTANT_NameAndType=12;

    public static final int ACC_PUBLIC=0x0001;
    public static final int ACC_PRIVATE=0x0002;
    public static final int ACC_PROTECTED=0x0004;
    public static final int ACC_STATIC=0x0008;
    public static final int ACC_FINAL=0x0010;
    public static final int ACC_SUPER=0x0020;
    public static final int ACC_VOLATILE=0x0040;
    public static final int ACC_TRANSIENT=0x0080;
    public static final int ACC_INTERFACE=0x0200;
    public static final int ACC_ABSTRACT=0x0400;

    public ClassWriter()
    {
        clearClass();
    }

    public void readClass( InputStream is)
		throws IOException, CodeCheckException
    {
        clearClass();

		DataInputStream classStream=new DataInputStream( is);

		magic=classStream.readInt();
		majorVersion=classStream.readUnsignedShort();
		minorVersion=classStream.readUnsignedShort();
  		constantPool.add( null);
		int constantPoolCount=classStream.readUnsignedShort();
  		int i;
		for ( i=1; i<constantPoolCount; i++)
		{
  			CPInfo poolEntry=readConstant( classStream);
		    constantPool.add( poolEntry);
		    if ( poolEntry.tag==CONSTANT_Double ||
				poolEntry.tag==CONSTANT_Long)
    		{
				i++;
    			constantPool.add( null);
    		}
		}
		accessFlags=classStream.readUnsignedShort();
		thisClassIndex=classStream.readUnsignedShort();
		superClassIndex=classStream.readUnsignedShort();
		int interfacesCount=classStream.readUnsignedShort();
		for ( i=0; i<interfacesCount; i++)
		{
		    interfaces.add( new Integer( classStream.readUnsignedShort()));
		}
		int fieldsCount=classStream.readUnsignedShort();
		for ( i=0; i<fieldsCount; i++)
		{
		    fields.add( readFieldInfo( classStream));
		}
		int methodsCount=classStream.readUnsignedShort();
		for ( i=0; i<methodsCount; i++)
		{
		    methods.add( readMethodInfo( classStream));
		}
  		attributes.read( classStream);
    }

    public void writeClass( OutputStream is)
		throws IOException
    {
		DataOutputStream classStream=new DataOutputStream( is);

		classStream.writeInt( magic);
  		classStream.writeShort( majorVersion);
    	classStream.writeShort( minorVersion);
     	classStream.writeShort( constantPool.size());
  		Iterator i=constantPool.iterator();
    	i.next();	// Skip initial, not really there entry
		for ( ; i.hasNext();)
		{
  			CPInfo poolEntry=(CPInfo)i.next();
  			poolEntry.write( classStream);
     		// Constant pool array must have null entry after long or double
		    if ( poolEntry.tag==CONSTANT_Double ||
				poolEntry.tag==CONSTANT_Long)
				i.next();
		}
		classStream.writeShort( accessFlags);
		classStream.writeShort( thisClassIndex);
		classStream.writeShort( superClassIndex);
  		classStream.writeShort( interfaces.size());
		for ( i=interfaces.iterator(); i.hasNext();)
		{
		    classStream.writeShort( ((Integer)i.next()).intValue());
		}
  		classStream.writeShort( fields.size());
		for ( i=fields.iterator(); i.hasNext();)
		{
		    ((FieldInfo)i.next()).write( classStream);
		}
  		classStream.writeShort( methods.size());
		for ( i=methods.iterator(); i.hasNext();)
		{
		    ((FieldInfo)i.next()).write( classStream);
		}
  		attributes.write( classStream);
    }

    private void clearClass()
    {
    	constantPool=new ArrayList();
     	interfaces=new ArrayList();
      	fields=new ArrayList();
        methods=new ArrayList();
        attributes=new AttributeList( this);
    }

    CPInfo readConstant( DataInputStream classStream)
		throws IOException, CodeCheckException
    {
		short tag=(short)classStream.readUnsignedByte();
		CPInfo result;
		switch ( tag)
		{
		    case CONSTANT_Utf8 :
				result=new CPUtf8( classStream);
				break;
		    case CONSTANT_Integer :
				result=new CPInteger( classStream);
				break;
		    case CONSTANT_Float :
				result=new CPFloat( classStream);
				break;
		    case CONSTANT_Long :
				result=new CPLong( classStream);
				break;
		    case CONSTANT_Double :
				result=new CPDouble( classStream);
				break;
		    case CONSTANT_Class :
				result=new CPClass( classStream);
				break;
		    case CONSTANT_String :
				result=new CPString( classStream);
				break;
		    case CONSTANT_Fieldref :
		    case CONSTANT_Methodref :
		    case CONSTANT_InterfaceMethodref :
				result=new CPTypeRef( tag, classStream);
				break;
		    case CONSTANT_NameAndType :
				result=new CPNameAndType( classStream);
				break;
		    default :
				throw new CodeCheckException( "Unknown constant type "+(int)tag);
		}
		return result;
    }

	class CPInfo
	{
		short tag;
		CPInfo( short t)
		{
			tag=t;
		}
		void write( DataOutputStream classStream)
			throws IOException
		{
			classStream.writeByte( tag);
		}
	}

    class CPUtf8 extends CPInfo
    {
		String value;
		CPUtf8( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_Utf8);
      		value=classStream.readUTF();
		}
  		void write( DataOutputStream classStream)
    		throws IOException
    	{
     		super.write( classStream);
       		classStream.writeUTF( value);
     	}
    }

    public final String getString( int index)
    {
		return ((CPUtf8)constantPool.get(index)).value;
    }

    public final int findStringIndex( String toFind)
    {
    	int index=0;
     	for ( Iterator i=constantPool.iterator(); i.hasNext(); index++)
        {
        	Object constant=i.next();
         	if ( constant!=null && constant instanceof CPUtf8)
          	{
           		if ( ((CPUtf8)constant).value.equals( toFind))
             		return index;
           	}
        }

        return -1;
    }

    public String getClassName( int classIndex)
    {
		char[] nameBuffer=getString( ((CPClass)constantPool.
  			get( classIndex)).nameIndex).toCharArray();
		for ( int i=0; i<nameBuffer.length; i++)
		{
		    if ( nameBuffer[i]=='$' || nameBuffer[i]=='/')
				nameBuffer[i]='.';
		}
		return new String( nameBuffer);
    }

    class CPTypeRef extends CPInfo
    {
		int classIndex;
		int nameAndTypeIndex;

		CPTypeRef( short t, DataInputStream classStream)
		    throws IOException
		{
		    super( t);
		    classIndex=classStream.readUnsignedShort();
		    nameAndTypeIndex=classStream.readUnsignedShort();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
    	{
     		super.write( classStream);
       		classStream.writeShort( classIndex);
         	classStream.writeShort( nameAndTypeIndex);
        }
    }

    class CPNameAndType extends CPInfo
    {
		int nameIndex;
		int descriptorIndex;

		CPNameAndType( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_NameAndType);
		    nameIndex=classStream.readUnsignedShort();
		    descriptorIndex=classStream.readUnsignedShort();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeShort( nameIndex);
          	classStream.writeShort( descriptorIndex);
        }
    }

    class CPClass extends CPInfo
    {
		int nameIndex;
		CPClass( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_Class);
		    nameIndex=classStream.readUnsignedShort();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeShort( nameIndex);
        }
    }

    class CPString extends CPInfo
    {
		int valueIndex;
		CPString( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_String);
		    valueIndex=classStream.readUnsignedShort();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeShort( valueIndex);
        }
    }

    class CPInteger extends CPInfo
    {
		int value;
		CPInteger( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_Integer);
		    value=classStream.readInt();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeInt( value);
        }
    }

    class CPFloat extends CPInfo
    {
		float value;
		CPFloat( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_Float);
		    value=classStream.readFloat();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeFloat( value);
        }
    }

    class CPLong extends CPInfo
    {
		long value;
		CPLong( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_Long);
		    value=classStream.readLong();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeLong( value);
        }
    }

    class CPDouble extends CPInfo
    {
		double value;
		CPDouble( DataInputStream classStream)
		    throws IOException
		{
		    super( ClassWriter.CONSTANT_Double);
		    value=classStream.readDouble();
		}

  		void write( DataOutputStream classStream)
    		throws IOException
      	{
       		super.write( classStream);
         	classStream.writeDouble( value);
        }
    }

    private FieldInfo readFieldInfo( DataInputStream classStream)
    	throws IOException
    {
    	return new FieldInfo( classStream, this);
    }

    private MethodInfo readMethodInfo( DataInputStream classStream)
    	throws IOException
    {
    	return new MethodInfo( classStream, this);
    }

}
