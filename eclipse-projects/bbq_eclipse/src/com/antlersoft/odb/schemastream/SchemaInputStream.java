
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

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

class SchemaInputStream extends ObjectInputStream
{
    SchemaInputStream( InputStream os, ArrayList classDescs)
        throws IOException
    {
        super( os);
        try
        {
            _wHandle2Object=classDescs;
            Class inputClass=ObjectInputStream.class;
            resetStream=inputClass.getDeclaredMethod( "resetStream", new Class[0]);
            resetStream.setAccessible( true);
            wHandle2Object=inputClass.getDeclaredField( "wireHandle2Object");
            wHandle2Object.setAccessible( true);
            nextWOffset=inputClass.getDeclaredField( "nextWireOffset");
            nextWOffset.setAccessible( true);
        }
        catch ( NoSuchFieldException fielde)
        {
            throw new IOException( fielde.getMessage());
        }
        catch ( NoSuchMethodException methode)
        {
            throw new IOException( methode.getMessage());
        }
    }

    void resetToSchema()
        throws IOException
    {
        try
        {
            resetStream.invoke( this, noArgs);
            wHandle2Object.set( this, _wHandle2Object.clone());
            nextWOffset.setInt( this, _wHandle2Object.size());
        }
        catch ( InvocationTargetException ite)
        {
            if ( ite.getTargetException() instanceof IOException)
                throw (IOException)ite.getTargetException();
            throw new IOException( ite.getTargetException().getMessage());
        }
        catch ( Exception e)
        {
            throw new IOException( e.getMessage());
        }
    }

    private static Object[] noArgs=new Object[0];

    private Method resetStream;
    private Field wHandle2Object; // ArrayList
    private ArrayList _wHandle2Object;
    private Field nextWOffset; // int
}
