
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

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;

import java.util.ArrayList;
import java.util.NoSuchElementException;

class SchemaInputStream extends ObjectInputStream
{
    SchemaInputStream( InputStream os, ArrayList classDescs)
        throws IOException
    {
        super( os);
        m_class_descs=classDescs;
    }

    protected ObjectStreamClass readClassDescriptor()
        throws IOException, ClassNotFoundException
    {
        if ( read()!=TC_MAX+1)
        {
            return super.readClassDescriptor();
        }
        try
        {
            int offset=readInt();
            return (ObjectStreamClass)m_class_descs.get( offset);
        }
        catch ( NoSuchElementException nsee)
        {
            throw new IOException( "Bad schema identifier");
        }
    }

    void resetToSchema()
        throws IOException
    {
    }

    private ArrayList m_class_descs;
}
