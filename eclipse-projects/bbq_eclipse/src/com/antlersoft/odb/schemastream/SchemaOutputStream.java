
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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

class SchemaOutputStream extends ObjectOutputStream
{
    SchemaOutputStream( OutputStream os, ArrayList classDescs)
        throws IOException
    {
        super( os);
        try
        {
            _wHandle2Object=classDescs;
            _wHash2Handle=null;
            Class outputClass=ObjectOutputStream.class;

            assignWOffset=outputClass.getDeclaredMethod( "assignWireOffset", new Class[]
                { Object.class } );
            assignWOffset.setAccessible( true);
            resetStream=outputClass.getDeclaredMethod( "resetStream", new Class[0]);
            resetStream.setAccessible( true);
            wHandle2Object=outputClass.getDeclaredField( "wireHandle2Object");
            wHandle2Object.setAccessible( true);
            nextWOffset=outputClass.getDeclaredField( "nextWireOffset");
            nextWOffset.setAccessible( true);
            wHash2Handle=outputClass.getDeclaredField( "wireHash2Handle");
            wHash2Handle.setAccessible( true);
            wNextHandle=outputClass.getDeclaredField( "wireNextHandle");
            wNextHandle.setAccessible( true);
            wHashSizePower=outputClass.getDeclaredField( "wireHashSizePower");
            wHashSizePower.setAccessible( true);
            wHashLoadFactor=outputClass.getDeclaredField( "wireHashLoadFactor");
            wHashLoadFactor.setAccessible( true);
            wHashCapacity=outputClass.getDeclaredField( "wireHashCapacity");
            wHashCapacity.setAccessible( true);
        }
        catch ( NoSuchFieldException fielde)
        {
            throw new IOException( fielde.getMessage());
        }
        catch ( NoSuchMethodException methode)
        {
methode.printStackTrace();
            throw new IOException( methode.getMessage());
        }
    }

    void resetToSchema()
        throws IOException
    {
        try
        {
            drain();
            resetStream.invoke( this, noArgs);
            if ( _wHash2Handle==null)
            {
                Object[] args=new Object[1];
                for ( Iterator i=_wHandle2Object.iterator(); i.hasNext();)
                {
                    args[0]=i.next();
                    assignWOffset.invoke( this, args);
                }
                _wHandle2Object=(ArrayList)((ArrayList)wHandle2Object.
                    get( this)).clone();
                _nextWOffset=nextWOffset.getInt( this);
                _wHash2Handle=(int[])((int[])wHash2Handle.get( this)).clone();
                _wNextHandle=(int[])((int[])wNextHandle.get( this)).clone();
                _wHashSizePower=wHashSizePower.getInt( this);
                _wHashLoadFactor=wHashLoadFactor.getInt( this);
                _wHashCapacity=wHashCapacity.getInt( this);
            }
            else
            {
                wHandle2Object.set( this, _wHandle2Object.clone());
                nextWOffset.setInt( this, _nextWOffset);
                wHash2Handle.set( this, _wHash2Handle.clone());
                wNextHandle.set( this, _wNextHandle.clone());
                wHashSizePower.setInt( this, _wHashSizePower);
                wHashLoadFactor.setInt( this, _wHashLoadFactor);
                wHashCapacity.setInt( this, _wHashCapacity);
            }
        }
        catch ( InvocationTargetException ite)
        {
            if ( ite.getTargetException() instanceof IOException)
                throw (IOException)ite.getTargetException();
            throw new IOException( ite.getTargetException().getMessage());
        }
        catch ( Exception e)
        {
e.printStackTrace();
            throw new IOException( e.getMessage());
        }
    }

    private static Object[] noArgs=new Object[0];

    private Method assignWOffset;
    private Method resetStream;
    private Field wHandle2Object; // ArrayList
    private ArrayList _wHandle2Object;
    private Field nextWOffset; // int
    private int _nextWOffset;
    private Field wHash2Handle; // int[]
    private int[] _wHash2Handle;
    private Field wNextHandle;  // int[]
    private int[] _wNextHandle;
    private Field wHashSizePower;  // int
    private int _wHashSizePower;
    private Field wHashLoadFactor; // int
    private int _wHashLoadFactor;
    private Field wHashCapacity; // int
    private int _wHashCapacity;
}