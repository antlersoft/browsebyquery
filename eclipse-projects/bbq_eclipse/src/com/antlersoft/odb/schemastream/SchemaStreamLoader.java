
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.schemastream;

import com.antlersoft.classwriter.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import java.util.ArrayList;

public class SchemaStreamLoader extends ClassLoader
{
    public SchemaStreamLoader()
    {
        super();
    }

    protected Class loadClass( String className, boolean resolve)
        throws ClassNotFoundException
    {
        Class result;

        result=findLoadedClass( className);
        try
        {
            if ( result==null)
            {
                if ( className.equals( "java.io.ObjectInputStream"))
                {
                    ClassWriter inputWriter=getSystemClassWriter( className);
                    editObjectInputStream( inputWriter);
                    defineClass( inputWriter);
                }
                else if ( className.equals( "java.io.ObjectOutputStream"))
                {
                    ClassWriter outputWriter=getSystemClassWriter( className);
                    editObjectOutputStream( outputWriter);
                    defineClass( outputWriter);
                }
                else
                    result=super.loadClass( className, resolve);
            }
        }
        catch ( IOException ioe)
        {
            throw new ClassNotFoundException( className, ioe);
        }
        catch ( CodeCheckException cce)
        {
            throw new ClassNotFoundException( className, cce);
        }
        if ( result!=null && resolve)
            resolveClass( result);

        return result;
    }

    private ClassWriter getSystemClassWriter( String className)
        throws IOException, CodeCheckException, ClassNotFoundException
    {
        InputStream is=getSystemResourceAsStream( className);
        if ( is==null)
            throw new ClassNotFoundException( className);
        ClassWriter writer=new ClassWriter();
        writer.readClass( is);
        is.close();
        return writer;
    }

    private Class defineClass( ClassWriter toDefine)
        throws IOException
    {
        ByteArrayOutputStream os=new ByteArrayOutputStream();

        toDefine.writeClass( os);
        byte[] classBuf=os.toByteArray();
        return defineClass( toDefine.getClassName(
            toDefine.getCurrentClassIndex()), classBuf, 0, classBuf.length);
    }

    private void editObjectInputStream( ClassWriter writer)
        throws CodeCheckException
    {
        MethodInfo resetSchema=writer.addMethod( ClassWriter.ACC_PUBLIC,
            "resetSchema", "(Ljava/util/ArrayList;)V");
        ArrayList instructions=new ArrayList();
        Instruction.addNextInstruction( instructions, "aload_0", null,
            false);
        Instruction.addNextInstruction( instructions, "dup", null, false);
        Instruction.addNextInstruction( instructions, "aload_1", null, false);
        Instruction.addNextInstruction( instructions, "putfield",
            writer.getReferenceIndex( ClassWriter.CONSTANT_Fieldref,
            "java/io/ObjectInputStream", "wireHandle2Object",
            "Ljava/util/ArrayList;"));
        Instruction.addNextInstruction( instructions, "aload_1", null, false);
        Instruction.addNextInstruction( instructions, "invokevirtual",
            writer.getReferenceIndex( ClassWriter.CONSTANT_Methodref,
            "java/util/ArrayList", "size", "()V"));
    }

    private void editObjectOutputStream( ClassWriter writer)
        throws CodeCheckException
    {
    }
}