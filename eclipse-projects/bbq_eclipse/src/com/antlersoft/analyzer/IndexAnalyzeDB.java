
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.analyzer;

import java.io.Serializable;
import java.io.File;

import java.util.ArrayList;
import java.util.Enumeration;

import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectStreamCustomizer;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.odb.diralloc.DirectoryAllocator;
import com.antlersoft.odb.diralloc.CustomizerFactory;

import com.antlersoft.odb.schemastream.SchemaCustomizer;

import com.antlersoft.util.IteratorEnumeration;

public class IndexAnalyzeDB implements AnalyzerDB
{
    private IndexObjectDB _session;
    private int createCount;
    private static String LOOKUP_INDEX="Lookup";

    public void openDB(String dbName) throws Exception
    {
        if ( _session!=null)
            closeDB();
        createCount=0;
        _session=new IndexObjectDB( new DirectoryAllocator( new File( dbName),
            new CFactory()));
        try
        {
            _session.defineIndex( LOOKUP_INDEX, Lookup.class,
                new TypeKeyGenerator(), false, true);
        }
        catch ( IndexExistsException iee)
        {
            // Don't care if index already exists
        }
    }

    public void closeDB() throws Exception
    {
        _session.close();
        _session=null;
    }

    public Object getWithKey(String type, String key) throws Exception
    {
        Object result=findWithKey( type, key);
        if ( result==null)
        {
try
{
			result=Class.forName( type).getConstructor( new Class[]
                { String.class, AnalyzerDB.class }).
                newInstance( new Object[] { key, this });
}
catch ( java.lang.reflect.InvocationTargetException ite)
{
ite.getTargetException().printStackTrace();
}
            Lookup keyReference=new Lookup( type, key, (Persistent)result);
			createCount++;
			if ( createCount==1000)
			{
				_session.commitAndRetain();
				createCount=0;
			}
        }
        return result;
    }

    public Object findWithKey(String type, String key) throws Exception
    {
        Lookup l=(Lookup)_session.findObject( LOOKUP_INDEX,
            new TypeKey( type, key));
        if ( l==null)
            return null;
        return l.object.getReferenced();
    }

    public Enumeration getAll(String type) throws Exception
    {
        return new IteratorEnumeration( _session.getAll( Class.forName( type)));
    }

    static class TypeKey implements Comparable, Serializable
    {
        private String type;
        private String key;

        TypeKey( String t, String k)
        {
            type=t;
            key=k;
        }

        public int compareTo( Object toCompare)
        {
            TypeKey compareKey=(TypeKey)toCompare;
            int result=type.compareTo( compareKey.type);
            if ( result==0)
                result=key.compareTo( compareKey.key);
            return result;
        }
    }

    static class Lookup implements Persistent
    {
        private transient PersistentImpl _impl;
        private TypeKey key;
        ObjectRef object;

        Lookup( String t, String k, Persistent p)
        {
            key=new TypeKey( t, k);
            object=new ObjectRef( p);
            _impl=new PersistentImpl();
            IndexObjectDB.makePersistent( this);
        }

        public PersistentImpl _getPersistentImpl()
        {
            if ( _impl==null)
                _impl=new PersistentImpl( this);
            return _impl;
        }
    }

    static class CFactory extends CustomizerFactory
    {
        ObjectStreamCustomizer getCustomizer( Class toStore)
        {
            ArrayList nameList=new ArrayList();
            nameList.add( "java.lang.Comparable");
            nameList.add( "[Ljava.lang.Comparable;");
            nameList.add( "[I");
            nameList.add( "com.antlersoft.odb.diralloc.DAKey");
            nameList.add( "com.antlersoft.analyzer.DBReference");
            nameList.add( "com.antlersoft.analyzer.DBFieldReference");
            nameList.add( "com.antlersoft.analyzer.DBCall");
            if ( toStore==Lookup.class)
            {
                nameList.add( "com.antlersoft.analyzer.IndexAnalyzeDB$TypeKey");
            }
            nameList.add( toStore.getName());
            return new SchemaCustomizer( nameList);
        }
    }

    static class TypeKeyGenerator implements KeyGenerator, Serializable
    {
        public Comparable generateKey( Object keyed)
        {
            return ((Lookup)keyed).key;
        }
    }
}