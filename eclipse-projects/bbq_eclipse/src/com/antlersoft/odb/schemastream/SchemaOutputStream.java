
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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class SchemaOutputStream extends ObjectOutputStream
{
    SchemaOutputStream( OutputStream os, ArrayList classDescs)
        throws IOException
    {
        super( os);
        m_desc_map=new HashMap();
        int class_id=0;
        for ( Iterator i=classDescs.iterator(); i.hasNext();
            class_id++)
        {
            m_desc_map.put( i.next(), new Integer( class_id));
        }
    }

    protected void writeClassDescriptor(ObjectStreamClass classdesc)
         throws IOException
    {
        Integer offset=(Integer)m_desc_map.get( classdesc);
        if ( offset==null)
        {
            write( TC_MAX+2);
            super.writeClassDescriptor( classdesc);
        }
        else
        {
            write( TC_MAX+1);
            writeInt( offset.intValue());
        }
    }

    void resetToSchema()
        throws IOException
    {
        super.reset();
    }

    private HashMap m_desc_map;
}