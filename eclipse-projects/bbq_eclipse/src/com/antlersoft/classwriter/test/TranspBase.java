
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * <p>Copyright (c) 2000, 2003  Michael A. MacDonald<p>
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
package com.antlersoft.classwriter.test;

import java.io.File;
import com.antlersoft.odb.transp.TransparentDB;
import com.antlersoft.odb.schemastream.SchemaAllocatorStore;

public class TranspBase implements QuestionBase
{
    private TransparentDB db;

    private static String[] classList={
        "com.antlersoft.classwriter.test.QuestionNode",
        "com.antlersoft.classwriter.test.BinaryNode",
        "com.antlersoft.classwriter.test.AnimalNode"
    };

    public QuestionNode getTopNode()
    {
        QuestionNode result=(QuestionNode)db.getRootObject( "Top");
        if ( result==null)
        {
            AnimalNode no=new AnimalNode( "squirrel");
            AnimalNode yes=new AnimalNode( "moose");
            result=new BinaryNode( "Does it have antlers?", yes, no);
            db.makeRootObject( "Top", result);
            db.commit();
        }
        return result;
    }

    public void openDB(String[] args)
    {
/*
        db=new TransparentDB( SchemaAllocatorStore.
            getSchemaStore( new File( args[1]),
            java.util.Arrays.asList( classList)));
*/
        db=new TransparentDB( new com.antlersoft.odb.diralloc.DirectoryAllocator(
            new File( args[1])));
    }

    public void closeDB()
    {
        //TODO: Implement this com.antlersoft.classwriter.test.QuestionBase method
        db.commit();
    }

    public void commit()
    {
        //TODO: Implement this com.antlersoft.classwriter.test.QuestionBase method
        db.commit();
    }
}