
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
import java.util.Collections;

public class TypeParse
{
    public static final String ARG_ARRAYREF="A";
    public static final String ARG_BYTE="B";
    public static final String ARG_CHAR="C";
    public static final String ARG_DOUBLE="D";
    public static final String ARG_FLOAT="F";
    public static final String ARG_INT="I";
    public static final String ARG_LONG="J";
    public static final String ARG_OBJREF="L";
    public static final String ARG_SHORT="S";
    public static final String ARG_VOID="V";
    public static final String ARG_BOOLEAN="Z";

    static class ParseTable
    {
        ParseTable( char c1, String s1)
        {
            c=c1;
            s=s1;
        }
        char c;
        String s;
    }

    private static ParseTable table[]={
        new ParseTable( 'B', ARG_BYTE),
        new ParseTable( 'C', ARG_CHAR),
        new ParseTable( 'D', ARG_DOUBLE),
        new ParseTable( 'F', ARG_FLOAT),
        new ParseTable( 'I', ARG_INT),
        new ParseTable( 'J', ARG_LONG),
        new ParseTable( 'S', ARG_SHORT),
        new ParseTable( 'V', ARG_VOID),
        new ParseTable( 'Z', ARG_BOOLEAN)
        };
    public static String parseFieldType( String simpleType)
    	throws CodeCheckException
    {
        return parseFieldArray( simpleType.toCharArray(),
        	new InstructionPointer( 0));
    }

	public static ArrayList parseMethodType( String methodType)
 		throws CodeCheckException
 	{
  		char[] array=methodType.toCharArray();
    	try
     	{
          	ArrayList result=new ArrayList( 20);
	    	if ( array.length<3 || array[0]!='(')
	     	{
				throw new CodeCheckException( "Bad method signature "+methodType);
	   		}
	 		InstructionPointer offset=new InstructionPointer( 1);
	   		while ( array[offset.currentPos]!=')')
	      	{
				result.add( parseFieldArray( array, offset));
	        }
         	offset.currentPos++;
          	result.add( parseFieldArray( array, offset));

            Collections.reverse( result);
            return result;
         }
         catch ( ArrayIndexOutOfBoundsException bounds)
         {
             throw new CodeCheckException( "MethodType truncated");
         }
    }

    public static Object stackCategory( String arg_type)
    {
        if ( arg_type==ARG_VOID)
        	return null;
        if ( arg_type==ARG_DOUBLE || arg_type==ARG_LONG)
        	return ProcessStack.CAT2;
        return ProcessStack.CAT1;
    }

    private static String parseFieldArray( char[] array,
    	InstructionPointer offset)
     	throws CodeCheckException
    {
        try
        {
            if ( array[offset.currentPos]=='[')
            {
                while ( array[++offset.currentPos]=='[');
                parseBaseArray( array, offset);
                return ARG_ARRAYREF;
            }
            else
            	return parseBaseArray( array, offset);
        }
        catch ( ArrayIndexOutOfBoundsException bounds)
        {
            throw new CodeCheckException( "Type array truncated");
        }
    }

    private static String parseBaseArray( char[] array,
    	InstructionPointer offset)
     	throws CodeCheckException
    {
        if ( array[offset.currentPos]=='L')
        {
            while ( array[offset.currentPos++]!=';');
            return ARG_OBJREF;
        }
        else
        {
            for ( int i=0; i<table.length; i++)
            {
                if ( table[i].c==array[offset.currentPos])
                {
                    ++offset.currentPos;
                    return table[i].s;
                }
            }
        }
        throw new CodeCheckException( "Unknown character in type signature");
    }
}