
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.postproc;

import com.antlersoft.classwriter.*;

import java.io.*;

import java.util.*;

public class PostProcessor
{
    final int PP_PROCESSED=1;
    final int PP_PERSISTABLE=2;
    final int PP_PERSISTENT=4;
    final int PP_PERSISTENT_BASE=8;

    private TreeMap processMap;

    private ArrayList invariants;

    private String[] baseInvariants={
        "java/lang/Boolean",
        "java/lang/Byte",
        "java/lang/Character",
        "java/lang/Class",
        "java/lang/Double",
        "java/lang/Float",
        "java/lang/Integer",
        "java/lang/Long",
        "java/lang/Short",
        "java/lang/String"
    };

    private static OpCode putfield;
    private static OpCode getfield;
    private static OpCode invokespecial;

    static
    {
        try
        {
            putfield=OpCode.getOpCodeByMnemonic( "putfield");
            getfield=OpCode.getOpCodeByMnemonic( "getfield");
            invokespecial=OpCode.getOpCodeByMnemonic( "invokespecial");
        }
        catch ( CodeCheckException cce)
        {
            throw new IllegalStateException( "Bad mnemonics");
        }
    }

    PostProcessor()
    {
        processMap=new TreeMap( Collections.reverseOrder());
        invariants=new ArrayList();
        invariants.addAll( Arrays.asList( baseInvariants));
    }

    private int getStatusFlags( String className)
    {
        if ( className.equals( "java.lang.Object"))
            return PP_PERSISTENT_BASE;
        for ( Iterator i=processMap.tailMap( className).entrySet().iterator();
            i.hasNext();)
        {
            Map.Entry entry=(Map.Entry)i.next();
            if ( className.startsWith( (String)entry.getKey()))
                return ((Integer)entry.getValue()).intValue();
        }
        return 0;
    }

    private void readConfigFile( BufferedReader reader)
        throws IOException
    {
        for ( String line=reader.readLine(); line!=null; line=reader.readLine())
        {
            if ( line.length()>0 && ! line.startsWith( "#"))
            {
                StringTokenizer tokens=new StringTokenizer( line);
                if ( tokens.hasMoreTokens())
                {
                    String firstToken=tokens.nextToken();
                    if ( tokens.hasMoreTokens())
                    {
                        int value=0;
                        switch ( tokens.nextToken().charAt( 0))
                        {
                            case 'P' :
                            case 'p' :
                                value=PP_PROCESSED|PP_PERSISTABLE|PP_PERSISTENT;
                                break;
                            case 'C' :
                            case 'c' :
                                value=PP_PROCESSED|PP_PERSISTABLE;
                                break;
                            case 'A' :
                            case 'a' :
                                value=PP_PROCESSED;
                                break;
                        }
                        if ( firstToken.endsWith( "*"))
                            firstToken=firstToken.substring( 0,
                                firstToken.length()-1);
                        processMap.put( firstToken, new Integer( value));
                    }
                }
            }
        }
/*
for ( Iterator i=processMap.entrySet().iterator(); i.hasNext();)
{
    Map.Entry me=(Map.Entry)i.next();
    System.out.println( ((String)me.getKey())+" "+me.getValue().toString());
}
*/
    }

    private int getTypeStatusFlags( String typeString)
    {
        int flags=0;
        if ( typeString.charAt( 0)=='L')
        {
            flags=getStatusFlags( TypeParse.convertFromInternalClassName(
                new String( typeString.substring( 1, typeString.length()-1))));
        }
        return flags;
    }

    public void transformClass( ClassWriter writer, ClassWriter[] newWriter)
        throws PostProcessingException, CodeCheckException
    {
        ArrayList movedFields=new ArrayList();
        int currentClassIndex=writer.getCurrentClassIndex();
        int statusFlags=getStatusFlags( writer.getClassName(
            currentClassIndex));
        int baseClassIndex=writer.getSuperClassIndex();
        int baseStatusFlags=getStatusFlags(
            writer.getClassName( baseClassIndex));
        String newClassName=null;
        String newBaseClassName=null;

        // Create aux class if necessary
        if ( ( statusFlags & PP_PERSISTABLE)!=0)
        {
            if ( ( baseStatusFlags & PP_PERSISTABLE)==0 &&
                ! writer.getInternalClassName( baseClassIndex).
                    equals( "java/lang/Object"))
                throw new PostProcessingException(
                    "Base class must be java.lang.Object or persistable");
            newClassName=writer.getInternalClassName(
                currentClassIndex)+"_odb_fields";
            char[] newClassArray=newClassName.toCharArray();
            for ( int i=0; i<newClassArray.length; i++)
            {
                if ( newClassArray[i]=='$')
                    newClassArray[i]='_';
            }
            newClassName=new String( newClassArray);
            if ( ( baseStatusFlags & PP_PERSISTABLE)!=0)
                newBaseClassName=writer.getInternalClassName(
                    baseClassIndex)+"_odb_fields";
            else
                newBaseClassName="com/antlersoft/odb/transp/AuxBase";
            newWriter[0]=new ClassWriter();
            newWriter[0].emptyClass( 0, newClassName, newBaseClassName);

            // Create constructor for aux class
            // (just calls base class constructor)
            MethodInfo newConstructor=newWriter[0].addMethod(
                0, "<init>", "()V");
            CodeAttribute constructorCode=newConstructor.getCodeAttribute();
            ArrayList constructorInstructions=new ArrayList();
            constructorInstructions.add( new Instruction(
                OpCode.getOpCodeByMnemonic(
                "aload_0"), 0, null, false));
            Instruction.addNextInstruction( constructorInstructions,
                "invokespecial", newWriter[0].getReferenceIndex(
                ClassWriter.CONSTANT_Methodref,
                newBaseClassName, "<init>", "()V"));
            Instruction.addNextInstruction( constructorInstructions,
                "return", null, false);
            constructorCode.insertInstructions( 0, 0, constructorInstructions);
            constructorCode.setMaxLocals( 1);
            constructorCode.codeCheck();

            // Create _odb_getTransparent for aux class
            // (Constructs main class with this as argument)
            MethodInfo getTransparentMethod=newWriter[0].addMethod(
                0, "_odb_newTransparent", "()Ljava/lang/Object;");
            CodeAttribute getTransparentCode=getTransparentMethod.
                getCodeAttribute();
            ArrayList transpInstructions=new ArrayList();
            Instruction.addNextInstruction( transpInstructions, "new",
                newWriter[0].getClassIndex(
                    writer.getInternalClassName( currentClassIndex)));
            Instruction.addNextInstruction( transpInstructions, "dup",
                null, false);
            Instruction.addNextInstruction( transpInstructions,
                "aload_0", null, false);
            Instruction.addNextInstruction( transpInstructions,
                "invokespecial",
                newWriter[0].getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                writer.getInternalClassName( currentClassIndex),
                "<init>", "(L"+newClassName+";)V"));
            Instruction.addNextInstruction( transpInstructions,
                "areturn", null, false);
            getTransparentCode.setMaxLocals( 1);
            getTransparentCode.insertInstructions( 0, 0, transpInstructions);
            getTransparentCode.codeCheck();

            // Move fields to aux class
            for ( Iterator i=writer.getFields().iterator(); i.hasNext();)
            {
                FieldInfo field=(FieldInfo)i.next();
                if ( ( field.getFlags() & ( ClassWriter.ACC_STATIC ))!=0)
                    continue;
                movedFields.add( field);
                String fieldType=field.getType();
                newWriter[0].addField( 0, field.getName(), fieldType);
             }
        }
        // Transform methods to call getter/setter instead of putfield getfield
        if ( ( statusFlags & PP_PROCESSED)!=0)
        {
            ArrayList callInstructions=new ArrayList();
            for ( Iterator methodIter=writer.getMethods().iterator();
                methodIter.hasNext();)
            {
                MethodInfo method=(MethodInfo)methodIter.next();
                CodeAttribute code=method.getCodeAttribute();
                if ( code==null)
                    continue;
                CheckedInstruction[] checked=code.codeCheck();
                for ( int i=0; i<checked.length; i++)
                {
                    Instruction instruction=checked[i].instruction;
                    OpCode opCode=instruction.getOpCode();
                    if ( opCode==putfield ||
                        opCode==getfield)
                    {
                        ClassWriter.CPTypeRef fieldRef=writer.getTypeRef(
                            instruction.operandsAsInt());
                        int fieldFlags=getStatusFlags( writer.getClassName(
                            fieldRef.getClassIndex()));
                        if ( ( fieldFlags & PP_PERSISTABLE)!=0)
                        {
                            Instruction.addNextInstruction( callInstructions,
                                "invokevirtual",
                                ( opCode==getfield ?
                                writer.getReferenceIndex(
                                ClassWriter.CONSTANT_Methodref,
                                writer.getInternalClassName(
                                fieldRef.getClassIndex()),
                                "_odb_get"+fieldRef.getSymbolName(),
                                "()"+fieldRef.getSymbolType()) :
                                writer.getReferenceIndex(
                                ClassWriter.CONSTANT_Methodref,
                                writer.getInternalClassName(
                                fieldRef.getClassIndex()),
                                "_odb_put"+fieldRef.getSymbolName(),
                                "("+fieldRef.getSymbolType()+")V")));
                            code.insertInstructions(
                                instruction.getInstructionStart(), 1,
                                callInstructions);
                            callInstructions.clear();
                        }
                    }
                }
                code.codeCheck();
            }
        }
        // Add getter/setter methods--
        // Add constructor that takes aux class
        // If base class is java/lang/Object, change base class
        // to TranspBase
        if ( ( statusFlags & PP_PERSISTABLE)!=0)
        {
            writer.removeFromFields( movedFields);
            // Add getter/setter for each moved field
            for ( Iterator i=movedFields.iterator(); i.hasNext();)
            {
                FieldInfo field=(FieldInfo)i.next();
                String fieldName=field.getName();
                String fieldType=field.getType();
                boolean dirtyOnGet=false;
                String parsedType=TypeParse.parseFieldType( fieldType);
                if ( parsedType==TypeParse.ARG_ARRAYREF)
                    dirtyOnGet=true;
                if ( TypeParse.parseFieldType( fieldType)==TypeParse.ARG_OBJREF)
                {
                    String fieldClass=fieldType.substring( 1,
                        fieldType.length()-1);
                    if ( ( getStatusFlags(
                        TypeParse.convertFromInternalClassName( fieldClass)) &
                        PP_PERSISTABLE)==0)
                    {
                        if ( ! invariants.contains( fieldClass))
                            dirtyOnGet=true;
                    }
                }
                MethodInfo method=writer.addMethod(
                    ClassWriter.ACC_PUBLIC|ClassWriter.ACC_FINAL,
                    "_odb_get"+fieldName, "()"+fieldType);
                CodeAttribute code=method.getCodeAttribute();
                ArrayList instructions=new ArrayList();
                Instruction.addNextInstruction( instructions, "aload_0", null,
                    false);
                Instruction.addNextInstruction( instructions, "getfield",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Fieldref,
                        "com/antlersoft/odb/transp/TransparentBase",
                        "_odb_ref", "Lcom/antlersoft/odb/ObjectRef;"));
                Instruction.addNextInstruction( instructions, "invokevirtual",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                        "com/antlersoft/odb/ObjectRef", "getReferenced",
                        "()Ljava/lang/Object;"));
                Instruction.addNextInstruction( instructions, "checkcast",
                    writer.getClassIndex( newClassName));
                if ( dirtyOnGet)
                {
                    Instruction.addNextInstruction( instructions,
                    "dup", null, false);
                    Instruction.addNextInstruction( instructions,
                    "invokestatic",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                    "com/antlersoft/odb/ObjectDB",
                    "makeDirty",
                    "(Lcom/antlersoft/odb/Persistent;)V"));
                }
                Instruction.addNextInstruction( instructions, "getfield",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Fieldref,
                    newClassName, field.getName(), field.getType()));
                Instruction.addNextInstruction( instructions,
                    TypeParse.getOpCodePrefix( parsedType)+"return", null,
                    false);
                code.insertInstructions( 0, 0, instructions);
                code.setMaxLocals( 1);
                code.codeCheck();
                instructions.clear();
                method=writer.addMethod(
                    ClassWriter.ACC_PUBLIC|ClassWriter.ACC_FINAL,
                    "_odb_put"+field.getName(), "("+fieldType+")V");
                code=method.getCodeAttribute();
                Instruction.addNextInstruction( instructions, "aload_0", null,
                    false);
                Instruction.addNextInstruction( instructions, "getfield",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Fieldref,
                        "com/antlersoft/odb/transp/TransparentBase",
                        "_odb_ref", "Lcom/antlersoft/odb/ObjectRef;"));
                Instruction.addNextInstruction( instructions, "invokevirtual",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                        "com/antlersoft/odb/ObjectRef", "getReferenced",
                        "()Ljava/lang/Object;"));
                Instruction.addNextInstruction( instructions, "checkcast",
                    writer.getClassIndex( newClassName));
                Instruction.addNextInstruction( instructions,
                "dup", null, false);
                Instruction.addNextInstruction( instructions,
                "invokestatic",
                writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                "com/antlersoft/odb/ObjectDB",
                "makeDirty",
                "(Lcom/antlersoft/odb/Persistent;)V"));
                Instruction.addNextInstruction( instructions,
                    TypeParse.getOpCodePrefix( parsedType)+"load_1",
                    null, false);
                Instruction.addNextInstruction( instructions, "putfield",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Fieldref,
                    newClassName, field.getName(), fieldType));
                Instruction.addNextInstruction( instructions, "return", null,
                    false);
                code.insertInstructions( 0, 0, instructions);
                code.setMaxLocals( ( parsedType==TypeParse.ARG_LONG ||
                    parsedType==TypeParse.ARG_DOUBLE) ? 3 : 2);
                code.codeCheck();
                instructions.clear();
            }
            // Add method that constructs and returns new aux class
            CodeAttribute code=writer.addMethod( ClassWriter.ACC_PROTECTED,
                "_odb_aux", "()Lcom/antlersoft/odb/transp/AuxBase;").
                getCodeAttribute();
            ArrayList instructions=new ArrayList();
            Instruction.addNextInstruction( instructions,
                "new", writer.getClassIndex( newClassName));
            Instruction.addNextInstruction( instructions,
                "dup", null, false);
            Instruction.addNextInstruction( instructions,
                "invokespecial", writer.getReferenceIndex(
                ClassWriter.CONSTANT_Methodref, newClassName,
                "<init>", "()V"));
            // Make new aux persistent if this class is always persistent
            if ( ( statusFlags & PP_PERSISTENT)!=0)
            {
                Instruction.addNextInstruction( instructions,
                    "dup", null, false);
                Instruction.addNextInstruction( instructions,
                    "invokestatic",
                    writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                        "com/antlersoft/odb/ObjectDB", "makePersistent",
                        "(Ljava/lang/Object;)V"));
            }
            Instruction.addNextInstruction( instructions,
                "areturn", null, false);
            code.insertInstructions( 0, 0, instructions);
            code.setMaxLocals( 1);
            code.codeCheck();
            instructions.clear();
            // If base class is object
            if ( writer.getInternalClassName( baseClassIndex).
                equals( "java/lang/Object"))
            {
                // Change all constructors that call Object constructor
                // to call TransparentBase constructor instead
                for ( Iterator mi=writer.getMethods().iterator(); mi.hasNext();)
                {
                    MethodInfo method=(MethodInfo)mi.next();
                    if ( method.getName().equals( "<init>"))
                    {
                        code=method.getCodeAttribute();
                        CheckedInstruction[] checked=code.codeCheck();
                        for ( int i=0; i<checked.length; i++)
                        {
                            if ( checked[i].instruction.getOpCode()==
                                invokespecial)
                            {
                                ClassWriter.CPTypeRef specialMethodRef=
                                    writer.getTypeRef( checked[i].instruction.
                                    operandsAsInt());
                                if ( specialMethodRef.getSymbolName().
                                    equals( "<init>"))
                                {
                                    String initClass=
                                        writer.getInternalClassName(
                                        specialMethodRef.getClassIndex());
                                    if ( initClass.equals( "java/lang/Object"))
                                    {
                                        Instruction.addNextInstruction(
                                            instructions, "invokespecial",
                                            writer.getReferenceIndex(
                                            ClassWriter.CONSTANT_Methodref,
                                    "com/antlersoft/odb/transp/TransparentBase",
                                            "<init>", "()V"));
                                        code.insertInstructions(
                                            checked[i].instruction.
                                            getInstructionStart(),
                                            1, instructions);
                                        instructions.clear();
                                        code.codeCheck();
                                        break;
                                    }
                                    else if ( initClass.equals(
                                        writer.getInternalClassName(
                                        currentClassIndex)))
                                        break;
                                }
                            }
                        }
                    }
                }
                // Change base class to TransparentBase
                writer.setBase( "com/antlersoft/odb/transp/TransparentBase");
            }
            // Add constructor that takes aux class
            code=writer.addMethod( ClassWriter.ACC_PROTECTED,
                "<init>", "(L"+newClassName+";)V").getCodeAttribute();
            Instruction.addNextInstruction( instructions, "aload_0", null,
                false);
            Instruction.addNextInstruction( instructions, "aload_1", null,
                false);
            Instruction.addNextInstruction( instructions, "invokespecial",
                writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
                writer.getInternalClassName( writer.getSuperClassIndex()),
                "<init>", "(L"+newBaseClassName+";)V"));
            Instruction.addNextInstruction( instructions, "return",
                null, false);
            code.insertInstructions( 0, 0, instructions);
            code.setMaxLocals( 2);
            code.codeCheck();
            instructions.clear();
        }
    }

    private void convertClasses( File source, File destination)
        throws PostProcessingException, CodeCheckException, IOException
    {
        Stack sourceStack=new Stack();
        Stack destinationStack=new Stack();
        sourceStack.push( source);
        destinationStack.push( destination);
        while ( ! sourceStack.isEmpty())
        {
            source=(File)sourceStack.pop();
            destination=(File)destinationStack.pop();
            if ( ! destination.exists())
                destination.mkdirs();
            File[] files=source.listFiles();
            for ( int i=0; i<files.length; i++)
            {
                File current=(File)files[i];
                if ( current.isDirectory())
                {
                    sourceStack.push( current);
                    destinationStack.push( new File( destination,
                        current.getName()));
                }
                else if ( current.getName().endsWith( ".class"))
                {
                    ClassWriter writer=new ClassWriter();
                    InputStream is=new BufferedInputStream(
                        new FileInputStream( current));
                    writer.readClass( is);
                    is.close();
                    if ( ( getStatusFlags(
                        writer.getClassName( writer.getCurrentClassIndex()))
                        & PP_PROCESSED)!=0)
                    {
                        ClassWriter[] auxWriter=new ClassWriter[1];
                        transformClass( writer, auxWriter);
                        File output=new File( destination, current.getName());
                        OutputStream os=new BufferedOutputStream(
                            new FileOutputStream( output));
                        writer.writeClass( os);
                        os.close();
                        if ( auxWriter[0]!=null)
                        {
                            String className=auxWriter[0].getClassName(
                                auxWriter[0].getCurrentClassIndex());
                            className=className.substring(
                                className.lastIndexOf( '.')+1,
                                className.length());
                            output=new File( destination,
                                className+".class");
                            os=new BufferedOutputStream(
                                new FileOutputStream( output));
                            auxWriter[0].writeClass( os);
                            os.close();
                        }
                    }
                }
            }
        }
    }

    public static void main( String[] args)
        throws IOException, CodeCheckException, PostProcessingException
    {
        PostProcessor pp=new PostProcessor();
        BufferedReader reader=new BufferedReader( new FileReader( args[0]));
        pp.readConfigFile( reader);
        reader.close();
        pp.convertClasses( new File( args[1]), new File( args[2]));
    }
}