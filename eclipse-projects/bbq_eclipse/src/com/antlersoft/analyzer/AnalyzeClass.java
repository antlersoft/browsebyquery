package analyzer;

import java.io.InputStream;
import java.io.IOException;
import java.io.DataInputStream;

class AnalyzeClass
{
    int magic;
    int majorVersion;
    int minorVersion;
    int constantPoolCount;
    CPInfo[] constantPool;
    int accessFlags;
    int thisClassIndex;
    int superClassIndex;
    int interfacesCount;
    int[] interfaces;
    int fieldsCount;
    FieldInfo[] fields;
    int methodsCount;
    FieldInfo[] methods;
    int attributesCount;
    AttributeInfo[] attributes;

    static final short CONSTANT_Utf8=1;
    static final short CONSTANT_Integer=3;
    static final short CONSTANT_Float=4;
    static final short CONSTANT_Long=5;
    static final short CONSTANT_Double=6;
    static final short CONSTANT_Class=7;
    static final short CONSTANT_String=8;
    static final short CONSTANT_Fieldref=9;
    static final short CONSTANT_Methodref=10;
    static final short CONSTANT_InterfaceMethodref=11;
    static final short CONSTANT_NameAndType=12;

    static final int ACC_PUBLIC=0x0001;
    static final int ACC_PRIVATE=0x0002;
    static final int ACC_PROTECTED=0x0004;
    static final int ACC_STATIC=0x0008;
    static final int ACC_FINAL=0x0010;
    static final int ACC_SUPER=0x0020;
    static final int ACC_VOLATILE=0x0040;
    static final int ACC_TRANSIENT=0x0080;
    static final int ACC_INTERFACE=0x0200;
    static final int ACC_ABSTRACT=0x0400;

    AnalyzeClass( InputStream is)
		throws IOException, IllegalStateException
    {
		DataInputStream classStream=new DataInputStream( is);

		magic=classStream.readInt();
		majorVersion=classStream.readUnsignedShort();
		minorVersion=classStream.readUnsignedShort();
		constantPoolCount=classStream.readUnsignedShort();
		constantPool=new CPInfo[constantPoolCount];
		for ( int i=1; i<constantPoolCount; i++)
		{
		    constantPool[i]=readConstant( classStream);
		    if ( constantPool[i].tag==CONSTANT_Double ||
				constantPool[i].tag==CONSTANT_Long)
				i++;
		}
		accessFlags=classStream.readUnsignedShort();
		thisClassIndex=classStream.readUnsignedShort();
		superClassIndex=classStream.readUnsignedShort();
		interfacesCount=classStream.readUnsignedShort();
		interfaces=new int[interfacesCount];
		int i;
		for ( i=0; i<interfacesCount; i++)
		{
		    interfaces[i]=classStream.readUnsignedShort();
		}
		fieldsCount=classStream.readUnsignedShort();
		fields=new FieldInfo[fieldsCount];
		for ( i=0; i<fieldsCount; i++)
		{
		    fields[i]=new FieldInfo( classStream);
		}
		methodsCount=classStream.readUnsignedShort();
		methods=new FieldInfo[methodsCount];
		for ( i=0; i<methodsCount; i++)
		{
		    methods[i]=new FieldInfo( classStream);
		}
		attributesCount=classStream.readUnsignedShort();
		attributes=new AttributeInfo[attributesCount];
		for ( i=0; i<attributesCount; i++)
		{
		    attributes[i]=new AttributeInfo( classStream);
		}
    }

    //private static readByteArray( DataInputStream

    CPInfo readConstant( DataInputStream classStream)
		throws IOException, IllegalStateException
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
				throw new IllegalStateException();
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
    }

    class CPUtf8 extends CPInfo
    {
		String value;
		CPUtf8( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_Utf8);
		    int length=classStream.readUnsignedShort();
		    StringBuffer sb=new StringBuffer(length);
		    for ( int i=0; i<length; i++)
		    {
				char nextChar;
				short nextByte=(short)classStream.readUnsignedByte();
				if ( nextByte>=1 && nextByte<128)
				{
				    nextChar=(char)nextByte;
				}
				else if ( nextByte>=192 && nextByte<224)
				{
				    nextChar=(char)(((nextByte & 0x1f ) << 6 ) + (classStream.readUnsignedByte() & 0x3f ));
				    i++;
				}
				else
				{
				    nextChar=(char)(((nextByte & 0xf ) << 12 ) + ((classStream.readUnsignedByte() & 0x3f ) << 6 ) + (classStream.readUnsignedByte() & 0x3f ));
				    i+=2;
				}
				sb.append( nextChar);
		    }
		    value=sb.toString();
		}
    }

    final String getString( int index)
    {
		return ((CPUtf8)constantPool[index]).value;
    }

    String getClassName( int classIndex)
    {
		char[] nameBuffer=getString( ((CPClass)constantPool[classIndex]).nameIndex).toCharArray();
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
    }

    class CPNameAndType extends CPInfo
    {
		int nameIndex;
		int descriptorIndex;

		CPNameAndType( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_NameAndType);
		    nameIndex=classStream.readUnsignedShort();
		    descriptorIndex=classStream.readUnsignedShort();
		}
    }

    class CPClass extends CPInfo
    {
		int nameIndex;
		CPClass( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_Class);
		    nameIndex=classStream.readUnsignedShort();
		}
    }

    class CPString extends CPInfo
    {
		int valueIndex;
		CPString( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_String);
		    valueIndex=classStream.readUnsignedShort();
		}
    }

    class CPInteger extends CPInfo
    {
		int value;
		CPInteger( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_Integer);
		    value=classStream.readInt();
		}
    }

    class CPFloat extends CPInfo
    {
		float value;
		CPFloat( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_Float);
		    value=classStream.readFloat();
		}
    }

    class CPLong extends CPInfo
    {
		long value;
		CPLong( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_Long);
		    value=classStream.readLong();
		}
    }

    class CPDouble extends CPInfo
    {
		double value;
		CPDouble( DataInputStream classStream)
		    throws IOException
		{
		    super( AnalyzeClass.CONSTANT_Double);
		    value=classStream.readDouble();
		}
    }

    class FieldInfo
    {
		int accessFlags;
		int nameIndex;
		int descriptorIndex;
		int attributesCount;
		AttributeInfo[] attributes;

		FieldInfo( DataInputStream classStream)
		    throws IOException
		{
		    accessFlags=classStream.readUnsignedShort();
		    nameIndex=classStream.readUnsignedShort();
		    descriptorIndex=classStream.readUnsignedShort();
		    attributesCount=classStream.readUnsignedShort();
		    attributes=new AttributeInfo[attributesCount];
		    for ( int i=0; i<attributesCount; i++)
		    {
				attributes[i]=new AttributeInfo(classStream);
		    }
		}
    }

    class AttributeInfo
    {
		int nameIndex;
		int length;
		Object value;
		AttributeInfo( DataInputStream classStream)
		    throws IOException
		{
		    nameIndex=classStream.readUnsignedShort();
		    length=classStream.readInt();
		    String type=getString( nameIndex);
		    if ( type.equals( "SourceFile"))
		    {
				value=getString(classStream.readUnsignedShort());
		    }
		    else if ( type.equals( "ConstantValue"))
		    {
				value=constantPool[classStream.readUnsignedShort()];
		    }
		    else if ( type.equals( "Code"))
		    {
				value=new CodeAttribute( classStream);
		    }
		    else if ( type.equals( "Exceptions"))
		    {
				value=new ExceptionsAttribute( classStream);
		    }
		    else if ( type.equals( "LineNumberTable"))
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
				value=new byte[length];
				classStream.readFully( (byte[])value);
		    }
		}
    }

    class CodeAttribute
    {
		int maxStack;
		int maxLocals;
		int codeLength;
		byte[] code;
		int exceptionCount;
		CodeException[] exceptions;
		int attributeCount;
		AttributeInfo[] attributes;

		CodeAttribute( DataInputStream classStream)
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
		    attributeCount=classStream.readUnsignedShort();
		    attributes=new AttributeInfo[attributeCount];
		    for ( i=0; i<attributeCount; i++)
		    {
				attributes[i]=new AttributeInfo( classStream);
		    }
		}
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

    class ExceptionsAttribute
    {
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
    }

    class LineNumberTableAttribute
    {
		int lineNumberEntryCount;
		LineNumberEntry[] lineNumberEntries;

		LineNumberTableAttribute( DataInputStream classStream)
		    throws IOException
        {
		    lineNumberEntryCount=classStream.readUnsignedShort();
		    lineNumberEntries=new LineNumberEntry[lineNumberEntryCount];
		    for ( int i=0; i<lineNumberEntryCount; i++)
		    {
				lineNumberEntries[i]=new LineNumberEntry( classStream);
		    }
		}
    }

    class LineNumberEntry
    {
		int start_pc;
		int line_number;
		LineNumberEntry( DataInputStream classStream)
		    throws IOException
		{
		    start_pc=classStream.readUnsignedShort();
		    line_number=classStream.readUnsignedShort();
		}
    }
}
