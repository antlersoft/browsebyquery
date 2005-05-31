
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
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
