
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CodeAttribute implements Attribute
{
	public final static String typeString="Code";

	private int maxStack;
	private int maxLocals;
 	private ArrayList instructions; // Instruction
	private ArrayList exceptions;
	private AttributeList attributes;

    CodeAttribute( ClassWriter contains)
    {
        maxStack=0;
        maxLocals=0;
        instructions=new ArrayList();
        exceptions=new ArrayList();
        attributes=new AttributeList( contains);
    }

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
        try
        {
	        while ( ip.currentPos<code.length)
	        {
	    		Instruction instr=OpCode.opCodes[OpCode.mU( code[ip.currentPos])].read( ip, code);
	      		instructions.add( instr);
	        	ip.wide=instr.opCode instanceof Wide;
	        }
	        if ( ip.currentPos!=code.length)
	        {
	            throw new CodeCheckException( "Did not read code fully");
	    	}
        }
        catch ( CodeCheckException cce)
        {
            throw new IllegalStateException( cce.getMessage());
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

    public int getMaxStack() { return maxStack; }

    public final int getMaxLocals() { return maxLocals; }
    public void setMaxLocals( int m) { maxLocals=m; }

    public Instruction getByIndex( int i)
    {
        return (Instruction)instructions.get( i);
    }

    /**
     * Replaces oldLength instructions at index at with the instructions
     * in newInstructions.  instructionStarts for instructions after
     * the inserted instructions are adjusted for the difference in length
     * between the old instructions and the inserted instructions.
     *
     * Branches within the existing code to points after the inserted
     * instructions are adjusted for the difference in instruction
     * length between the old instructions and the new instructions.
     * Branches from outside the inserted section to within the inserted
     * section cause a CodeCheckException.  Branches within the inserted
     * section are not adjusted; if such branches point to points after
     * the inserted code, the branches must be set as if the code had
     * already been inserted.
     *
     * Assumes branch
     * offsets from instructions within newInstructions
     * are already set as if newInstructions had been inserted.
     *
     * Exception ranges are adjusted for the difference in length
     * between the old and new instructions.  Exception ranges that
     * start or end within the added instructions throw a CodeCheckException.
     *
     * Offsets in the line number table after the end of the replaced section
     * are updated by the difference between the length of the old and new
     * instructions.  Offsets in the line number table that refer to the middle
     * of the replaced range are removed so they don't end up pointing to a non-
     * instruction.
     */
    public void insertInstructions( int at, int oldLength,
        Collection newInstructions)
        throws CodeCheckException
    {
        int start;
        int oldPostEnd;
        Instruction oldLastInstruction;

        if ( at<0 || at+oldLength>instructions.size())
            throw new CodeCheckException( "Bad instruction insertion point");
        if ( at==0)
        {
            start=0;
         }
        else
        {
            Instruction lastInstruction=(Instruction)instructions.get( at-1);
            start=lastInstruction.instructionStart+
                lastInstruction.getLength();
        }
        if ( oldLength==0)
        {
            oldPostEnd=start;
        }
        else
        {
            Instruction lastInstruction=(Instruction)instructions.get(
                at+oldLength-1);
            oldPostEnd=lastInstruction.instructionStart+
                lastInstruction.getLength();
        }
        int newPostEnd=start;
        for ( Iterator i=newInstructions.iterator(); i.hasNext();)
        {
            Instruction currentInstruction=(Instruction)i.next();
            currentInstruction.instructionStart=newPostEnd;
            newPostEnd+=currentInstruction.getLength();
        }
        // Replace the instructions
        for ( int i=0; i<oldLength; i++)
            instructions.remove( at);
        instructions.addAll( at, newInstructions);

        // Fix instructions for the opcodes
        for ( Iterator i=instructions.iterator(); i.hasNext();)
        {
            Instruction current=(Instruction)i.next();
            if ( current.instructionStart<start ||
                current.instructionStart>=newPostEnd)
                current.opCode.fixDestinationAddress( current,
                    start, oldPostEnd, newPostEnd);
        }

        // Fix exception ranges
        for ( Iterator i=exceptions.iterator(); i.hasNext();)
        {
            CodeException range=(CodeException)i.next();
            if ( ( range.start>start && range.start<oldPostEnd)
                || ( range.end>start && range.end<oldPostEnd))
            {
                throw new CodeCheckException(
                    "Exception range overlaps inserted code");
            }
            if ( range.start>=oldPostEnd)
                range.start+=newPostEnd-oldPostEnd;
            if ( range.end>=oldPostEnd)
                range.end+=newPostEnd-oldPostEnd;
        }

        // Fix line number table offsets
        LineNumberTableAttribute lineTable=getLineNumberAttribute();
        if ( lineTable!=null)
            lineTable.fixOffsets( start, oldPostEnd, newPostEnd);
    }

 	/**
	 * Checks the code to make sure each instruction is visited, that there
     * is a consistent stack depth at each instruction, and determines the
     * maximum stack depth-- and saves it in the max stack variable.
     */
 	public void codeCheck()
        throws CodeCheckException
    {
        LinkedList next=new LinkedList();
        Stack[] stackArray=new Stack[instructions.size()];

        /*
         * Initialize next collection with the start point of the
         * method and with any exception handlers
         */
        next.add( new InstructionPointer( 0));
        stackArray[0]=new Stack();
        for ( Iterator i=exceptions.iterator(); i.hasNext();)
        {
            int handler=((CodeException)i.next()).handler;
            InstructionPointer ip=new InstructionPointer(
                ((CodeException)i.next()).handler);
            next.add( ip);
            int index=indexAtOffset( ip);
            stackArray[index]=new Stack();
            stackArray[index].push( ProcessStack.CAT1);
        }
        while ( ! next.isEmpty())
        {
            InstructionPointer ip=(InstructionPointer)next.getFirst();
            next.removeFirst();
            try
            {
                int index=indexAtOffset( ip);
                if ( stackArray[index]==null)
                {
                    throw new
                        CodeCheckException( "Instruction with undefined stack");
                }
                Stack old_stack=stackArray[index];
                Instruction instruction=getByIndex( index);
                Stack new_stack=instruction.opCode.stackUpdate( instruction,
                    old_stack, this);
                ArrayList new_pointers=new ArrayList( 20);
                instruction.opCode.traverse( instruction, new_pointers, this);
                for ( Iterator i=new_pointers.iterator(); i.hasNext();)
                {
                    ip=(InstructionPointer)i.next();
                    index=indexAtOffset( ip);
                    if ( stackArray[index]==null)
                    {
                        stackArray[index]=new_stack;
                        next.add( ip);
                    }
                    else
                    {
                        if ( ! new_stack.equals( stackArray[index]))
                            throw new CodeCheckException( "Stacks don't match");
                    }
                }
                // Special processing for jsr opcodes
                if ( instruction.opCode.getMnemonic().startsWith( "jsr"))
                {
                    ip=new InstructionPointer(
                        instruction.getOffsetDestination());
                    index=indexAtOffset( ip);
                    new_stack=(Stack)new_stack.clone();
                    new_stack.push( ProcessStack.CAT1);
                    stackArray[index]=new_stack;
                    next.add( ip);
                }
            }
            catch ( CodeCheckException cce)
            {
                throw new CodeCheckException( cce.getMessage()+" at offset "
                    +ip.currentPos);
            }
        }
        int max_stack=0;
        for ( int i=0; i<stackArray.length; i++)
        {
            if ( stackArray[i]==null)
                throw new CodeCheckException( "Unvisited opcode at offset "+
                    ((Instruction)instructions.get( i)).instructionStart);
            if ( stackArray[i].size()>max_stack)
                max_stack=stackArray[i].size();
        }
        maxStack=max_stack;
    }

 	public int indexAtOffset( InstructionPointer ip)
  		throws CodeCheckException
    {
        int result=0;
        for ( Iterator i=instructions.iterator(); i.hasNext(); result++)
        {
            Instruction current=(Instruction)i.next();
            if ( current.instructionStart==ip.currentPos && current.wideFlag==
            	ip.wide)
            	return result;
            if ( current.instructionStart>ip.currentPos)
            	throw new CodeCheckException( "Bad instruction alignment");
        }
        throw new CodeCheckException( "Offset beyond end of method");
    }

	public LineNumberTableAttribute getLineNumberAttribute()
 	{
  		return (LineNumberTableAttribute)attributes.getAttributeByType(
    		LineNumberTableAttribute.typeString);
  	}

	public String getTypeString() { return typeString; }

 	public ClassWriter getCurrentClass()
  	{
   		return attributes.getCurrentClass();
    }

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

	static class CodeException
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
		    new SimpleOpCode( 50, 1, "aaload", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 83, 1, "aastore", new Cat1Stack( 3, 0));
		    new SimpleOpCode( 1, 1, "aconst_null", new Cat1Stack( 0, 1));
		    new WSimpleOpCode( 25, 2, "aload", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 42, 1, "aload_0", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 43, 1, "aload_1", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 44, 1, "aload_2", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 45, 1, "aload_3", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 189, 3, "anewarray");
		    new ReturnOpCode( 176, 1, "areturn", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 190, 1, "arraylength");
		    new WSimpleOpCode( 58, 2, "astore", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 75, 1, "astore_0", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 76, 1, "astore_1", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 77, 1, "astore_2", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 78, 1, "astore_3", new Cat1Stack( 1, 0));
		    new ReturnOpCode( 191, 1, "athrow", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 51, 1, "baload", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 84, 1, "bastore", new Cat1Stack( 3, 0));
		    new SimpleOpCode( 16, 2, "bipush", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 52, 1, "caload", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 85, 1, "castore", new Cat1Stack( 3, 0));
		    new SimpleOpCode( 192, 3, "checkcast");
		    new SimpleOpCode( 144, 1, "d2f", new ConvertStack( ProcessStack.CAT2, ProcessStack.CAT1));
		    new SimpleOpCode( 142, 1, "d2i", new ConvertStack( ProcessStack.CAT2, ProcessStack.CAT1));
		    new SimpleOpCode( 143, 1, "d2l", new ConvertStack( ProcessStack.CAT2, ProcessStack.CAT1));
		    new SimpleOpCode( 99, 1, "dadd", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 49, 1, "daload", new ComboStack( new Cat1Stack( 2, 0), new Cat2Stack( 0, 1)));
		    new SimpleOpCode( 82, 1, "dastore", new ComboStack( new Cat1Stack( 2, 0), new Cat2Stack( 1, 0)));
		    new SimpleOpCode( 152, 1, "dcmpg", new ComboStack( new Cat2Stack( 2, 0), new Cat1Stack( 0, 1)));
		    new SimpleOpCode( 151, 1, "dcmpl", new ComboStack( new Cat2Stack( 2, 0), new Cat1Stack( 0, 1)));
		    new SimpleOpCode( 14, 1, "dconst_0", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 15, 1, "dconst_1", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 111, 1, "ddiv", new Cat2Stack( 2, 1));
		    new WSimpleOpCode( 24, 2, "dload", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 38, 1, "dload_0", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 39, 1, "dload_1", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 40, 1, "dload_2", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 41, 1, "dload_3", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 107, 1, "dmul", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 119, 1, "dneg", new Cat2Stack( 1, 1));
		    new SimpleOpCode( 115, 1, "drem", new Cat2Stack( 2, 1));
		    new ReturnOpCode( 175, 1, "dreturn", new Cat2Stack( 1, 0));
		    new WSimpleOpCode( 57, 2, "dstore", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 71, 1, "dstore_0", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 72, 1, "dstore_1", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 73, 1, "dstore_2", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 74, 1, "dstore_3", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 103, 1, "dsub", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 89, 1, "dup", new Cat1Stack( 1, 2));
		    new SimpleOpCode( 90, 1, "dup_x1", new Cat1Stack( 2, 3));
		    new SimpleOpCode( 91, 1, "dup_x2", new DupX2Stack());
		    new SimpleOpCode( 92, 1, "dup2", new Dup2Stack());
		    new SimpleOpCode( 93, 1, "dup2_x1", new Dup2X1Stack());
		    new SimpleOpCode( 94, 1, "dup2_x2", new Dup2X2Stack());
		    new SimpleOpCode( 141, 1, "f2d", new ConvertStack( ProcessStack.CAT1, ProcessStack.CAT2));
		    new SimpleOpCode( 139, 1, "f2i");
		    new SimpleOpCode( 140, 1, "f2l", new ConvertStack( ProcessStack.CAT1, ProcessStack.CAT2));
		    new SimpleOpCode( 98, 1, "fadd", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 48, 1, "faload", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 81, 1, "fastore", new Cat1Stack( 3, 0));
		    new SimpleOpCode( 150, 1, "fcmpg", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 149, 1, "fcmpl", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 11, 1, "fconst_0", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 12, 1, "fconst_1", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 13, 1, "fconst_2", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 110, 1, "fdiv", new Cat1Stack( 2, 1));
		    new WSimpleOpCode( 23, 2, "fload", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 34, 1, "fload_0", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 35, 1, "fload_1", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 36, 1, "fload_2", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 37, 1, "fload_3", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 106, 1, "fmul", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 118, 1, "fneg");
		    new SimpleOpCode( 114, 1, "frem", new Cat1Stack( 2, 1));
		    new ReturnOpCode( 174, 1, "freturn", new Cat1Stack( 1, 0));
		    new WSimpleOpCode( 56, 2, "fstore", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 67, 1, "fstore_0", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 68, 1, "fstore_1", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 69, 1, "fstore_2", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 70, 1, "fstore_3", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 102, 1, "fsub", new Cat1Stack( 2, 1));
		    new GetOpCode( 180, 3, "getfield");
		    new GetOpCode( 178, 3, "getstatic");
		    new GotoOpCode( 167, 3, "goto");
		    new GotoOpCode( 200, 5, "goto_w");
		    new SimpleOpCode( 145, 1, "i2b");
		    new SimpleOpCode( 146, 1, "i2c");
		    new SimpleOpCode( 135, 1, "i2d", new ConvertStack( ProcessStack.CAT1, ProcessStack.CAT2));
		    new SimpleOpCode( 134, 1, "i2f");
		    new SimpleOpCode( 133, 1, "i2l", new ConvertStack( ProcessStack.CAT1, ProcessStack.CAT2));
		    new SimpleOpCode( 147, 1, "i2s");
		    new SimpleOpCode( 96, 1, "iadd", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 46, 1, "iaload", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 126, 1, "iand", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 79, 1, "iastore", new Cat1Stack( 3, 0));
		    new SimpleOpCode( 2, 1, "iconst_m1", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 3, 1, "iconst_0", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 4, 1, "iconst_1", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 5, 1, "iconst_2", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 6, 1, "iconst_3", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 7, 1, "iconst_4", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 8, 1, "iconst_5", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 108, 1, "idiv", new Cat1Stack( 2, 1));
		    new BranchOpCode( 165, 3, "if_acmpeq", 2);
		    new BranchOpCode( 166, 3, "if_acmpne", 2);
		    new BranchOpCode( 159, 3, "if_icmpeq", 2);
		    new BranchOpCode( 160, 3, "if_icmpne", 2);
		    new BranchOpCode( 161, 3, "if_icmplt", 2);
		    new BranchOpCode( 162, 3, "if_icmpge", 2);
		    new BranchOpCode( 163, 3, "if_icmpgt", 2);
		    new BranchOpCode( 164, 3, "if_icmple", 2);
		    new BranchOpCode( 153, 3, "ifeq", 1);
		    new BranchOpCode( 154, 3, "ifne", 1);
		    new BranchOpCode( 155, 3, "iflt", 1);
		    new BranchOpCode( 156, 3, "ifge", 1);
		    new BranchOpCode( 157, 3, "ifgt", 1);
		    new BranchOpCode( 158, 3, "ifle", 1);
		    new BranchOpCode( 199, 3, "ifnonnull", 1);
		    new BranchOpCode( 198, 3, "ifnull", 1);
		    new WSimpleOpCode( 132, 3, "iinc", new Cat1Stack( 0, 0));
		    new WSimpleOpCode( 21, 2, "iload", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 26, 1, "iload_0", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 27, 1, "iload_1", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 28, 1, "iload_2", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 29, 1, "iload_3", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 104, 1, "imul", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 116, 1, "ineg");
		    new SimpleOpCode( 193, 3, "instanceof");
		    new InvokeOpCode( 185, 5, "invokeinterface");
		    new InvokeOpCode( 183, 3, "invokespecial");
		    new InvokeOpCode( 184, 3, "invokestatic");
		    new InvokeOpCode( 182, 3, "invokevirtual");
		    new SimpleOpCode( 128, 1, "ior", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 112, 1, "irem", new Cat1Stack( 2, 1));
		    new ReturnOpCode( 172, 1, "ireturn", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 120, 1, "ishl", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 122, 1, "ishr", new Cat1Stack( 2, 1));
		    new WSimpleOpCode( 54, 2, "istore", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 59, 1, "istore_0", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 60, 1, "istore_1", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 61, 1, "istore_2", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 62, 1, "istore_3", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 100, 1, "isub", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 124, 1, "iushr", new Cat1Stack( 2, 1));
		    new SimpleOpCode( 130, 1, "ixor", new Cat1Stack( 2, 1));
		    new JsrOpCode( 168, 3, "jsr"); // Special handling in check code
		    new JsrOpCode( 201, 5, "jsr_w"); // ''
		    new SimpleOpCode( 138, 1, "l2d", new ConvertStack( ProcessStack.CAT2, ProcessStack.CAT2));
		    new SimpleOpCode( 137, 1, "l2f", new ConvertStack( ProcessStack.CAT2, ProcessStack.CAT1));
		    new SimpleOpCode( 136, 1, "l2i", new ConvertStack( ProcessStack.CAT2, ProcessStack.CAT1));
		    new SimpleOpCode( 97, 1, "ladd", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 47, 1, "laload", new ComboStack( new Cat1Stack( 2, 0), new Cat2Stack( 0, 1)));
		    new SimpleOpCode( 127, 1, "land", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 80, 1, "lastore", new ComboStack( new Cat1Stack( 2, 0), new Cat2Stack( 1, 0)));
		    new SimpleOpCode( 148, 1, "lcmp", new ComboStack( new Cat2Stack( 2, 0), new Cat1Stack( 1, 0)));
		    new SimpleOpCode( 9, 1, "lconst_0", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 10, 1, "lconst_1", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 18, 2, "ldc", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 19, 3, "ldc_w", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 20, 3, "ldc2_w", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 109, 1, "ldiv", new Cat2Stack( 2, 1));
		    new WSimpleOpCode( 22, 2, "lload", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 30, 1, "lload_0", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 31, 1, "lload_1", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 32, 1, "lload_2", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 33, 1, "lload_3", new Cat2Stack( 0, 1));
		    new SimpleOpCode( 105, 1, "lmul", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 117, 1, "lneg", new Cat2Stack( 1, 1));
		    new LookupSwitch( 171, "lookupswitch");
		    new SimpleOpCode( 129, 1, "lor", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 113, 1, "lrem", new Cat2Stack( 2, 1));
		    new ReturnOpCode( 173, 1, "lreturn", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 121, 1, "lshl", new ComboStack( new Cat2Stack( 1, 0), new ComboStack( new Cat1Stack( 1, 0), new Cat2Stack( 0, 1))));
		    new SimpleOpCode( 123, 1, "lshr", new ComboStack( new Cat2Stack( 1, 0), new ComboStack( new Cat1Stack( 1, 0), new Cat2Stack( 0, 1))));
		    new WSimpleOpCode( 55, 2, "lstore", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 63, 1, "lstore_0", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 64, 1, "lstore_1", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 65, 1, "lstore_2", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 66, 1, "lstore_3", new Cat2Stack( 1, 0));
		    new SimpleOpCode( 101, 1, "lsub", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 125, 1, "lushr", new ComboStack( new Cat2Stack( 1, 0), new ComboStack( new Cat1Stack( 1, 0), new Cat2Stack( 0, 1))));
		    new SimpleOpCode( 131, 1, "lxor", new Cat2Stack( 2, 1));
		    new SimpleOpCode( 194, 1, "monitorenter", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 195, 1, "monitorexit", new Cat1Stack( 1, 0));
		    new MultiArrayOpCode( 197, 4, "multianewarray");
		    new SimpleOpCode( 187, 3, "new", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 188, 2, "newarray");
		    new SimpleOpCode( 0, 1, "nop", new Cat1Stack( 0, 0));
		    new SimpleOpCode( 87, 1, "pop", new Cat1Stack( 1, 0));
		    new SimpleOpCode( 88, 1, "pop2", new Pop2Stack());
		    new GetOpCode( 181, 3, "putfield");
		    new GetOpCode( 179, 3, "putstatic");
		    new ReturnOpCode( 169, 2, "ret", new Cat1Stack( 0, 0));
		    new ReturnOpCode( 177, 1, "return", new Cat1Stack( 0, 0));
		    new SimpleOpCode( 53, 1, "saload", new Cat1Stack( 2,1));
		    new SimpleOpCode( 86, 1, "sastore", new Cat1Stack( 3, 0));
		    new SimpleOpCode( 17, 3, "sipush", new Cat1Stack( 0, 1));
		    new SimpleOpCode( 95, 1, "swap", new Cat1Stack( 2, 2));
		    new TableSwitch( 170, "tableswitch");
		    new Wide( 196, "wide");
		}
		catch ( Throwable t)
		{
		    t.printStackTrace();
		}
    }

}
