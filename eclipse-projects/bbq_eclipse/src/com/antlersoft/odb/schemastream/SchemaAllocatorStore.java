
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
import com.antlersoft.odb.ObjectStreamCustomizer;

import java.util.Collection;
import java.util.ArrayList;
import java.io.File;

public class SchemaAllocatorStore extends DiskAllocatorStore
{
    public SchemaAllocatorStore( File file, Collection classNames)
    {
        super( file, 4, 120, 102400, 0,
            new SchemaCustomizer( addName( classNames)));
    }

    public static synchronized SchemaAllocatorStore getSchemaStore( File file,
        Collection classNames)
    {
        return new SchemaAllocatorStore( file, classNames);
    }

    static Collection addName( Collection classNames)
    {
        ArrayList result=new ArrayList( classNames.size()+1);
        result.add( classNames);
        result.add( "com.antlersoft.odb.DiskAllocatorStore$DAKey");

        return result;
    }
}