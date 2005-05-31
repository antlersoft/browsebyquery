
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