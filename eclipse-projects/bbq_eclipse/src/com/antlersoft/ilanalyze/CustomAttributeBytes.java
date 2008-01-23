/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.io.UnsupportedEncodingException;

import java.util.Iterator;
import java.util.List;

import com.antlersoft.util.NetByte;

/**
 * Interpret the sequence of bytes associated with a custom attribute
 * @author Michael A. MacDonald
 *
 */
public class CustomAttributeBytes {
	
	private int offset;
	private byte[] bytes;
	private CustomAttributeSetting setting;
	private List currentStringList;
	
	public static CustomAttributeSetting getSettings( ReadType containing, Signature s, byte[] b)
	throws InterpretationException
	{
		CustomAttributeSetting result=new CustomAttributeSetting( containing, s);
		
		CustomAttributeBytes cab=new CustomAttributeBytes( result, b);
		
		if ( b[0]!=1 || b[1]!=0)
			throw new InterpretationException( "Bad prolog");
		
		cab.offset=2;
		
		cab.currentStringList=cab.setting.getStringArguments();
		
		// Go through signature arguments
		for ( Iterator i=s.getArguments().iterator(); i.hasNext();)
		{
			if ( ! cab.ReadFixedArg( ((ReadArg)i.next()).getType()))
				return result;
		}
		
		if ( cab.offset>cab.bytes.length-2)
			throw new InterpretationException( "No room for number of named args");
		int numberOfNamed=NetByte.mU(cab.bytes[cab.offset++])+256*NetByte.mU(cab.bytes[cab.offset++]);
		
		for ( int i=0; i<numberOfNamed; ++i)
		{
			if ( ! cab.ReadNamedArg())
			{
				return result;
			}
		}
		
		return result;
	}
	
	public static class InterpretationException extends Exception
	{
		InterpretationException( String s)
		{
			super(s);
		}
	}
	
	/**
	 * Reads the fixed argument for a custom attribute from the current offset in the byte array,
	 * according to a supplied type.  The type may be an SZARRAY
	 * @param typeCode Argument type appearing in custom attribute constructor as int
	 * @param underlyingType Type of array elements if typeCode is ELEMENT_TYPE_SZARRAY; otherwise ignored
	 * @return True if the fixed arg was successfully read from the byte array, false otherwise
	 * @throws InterpretationException Thrown if the byte array appears to have a bad format
	 */
	private boolean ReadFixedArg( int typeCode, int underlyingType)
	throws InterpretationException
	{
		boolean result=false;
		if ( typeCode==ELEMENT_TYPE_SZARRAY)
		{
			if ( offset>bytes.length-4)
				throw new InterpretationException( "No room for SZARRAY length");
			long l=NetByte.mU( bytes[offset++])+256L*NetByte.mU( bytes[offset++])+65536L*NetByte.mU( bytes[offset++])+
				(65536L*256L)*NetByte.mU( bytes[offset++]);
			if ( l==(2L<<32)-1L)
				// Initialized value is NULL
				result=true;
			else
			{
				result=true;
				for ( long i=0; i<l && result; ++i)
				{
					result=ReadElem( underlyingType);
				}
			}
		}
		else
		{
			result=ReadElem( typeCode);
		}
		return result;
	}
	
	/**
	 * Reads a value for a custom attribute from the current offset in the byte array,
	 * according to a supplied type.  The type may not be an SZARRAY; it must be an atomic type
	 * @param typeCode Argument type appearing in custom attribute constructor as int
	 * @return True if the fixed arg was successfully read from the byte array, false otherwise
	 * @throws InterpretationException Thrown if the byte array appears to have a bad format
	 */
	private boolean ReadElem( int typeCode)
	throws InterpretationException
	{
		boolean result=true;
		switch ( typeCode)
		{
		case ELEMENT_TYPE_BOOLEAN:
		case ELEMENT_TYPE_I1:
		case ELEMENT_TYPE_U1:
			offset++;
			break;
		case ELEMENT_TYPE_CHAR:
		case ELEMENT_TYPE_I2:
		case ELEMENT_TYPE_U2:
			offset+=2;
			break;
		case ELEMENT_TYPE_I4:
		case ELEMENT_TYPE_U4:
		case ELEMENT_TYPE_R4:
			offset+=4;
			break;
		case ELEMENT_TYPE_I8:
		case ELEMENT_TYPE_U8:
		case ELEMENT_TYPE_R8:
			offset+=8;
			break;
		case ELEMENT_TYPE_STRING:
			ReadSerString();
			break;
		default :
			LoggingDBDriver.logger.finest( "Giving up on type code "+typeCode);
			result=false;
		}
		return result;
	}
	
	/**
	 * Converts a ReadType to an int type code and calls ReadFixedArg with that type code
	 * @param type Argument type appearing in custom attribute constructor as ReadType
	 * @return True if the fixed arg was successfully read from the byte array, false otherwise
	 * @throws InterpretationException Thrown if the byte array appears to have a bad format
	 */
	private boolean ReadFixedArg( ReadType type)
	throws InterpretationException
	{
		if ( type instanceof ReadArray)
		{
			ReadArray array=(ReadArray)type;
			return ReadFixedArg( array.getTypeCode(), array.getUnderlying().getTypeCode());
		}
		
		return ReadFixedArg( type.getTypeCode(), 0);
	}
	
	/**
	 * Read a single named argument from the byte sequence, as described in 23.3.  Reads information
	 * about the argument and creates a CustomAttributeSetting.NamedArg to add to the collection,
	 * then calls ReadFixedArg to get the value.
	 * @return True if the named argument could be read completely and correctly; false otherwise
	 * @throws InterpretationException If custom attribute byte string appears to have wrong format
	 */
	private boolean ReadNamedArg()
	throws InterpretationException
	{
		if ( offset>bytes.length-2)
		{
			throw new InterpretationException( "No room for named arg field or prop flag");
		}
		int f=NetByte.mU(bytes[offset++]);
		if ( f!=NAMED_ARG_IS_FIELD && f!=NAMED_ARG_IS_PROPERTY)
			throw new InterpretationException( "NamedArg:Unexpected field or property type code");

		int typeCode=bytes[offset++];
		int secondaryType=0;
		if ( typeCode==ELEMENT_TYPE_SZARRAY)
		{
			if ( offset>bytes.length-1)
				throw new InterpretationException( "NamedArg:No room for secondary type code");
			secondaryType=bytes[offset++];
		}
		currentStringList=null;
		String named_arg_name=ReadSerString();
		ReadType arg_type=null;
		if ( typeCode==VALUE_IS_ENUM)
		{
			arg_type=new BasicType(named_arg_name);
			// We just read enum name, which we don't care about
			named_arg_name=ReadSerString();
		}
		else if ( typeCode==ELEMENT_TYPE_STRING)
		{
			arg_type=new BasicType("System.String");
		}
		else if ( typeCode==ELEMENT_TYPE_SZARRAY)
		{
			if ( secondaryType==ELEMENT_TYPE_STRING)
			{
				arg_type=new ReadArray(new BasicType("System.String"), 1);
			}
			else
			{
				ReadType underlying=BuiltinType.getTypeFromCode(secondaryType);
				if ( underlying!=null)
					arg_type=new ReadArray( underlying, 1);
			}
		}
		else
		{
			arg_type=BuiltinType.getTypeFromCode(typeCode);
		}
		if ( arg_type==null)
			return false;
		CustomAttributeSetting.NamedArgument namedArg=new CustomAttributeSetting.NamedArgument( named_arg_name, f==NAMED_ARG_IS_PROPERTY, arg_type);
		setting.getNamedArguments().add(namedArg);
		currentStringList=namedArg.getStringArguments();
		
		if ( typeCode==VALUE_IS_ENUM)
		{
			// No good way to get enum type... so we will punt
			return false;
		}
		
		return ReadFixedArg( typeCode, secondaryType);
	}
	
	private CustomAttributeBytes( CustomAttributeSetting result, byte[] b)
	{
		bytes=b;
		offset=0;
		setting=result;
	}
	
	private String ReadSerString()
	throws InterpretationException
	{
		int length=getPackedLen();
		if ( offset>bytes.length-length)
		{
			throw new InterpretationException( "No room for SerString with lenght "+length);
		}
		try
		{
			String s=new String( bytes, offset, length, "UTF-8");
			offset+=length;
			if ( currentStringList!=null)
				currentStringList.add( s);
			return s;
		}
		catch ( UnsupportedEncodingException ue)
		{
			throw new InterpretationException( "UTF-8 not supported? "+ue.getMessage());
		}
	}
	
	/*
	 * If value is 0-127, store it in the 7 least significant bits in a
 single byte, and set the MSB to 0).
 If value is 128-0x3fff, store it in the 14 least significant bits of a
 16-bit word, and set the two high bits to binary 10. This is what
 you're seeing - the 8 comes from the high nibble being 1000.
 Otherwise (if value is 0x4000-0x1fffffff) store in a 32-bit word with
 the three high bits set to binary 110.
	 */
	private int getPackedLen()
		throws InterpretationException
	{
		if ( offset>bytes.length-1)
			throw new InterpretationException( "No room for 1 byte packed len length");
		int l=bytes[offset++];
		if ( l>=0 || l== -1)
		{
			return l;
		}
		if ( offset>bytes.length-1)
			throw new InterpretationException( "No room for 2 byte packed len length");
		l+=256;
		if ( ( l & 0xC0)==0x80)
		{
			offset++;
			bytes[offset-2]=(byte)( l & 0x3F);
			return NetByte.pairToInt(bytes, offset-2);
		}
		if ( offset>bytes.length-3)
			throw new InterpretationException( "No room for 4 byte packed len length");
		if ( ( l & 0xE0)==0xC0)
		{
			offset+=3;
			bytes[offset-4]=(byte)( l & 0x1F);
			return NetByte.quadToInt(bytes, offset-4);
		}
		throw new InterpretationException( "Failed to interpret PackedLen");
	}
	
	static final int ELEMENT_TYPE_END = 0x00; // Marks end of a list
	static final int ELEMENT_TYPE_VOID = 0x01; //
	static final int ELEMENT_TYPE_BOOLEAN = 0x02; //
	static final int ELEMENT_TYPE_CHAR = 0x03; //
	static final int ELEMENT_TYPE_I1 = 0x04; //
	static final int ELEMENT_TYPE_U1 = 0x05; //
	static final int ELEMENT_TYPE_I2 = 0x06; //
	static final int ELEMENT_TYPE_U2 = 0x07; //
	static final int ELEMENT_TYPE_I4 = 0x08; //
	static final int ELEMENT_TYPE_U4 = 0x09; //
	static final int ELEMENT_TYPE_I8 = 0x0a; //
	static final int ELEMENT_TYPE_U8 = 0x0b; //
	static final int ELEMENT_TYPE_R4 = 0x0c; //
	static final int ELEMENT_TYPE_R8 = 0x0d; //
	static final int ELEMENT_TYPE_STRING = 0x0e; //
	static final int ELEMENT_TYPE_PTR = 0x0f; // Followed by type
	static final int ELEMENT_TYPE_BYREF = 0x10; // Followed by type
	static final int ELEMENT_TYPE_VALUETYPE = 0x11; // Followed by TypeDef or TypeRef token
	static final int ELEMENT_TYPE_CLASS = 0x12; // Followed by TypeDef or TypeRef token
	static final int ELEMENT_TYPE_VAR = 0x13; // Generic parameter in a generic type definition,represented as number
	static final int ELEMENT_TYPE_ARRAY = 0x14; // type rank boundsCount bound1 ... loCount lo1 ...
	static final int ELEMENT_TYPE_GENERICINST = 0x15; // Generic type instantiation. Followed by type typearg-	count type-1 ... type-n
	static final int ELEMENT_TYPE_TYPEDBYREF = 0x16; //
	static final int ELEMENT_TYPE_I = 0x18; // System.IntPtr
	static final int ELEMENT_TYPE_U = 0x19; // System.UIntPtr
	static final int ELEMENT_TYPE_FNPTR = 0x1b; // Followed by full method signature
	static final int ELEMENT_TYPE_OBJECT = 0x1c; // System.Object
	static final int ELEMENT_TYPE_SZARRAY = 0x1d; // Single-dim array with 0 lower bound
	static final int ELEMENT_TYPE_MVAR = 0x1e; // Generic parameter in a generic method definition,	represented as number
	static final int ELEMENT_TYPE_CMOD_REQD = 0x1f; // Required modifier : followed by a TypeDef or	TypeRef token
	static final int ELEMENT_TYPE_CMOD_OPT = 0x20; // Optional modifier : followed by a TypeDef or	TypeRef token
	static final int ELEMENT_TYPE_INTERNAL = 0x21; // Implemented within the CLI
	static final int ELEMENT_TYPE_MODIFIER = 0x40; // Or'd with following element types
	static final int ELEMENT_TYPE_SENTINEL = 0x41; // Sentinel for vararg method signature
	static final int ELEMENT_TYPE_PINNED = 0x45; // Denotes a local variable that points at a pinned	object
	/*
	0x50 Indicates an argument of type System.Type.
	0x51 Used in custom attributes to specify a boxed object
	(23.3).
	0x52 Reserved
	*/
	static final int NAMED_ARG_IS_FIELD = 0x53; // Used in custom attributes to indicate a FIELD (22.10, 23.3).
	static final int NAMED_ARG_IS_PROPERTY = 0x54; // Used in custom attributes to indicate a PROPERTY
	static final int VALUE_IS_ENUM = 0x55; // Used in custom attributes to specify an enum
}
