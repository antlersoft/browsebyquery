
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Stack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.antlersoft.util.NetByte;

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
	    		Instruction instr=OpCode.opCodes[NetByte.mU( code[ip.currentPos])].read( ip, code);
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
     * Replaces oldLength instructions at instruction pointer
     * start with the instructions
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
    public void insertInstructions( int start, int oldLength,
        Collection newInstructions)
        throws CodeCheckException
    {
        int at;
        int oldPostEnd;
        int newCount=newInstructions.size();

        if ( start==0)
            at=0;
        else
        {
            Instruction endInstruction=
                (Instruction)instructions.get( instructions.size()-1);
            if ( start==endInstruction.instructionStart+
                endInstruction.getLength())
                at=instructions.size();
            else
                at=indexAtOffset( new InstructionPointer( start));
        }

        if ( at+oldLength>instructions.size())
            throw new CodeCheckException( "Bad instruction insertion point");
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
        for ( ListIterator i=instructions.listIterator(); i.hasNext();)
        {
            int index=i.nextIndex();
            Instruction current=(Instruction)i.next();
            if ( index<at || index>=at+newCount)
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
 	public CheckedInstruction[] codeCheck()
        throws CodeCheckException
    {
        LinkedList next=new LinkedList();
        CheckedInstruction[] stackArray=
            new CheckedInstruction[instructions.size()];

        /*
         * Initialize next collection with the start point of the
         * method and with any exception handlers
         */
        next.add( new InstructionPointer( 0));
        stackArray[0]=new CheckedInstruction( (Instruction)instructions.get(0),
            new Stack());
        for ( Iterator i=exceptions.iterator(); i.hasNext();)
        {
            int handler=((CodeException)i.next()).handler;
            InstructionPointer ip=new InstructionPointer( handler);
            next.add( ip);
            int index=indexAtOffset( ip);
            stackArray[index]=new CheckedInstruction(
                (Instruction)instructions.get( index), new Stack());
            stackArray[index].stack.push( ProcessStack.CAT1);
        }
        while ( ! next.isEmpty())
        {
            InstructionPointer ip=(InstructionPointer)next.getFirst();
            next.removeFirst();
            try
            {
                int index=indexAtOffset( ip);
                CheckedInstruction currentCheckedInstruction=stackArray[index];
                if ( currentCheckedInstruction==null)
                {
                    throw new
                        CodeCheckException( "Instruction with undefined stack");
                }
                Stack old_stack=currentCheckedInstruction.stack;
                Instruction instruction=currentCheckedInstruction.instruction;
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
                        stackArray[index]=new CheckedInstruction(
                            (Instruction)instructions.get( index), new_stack);
                        next.add( ip);
                    }
                    else
                    {
                        if ( ! new_stack.equals( stackArray[index].stack))
                            throw new CodeCheckException( "Stacks don't match");
                    }
                    stackArray[index].previousCheckedInstructions.add(
                        currentCheckedInstruction);
                }
                // Special processing for jsr opcodes
                if ( instruction.opCode.getMnemonic().startsWith( "jsr"))
                {
                    ip=new InstructionPointer(
                        instruction.getOffsetDestination());
                    index=indexAtOffset( ip);
                    if ( stackArray[index]==null)
                    {
                        new_stack=(Stack)new_stack.clone();
                        new_stack.push( ProcessStack.CAT1);
                        stackArray[index]=new CheckedInstruction(
                            (Instruction)instructions.get( index), new_stack);
                        next.add( ip);
                    }
                    stackArray[index].previousCheckedInstructions.add(
                        currentCheckedInstruction);
                }
            }
            catch ( CodeCheckException cce)
            {
                cce.printStackTrace();
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
            if ( stackArray[i].stack.size()>max_stack)
                max_stack=stackArray[i].stack.size();
        }
        maxStack=max_stack;

        return stackArray;
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

}
