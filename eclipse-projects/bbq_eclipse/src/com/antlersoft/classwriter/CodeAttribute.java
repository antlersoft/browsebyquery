
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

	private int maxStack;
	private int maxLocals;
 	private ArrayList instructions; // Instruction
	private ArrayList exceptions;
	private AttributeList attributes;

	CodeAttribute( DataInputStream classStream, ClassWriter contains)
	    throws IOException
	{
	    maxStack=classStream.readUnsignedShort();
	    maxLocals=classStream.readUnsignedShort();
	    int codeLength=classStream.readUnsignedShort()*65536+classStream.readUnsignedShort();
	    byte[] code=new byte[codeLength];
        instructions=new ArrayList();
	    classStream.readFully( code);
     	InstructionPointer ip=new InstructionPointer(0);
        while ( ip.currentPos<code.length)
        {
System.out.println( "ip.currentPos="+Integer.toString( ip.currentPos)+" code.length="+Integer.toString(code.length)+" current op="+Integer.toString( OpCode.mU( code[ip.currentPos])));;
    		Instruction instr=OpCode.opCodes[OpCode.mU( code[ip.currentPos])].read( ip, code);
System.out.println( instr.opCode.getMnemonic());
      		instructions.add( instr);
        	ip.wide=instr.opCode instanceof Wide;
        }
        if ( ip.currentPos!=code.length)
        {
            throw new IllegalStateException( "Did not read code fully");
    	}
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

	public LineNumberTableAttribute getLineNumberAttribute()
 	{
  		return (LineNumberTableAttribute)attributes.getAttributeByType(
    		LineNumberTableAttribute.typeString);
  	}

	public String getTypeString() { return typeString; }

	public void write( DataOutputStream classStream)
 		throws IOException
   	{
    	classStream.writeShort( maxStack);
     	classStream.writeShort( maxLocals);
      	Instruction lastInstr=
            (Instruction)instructions.get( instructions.size()-1);
        int codeLength=lastInstr.instructionStart+lastInstr.getLength();
      	classStream.writeShort( codeLength >>16);
       	classStream.writeShort( codeLength & 0xffff);
        for ( Iterator i=instructions.iterator(); i.hasNext();)
        {
            Instruction instr=(Instruction)i.next();
            instr.opCode.write( classStream, instr);
        }
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

    static
    {
		try
		{
		    new SimpleOpCode( 50, 1, "aaload");
		    new SimpleOpCode( 83, 1, "aastore");
		    new SimpleOpCode( 1, 1, "aconst_null");
		    new SimpleOpCode( 25, 2, "aload");
		    new SimpleOpCode( 42, 1, "aload_0");
		    new SimpleOpCode( 43, 1, "aload_1");
		    new SimpleOpCode( 44, 1, "aload_2");
		    new SimpleOpCode( 45, 1, "aload_3");
		    new SimpleOpCode( 189, 3, "anewarray");
		    new SimpleOpCode( 176, 1, "areturn");
		    new SimpleOpCode( 190, 1, "arraylength");
		    new SimpleOpCode( 58, 2, "astore");
		    new SimpleOpCode( 75, 1, "astore_0");
		    new SimpleOpCode( 76, 1, "astore_1");
		    new SimpleOpCode( 77, 1, "astore_2");
		    new SimpleOpCode( 78, 1, "astore_3");
		    new SimpleOpCode( 191, 1, "athrow");
		    new SimpleOpCode( 51, 1, "baload");
		    new SimpleOpCode( 84, 1, "bastore");
		    new SimpleOpCode( 16, 2, "bipush");
		    new SimpleOpCode( 52, 1, "caload");
		    new SimpleOpCode( 85, 1, "castore");
		    new SimpleOpCode( 192, 3, "checkcast");
		    new SimpleOpCode( 144, 1, "d2f");
		    new SimpleOpCode( 142, 1, "d2i");
		    new SimpleOpCode( 143, 1, "d2l");
		    new SimpleOpCode( 99, 1, "dadd");
		    new SimpleOpCode( 49, 1, "daload");
		    new SimpleOpCode( 82, 1, "dastore");
		    new SimpleOpCode( 152, 1, "dcmpg");
		    new SimpleOpCode( 151, 1, "dcmpl");
		    new SimpleOpCode( 14, 1, "dconst_0");
		    new SimpleOpCode( 15, 1, "dconst_1");
		    new SimpleOpCode( 111, 1, "ddiv");
		    new SimpleOpCode( 24, 2, "dload");
		    new SimpleOpCode( 38, 1, "dload_0");
		    new SimpleOpCode( 39, 1, "dload_1");
		    new SimpleOpCode( 40, 1, "dload_2");
		    new SimpleOpCode( 41, 1, "dload_3");
		    new SimpleOpCode( 107, 1, "dmul");
		    new SimpleOpCode( 119, 1, "dneg");
		    new SimpleOpCode( 115, 1, "drem");
		    new SimpleOpCode( 175, 1, "dreturn");
		    new SimpleOpCode( 57, 2, "dstore");
		    new SimpleOpCode( 71, 1, "dstore_0");
		    new SimpleOpCode( 72, 1, "dstore_1");
		    new SimpleOpCode( 73, 1, "dstore_2");
		    new SimpleOpCode( 74, 1, "dstore_3");
		    new SimpleOpCode( 103, 1, "dsub");
		    new SimpleOpCode( 89, 1, "dup");
		    new SimpleOpCode( 90, 1, "dup_x1");
		    new SimpleOpCode( 91, 1, "dup_x2");
		    new SimpleOpCode( 92, 1, "dup2");
		    new SimpleOpCode( 93, 1, "dup2_x1");
		    new SimpleOpCode( 94, 1, "dup2_x4");
		    new SimpleOpCode( 141, 1, "f2d");
		    new SimpleOpCode( 139, 1, "f2i");
		    new SimpleOpCode( 140, 1, "f2l");
		    new SimpleOpCode( 98, 1, "fadd");
		    new SimpleOpCode( 48, 1, "faload");
		    new SimpleOpCode( 81, 1, "fastore");
		    new SimpleOpCode( 150, 1, "fcmpg");
		    new SimpleOpCode( 149, 1, "fcmpl");
		    new SimpleOpCode( 11, 1, "fconst_0");
		    new SimpleOpCode( 12, 1, "fconst_1");
		    new SimpleOpCode( 13, 1, "fconst_2");
		    new SimpleOpCode( 110, 1, "fdiv");
		    new SimpleOpCode( 23, 2, "fload");
		    new SimpleOpCode( 34, 1, "fload_0");
		    new SimpleOpCode( 35, 1, "fload_1");
		    new SimpleOpCode( 36, 1, "fload_2");
		    new SimpleOpCode( 37, 1, "fload_3");
		    new SimpleOpCode( 106, 1, "fmul");
		    new SimpleOpCode( 118, 1, "fneg");
		    new SimpleOpCode( 114, 1, "frem");
		    new SimpleOpCode( 174, 1, "freturn");
		    new SimpleOpCode( 56, 2, "fstore");
		    new SimpleOpCode( 67, 1, "fstore_0");
		    new SimpleOpCode( 68, 1, "fstore_1");
		    new SimpleOpCode( 69, 1, "fstore_2");
		    new SimpleOpCode( 70, 1, "fstore_3");
		    new SimpleOpCode( 102, 1, "fsub");
		    new SimpleOpCode( 180, 3, "getfield");
		    new SimpleOpCode( 178, 3, "getstatic");
		    new SimpleOpCode( 167, 3, "goto");
		    new SimpleOpCode( 200, 3, "goto_w");
		    new SimpleOpCode( 145, 1, "i2b");
		    new SimpleOpCode( 146, 1, "i2c");
		    new SimpleOpCode( 135, 1, "i2d");
		    new SimpleOpCode( 134, 1, "i2f");
		    new SimpleOpCode( 133, 1, "i2l");
		    new SimpleOpCode( 147, 1, "i2s");
		    new SimpleOpCode( 96, 1, "iadd");
		    new SimpleOpCode( 46, 1, "iaload");
		    new SimpleOpCode( 126, 1, "iand");
		    new SimpleOpCode( 79, 1, "iastore");
		    new SimpleOpCode( 2, 1, "iconst_m1");
		    new SimpleOpCode( 3, 1, "iconst_0");
		    new SimpleOpCode( 4, 1, "iconst_1");
		    new SimpleOpCode( 5, 1, "iconst_2");
		    new SimpleOpCode( 6, 1, "iconst_3");
		    new SimpleOpCode( 7, 1, "iconst_4");
		    new SimpleOpCode( 8, 1, "iconst_5");
		    new SimpleOpCode( 108, 1, "idiv");
		    new SimpleOpCode( 165, 3, "if_acmpeq");
		    new SimpleOpCode( 166, 3, "if_acmpne");
		    new SimpleOpCode( 159, 3, "if_icmpeq");
		    new SimpleOpCode( 160, 3, "if_icmpne");
		    new SimpleOpCode( 161, 3, "if_icmplt");
		    new SimpleOpCode( 162, 3, "if_icmpge");
		    new SimpleOpCode( 163, 3, "if_icmpgt");
		    new SimpleOpCode( 164, 3, "if_icmple");
		    new SimpleOpCode( 153, 3, "ifeq");
		    new SimpleOpCode( 154, 3, "ifne");
		    new SimpleOpCode( 155, 3, "iflt");
		    new SimpleOpCode( 156, 3, "ifge");
		    new SimpleOpCode( 157, 3, "ifgt");
		    new SimpleOpCode( 158, 3, "ifle");
		    new SimpleOpCode( 199, 3, "ifnonnull");
		    new SimpleOpCode( 198, 3, "ifnull");
		    new SimpleOpCode( 132, 3, "iinc");
		    new SimpleOpCode( 21, 2, "iload");
		    new SimpleOpCode( 26, 1, "iload_0");
		    new SimpleOpCode( 27, 1, "iload_1");
		    new SimpleOpCode( 28, 1, "iload_2");
		    new SimpleOpCode( 29, 1, "iload_3");
		    new SimpleOpCode( 104, 1, "imul");
		    new SimpleOpCode( 116, 1, "ineg");
		    new SimpleOpCode( 193, 3, "instanceof");
		    new SimpleOpCode( 185, 5, "invokeinterface");
		    new SimpleOpCode( 183, 3, "invokespecial");
		    new SimpleOpCode( 184, 3, "invokestatic");
		    new SimpleOpCode( 182, 3, "invokevirtual");
		    new SimpleOpCode( 128, 1, "ior");
		    new SimpleOpCode( 112, 1, "irem");
		    new SimpleOpCode( 172, 1, "ireturn");
		    new SimpleOpCode( 120, 1, "ishl");
		    new SimpleOpCode( 122, 1, "ishr");
		    new SimpleOpCode( 54, 2, "istore");
		    new SimpleOpCode( 59, 1, "istore_0");
		    new SimpleOpCode( 60, 1, "istore_1");
		    new SimpleOpCode( 61, 1, "istore_2");
		    new SimpleOpCode( 62, 1, "istore_3");
		    new SimpleOpCode( 100, 1, "isub");
		    new SimpleOpCode( 124, 1, "iushr");
		    new SimpleOpCode( 130, 1, "ixor");
		    new SimpleOpCode( 168, 3, "jsr");
		    new SimpleOpCode( 201, 5, "jsr_w");
		    new SimpleOpCode( 138, 1, "l2d");
		    new SimpleOpCode( 137, 1, "l2f");
		    new SimpleOpCode( 136, 1, "l2i");
		    new SimpleOpCode( 97, 1, "ladd");
		    new SimpleOpCode( 47, 1, "laload");
		    new SimpleOpCode( 127, 1, "land");
		    new SimpleOpCode( 80, 1, "lastore");
		    new SimpleOpCode( 148, 1, "lcmp");
		    new SimpleOpCode( 9, 1, "lconst_0");
		    new SimpleOpCode( 10, 1, "lconst_1");
		    new SimpleOpCode( 18, 2, "ldc");
		    new SimpleOpCode( 19, 3, "ldc_w");
		    new SimpleOpCode( 20, 3, "ldc2_w");
		    new SimpleOpCode( 109, 1, "ldiv");
		    new SimpleOpCode( 22, 2, "lload");
		    new SimpleOpCode( 30, 1, "lload_0");
		    new SimpleOpCode( 31, 1, "lload_1");
		    new SimpleOpCode( 32, 1, "lload_2");
		    new SimpleOpCode( 33, 1, "lload_3");
		    new SimpleOpCode( 105, 1, "lmul");
		    new SimpleOpCode( 117, 1, "lneg");
		    new LookupSwitch( 171, "lookupswitch");
		    new SimpleOpCode( 129, 1, "lor");
		    new SimpleOpCode( 113, 1, "lrem");
		    new SimpleOpCode( 173, 1, "lreturn");
		    new SimpleOpCode( 121, 1, "lshl");
		    new SimpleOpCode( 123, 1, "lshr");
		    new SimpleOpCode( 55, 2, "lstore");
		    new SimpleOpCode( 63, 1, "lstore_0");
		    new SimpleOpCode( 64, 1, "lstore_1");
		    new SimpleOpCode( 65, 1, "lstore_2");
		    new SimpleOpCode( 66, 1, "lstore_3");
		    new SimpleOpCode( 101, 1, "lsub");
		    new SimpleOpCode( 125, 1, "lushr");
		    new SimpleOpCode( 131, 1, "lxor");
		    new SimpleOpCode( 194, 1, "monitorenter");
		    new SimpleOpCode( 195, 1, "monitorexit");
		    new SimpleOpCode( 197, 4, "multianewarray");
		    new SimpleOpCode( 187, 3, "new");
		    new SimpleOpCode( 188, 2, "newarray");
		    new SimpleOpCode( 0, 1, "nop");
		    new SimpleOpCode( 87, 1, "pop");
		    new SimpleOpCode( 88, 1, "pop2");
		    new SimpleOpCode( 181, 3, "putfield");
		    new SimpleOpCode( 179, 3, "putstatic");
		    new SimpleOpCode( 169, 2, "ret");
		    new SimpleOpCode( 177, 1, "return");
		    new SimpleOpCode( 53, 1, "saload");
		    new SimpleOpCode( 86, 1, "sastore");
		    new SimpleOpCode( 17, 3, "sipush");
		    new SimpleOpCode( 95, 1, "swap");
		    new TableSwitch( 170, "tableswitch");
		    new Wide( 196, "wide");
		}
		catch ( Throwable t)
		{
		    t.printStackTrace();
		}
    }

}