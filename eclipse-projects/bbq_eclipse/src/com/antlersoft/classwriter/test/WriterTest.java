
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter.test;

import java.io.*;
import java.util.*;
import classwriter.*;

public class WriterTest
{
    public static void rewrite(String[] args)
    	throws IOException, CodeCheckException
    {
    	ClassWriter writer=new ClassWriter();

     	writer.readClass( new FileInputStream( args[0]));
        for ( Iterator i=writer.getMethods().iterator(); i.hasNext();)
        {
            MethodInfo method=(MethodInfo)i.next();
            CodeAttribute attribute=method.getCodeAttribute();
            int origStack=attribute.getMaxStack();
            System.out.print( method.getName());
            attribute.codeCheck();
            System.out.println( " "+origStack+" "+
                attribute.getMaxStack());
        }
      	writer.writeClass( new FileOutputStream( args[1]));
    }

    public static void main( String[] args)
        throws IOException, CodeCheckException
    {
        ClassWriter writer=new ClassWriter();
        writer.emptyClass( ClassWriter.ACC_PUBLIC, "TestClass", "java/lang/Object");
        MethodInfo newMethod=writer.addMethod( ClassWriter.ACC_PUBLIC|ClassWriter.ACC_STATIC,
            "main", "([Ljava/lang/String;)V");
        CodeAttribute attribute=newMethod.getCodeAttribute();
        int constantIndex=writer.getStringConstantIndex( "It's alive! It's alive!!");
        int fieldRefIndex=writer.getReferenceIndex( ClassWriter.CONSTANT_Fieldref,
            "java/lang/System", "out", "Ljava/io/PrintStream;");
        int methodRefIndex=writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
            "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        ArrayList instructions=new ArrayList();
        // Instruction to read constant
        byte[] operands;

        operands=new byte[2];
        OpCode.intToPair( fieldRefIndex, operands, 0);
        instructions.add( new Instruction(
            OpCode.getOpCodeByMnemonic( "getstatic"), 0, operands, false));
        operands=new byte[1];
        operands[0]=(byte)constantIndex;
        instructions.add( new Instruction( OpCode.getOpCodeByMnemonic( "ldc"),
            0, operands, false));

        operands=new byte[2];
        OpCode.intToPair( methodRefIndex, operands, 0);
        instructions.add( new Instruction(
            OpCode.getOpCodeByMnemonic( "invokevirtual"), 0, operands, false));

        instructions.add( new Instruction(
            OpCode.getOpCodeByMnemonic( "return"), 0, null, false));
        attribute.insertInstructions( 0, 0, instructions);
        attribute.setMaxLocals( 1);
        attribute.codeCheck();
        System.out.println( "constantIndex="+constantIndex+
            " fieldRef="+fieldRefIndex+" methodRef="+methodRefIndex);
        writer.writeClass( new FileOutputStream(
            "c:/cygnus/home/javaodb/classes/TestClass.class"));
        writer.readClass( new FileInputStream(
            "c:/cygnus/home/javaodb/classes/TestClass.class"));
    }
}