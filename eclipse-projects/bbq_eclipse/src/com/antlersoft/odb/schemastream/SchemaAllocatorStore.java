
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

import com.antlersoft.odb.DiskAllocatorStore;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

public class SchemaAllocatorStore extends DiskAllocatorStore
{
    private SchemaAllocatorStore( File file)
    {
        super( file, 4, 120, 102400, 0);
    }

    public static synchronized SchemaAllocatorStore getSchemaStore( File file,
        Collection classNames)
    {
        HashSet fullNames=new HashSet();
        fullNames.addAll( Arrays.asList( baseClasses));
        fullNames.addAll( classNames);
        schema=new ArrayList( fullNames.size());
        for ( Iterator i=fullNames.iterator(); i.hasNext();)
        {
            try
            {
                schema.add( ObjectStreamClass.lookup( Class.forName( (String)
                    i.next())));
            }
            catch ( ClassNotFoundException cnfe)
            {
System.out.println( "Failed to find "+cnfe.getMessage());
            }
        }
        return new SchemaAllocatorStore( file);
    }

    protected ObjectInputStream createObjectInputStream( InputStream is)
        throws IOException
    {
        return new SchemaInputStream( is, schema);
    }

    protected ObjectOutputStream createObjectOutputStream(
        OutputStream os)
        throws IOException
    {
        return new SchemaOutputStream( os, schema);
    }

    protected void resetObjectInputStream( ObjectInputStream ois)
        throws IOException
    {
        ((SchemaInputStream)ois).resetToSchema();
    }

    protected void resetObjectOutputStream( ObjectOutputStream oos)
        throws IOException
    {
        ((SchemaOutputStream)oos).resetToSchema();
    }

    private static ArrayList schema;

    private static String[] baseClasses={
        "java.lang.Object",
        "java.io.Serializable",
        "java.io.Externalizable",
        "[Ljava.lang.Object;",
        "java.util.Vector",
        "java.util.Collection",
        "java.util.AbstractCollection",
        "java.util.AbstractList",
        "java.util.List",
        "java.util.Map",
        "java.util.ArrayList",
        "java.util.HashMap",
        "java.util.Hashtable",
        "com.antlersoft.odb.ObjectRef",
        "com.antlersoft.odb.DualRef",
        "com.antlersoft.odb.ObjectKey",
        "com.antlersoft.odb.DiskAllocatorStore$DAKey",
        "com.antlersoft.odb.Persistent",
        "com.antlersoft.odb.PersistentHashtable",
        "com.antlersoft.odb.transp.AuxBase",
        "com.antlersoft.odb.transp.TransparentBase"
        };
}