
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.postproc;

import com.antlersoft.classwriter.*;

import java.io.*;

import java.util.*;

public class PostProcessor
{
    final int PP_PROCESSED=1;
    final int PP_PERSISTABLE=2;
    final int PP_PERSISTENT=4;
    final int PP_PERSISTENT_BASE=8;

    private TreeMap processMap;
    private HashSet persistentBase;

    PostProcessor()
    {
        processMap=new TreeMap( Collections.reverseOrder());
        HashSet persistentBase=new HashSet();
    }

    private int getStatusFlags( String className)
    {
        if ( persistentBase.contains( className))
            return PP_PERSISTENT_BASE;
        for ( Iterator i=processMap.tailMap( className).entrySet().iterator();
            i.hasNext();)
        {
            Map.Entry entry=(Map.Entry)i.next();
            if ( className.startsWith( (String)entry.getKey()))
                return ((Integer)entry.getValue()).intValue();
        }
        return 0;
    }

    private void readConfigFile( BufferedReader reader)
        throws IOException
    {
        persistentBase.clear();
        processMap.clear();

        for ( String line=reader.readLine(); line!=null; line=reader.readLine())
        {
            if ( line.length()>0 && ! line.startsWith( "#"))
            {
                StringTokenizer tokens=new StringTokenizer( line);
                if ( tokens.hasMoreTokens())
                {
                    String firstToken=tokens.nextToken();
                    if ( tokens.hasMoreTokens())
                    {
                        int value=0;
                        switch ( tokens.nextToken().charAt( 0))
                        {
                            case 'P' :
                                value=PP_PROCESSED|PP_PERSISTABLE|PP_PERSISTENT;
                                break;
                            case 'C' :
                                value=PP_PROCESSED|PP_PERSISTABLE;
                                break;
                            case 'A' :
                                value=PP_PROCESSED;
                                break;
                        }
                        processMap.put( firstToken, new Integer( value));
                    }
                    else
                    {
                        persistentBase.add( firstToken);
                    }
                }
            }
        }
    }

    private int getTypeStatusFlags( String typeString)
    {
        int flags=0;
        if ( typeString.charAt( 0)=='L')
        {
            char[] typeClass=typeString.substring( 1, typeString.length()-2).
                toCharArray();
            int len=typeClass.length;
            for ( int i=0; i<len; i++)
            {
                if ( typeClass[i]=='/' || typeClass[i]=='$')
                    typeClass[i]='.';
            }
            flags=getStatusFlags( new String( typeClass));
        }
        return flags;
    }

    public void transformClass( ClassWriter writer)
    {
        int statusFlags=getStatusFlags( writer.getClassName(
            writer.getCurrentClassIndex()));
        if ( ( statusFlags & PP_PERSISTABLE)!=0)
        {
            // Re-type fields
            for ( Iterator i=writer.getFields().iterator(); i.hasNext();)
            {
                FieldInfo field=(FieldInfo)i.next();
                int flags=getTypeStatusFlags( field.getType());
                if ( ( flags & PP_PERSISTABLE)!=0)
                {
                    field.setType( "odb/ObjectRef");
                }
                if ( ( flags & PP_PERSISTENT_BASE)!=0)
                {
                    field.setType( "odb/DualRef");
                }
            }
        }
        // Transform methods
        if ( ( statusFlags & PP_PROCESSED)!=0)
        {
        }
    }
}