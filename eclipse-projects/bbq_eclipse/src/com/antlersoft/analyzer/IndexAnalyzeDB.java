
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
package com.antlersoft.analyzer;

import java.io.Serializable;
import java.io.File;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.antlersoft.odb.IndexIterator;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectStreamCustomizer;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.odb.diralloc.CustomizerFactory;
import com.antlersoft.odb.diralloc.DirectoryAllocator;
import com.antlersoft.odb.diralloc.IndexStatistics;
import com.antlersoft.odb.diralloc.NoSuchClassException;

import com.antlersoft.odb.schemastream.SchemaCustomizer;

import com.antlersoft.query.EmptyEnum;

import com.antlersoft.util.IteratorEnumeration;

public class IndexAnalyzeDB implements AnalyzerDB
{
    private IndexObjectDB _session;
    private int createCount;
    private HashMap _constructorMap;
    private int _captureOptional;
    private static Logger _logger=Logger.getLogger( IndexAnalyzeDB.class.getName());
    private static final String LOOKUP_INDEX="Lookup";
    private static String OPTIONAL_FLAGS_KEY="optional_flags";
    private static Class[] _argumentList=new Class[] { String.class, AnalyzerDB.class };
    
    private static Properties LOOKUP_INDEX_PROPS;
    private static Properties TYPEKEY_INDEX_PROPS;
    private static Properties CLASSNAME_INDEX_PROPS;
    
    static
    {
    	LOOKUP_INDEX_PROPS=new Properties();
    	LOOKUP_INDEX_PROPS.put( DirectoryAllocator.ENTRIES_PER_PAGE, "80");
    	TYPEKEY_INDEX_PROPS=new Properties();
    	TYPEKEY_INDEX_PROPS.put( DirectoryAllocator.ENTRIES_PER_PAGE, "220");
    	CLASSNAME_INDEX_PROPS=new Properties();
    	CLASSNAME_INDEX_PROPS.put( DirectoryAllocator.ENTRIES_PER_PAGE, "150");
    }

	public IndexAnalyzeDB()
	{
		_session=null;
		_constructorMap=new HashMap();
        createCount=0;
	}

    public void openDB(String dbName) throws Exception
    {
        if ( _session!=null)
            closeDB();
        createCount=0;
        _session=new IndexObjectDB( new DirectoryAllocator( new File( dbName),
            new CFactory()));
        Integer optional_flags=(Integer)_session.getRootObject( OPTIONAL_FLAGS_KEY);
        if ( optional_flags!=null)
        	_captureOptional=optional_flags.intValue();
        else
        	_captureOptional=AnalyzerDB.OPTIONAL_TYPE_INFO;
        try
        {
            _session.defineIndex( LOOKUP_INDEX, Lookup.class,
                new TypeKeyGenerator(), false, true, LOOKUP_INDEX_PROPS);
            _session.defineIndex( DBClass.CLASS_NAME_INDEX, DBClass.class,
            	new DBClass.NameKeyGenerator(), false, true, CLASSNAME_INDEX_PROPS);
            _session.defineIndex( DBField.FIELD_TYPE_INDEX, DBField.class,
            		new DBField.FieldTypeKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
            _session.defineIndex( DBMethod.RETURN_TYPE_INDEX, DBMethod.class,
            		new DBMethod.ReturnTypeKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
            _session.defineIndex( DBArgument.ARGUMENT_TYPE_INDEX, DBArgument.class,
            		new DBArgument.ArgumentTypeKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
            _session.defineIndex( DBType.TYPE_CLASS_INDEX, DBType.class,
            		new DBType.TypeClassKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
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

    public synchronized void clearDB( String dbName)
        throws Exception
    {
        // Ignore exceptions closing the database
        try
        {
            if (_session != null)
                closeDB();
        }
        catch ( Exception e)
        {

        }
        File f=new File( dbName);
        if ( f.exists())
        {
            File[] children=f.listFiles();
            for ( int i=0; i<children.length; ++i)
                children[i].delete();
            f.delete();
        }
        openDB( dbName);
    }

    public Object getWithKey(String type, String key) throws Exception
    {
        Object result=findWithKey( type, key);
        if ( result==null)
        {
			result=newInstance( type, key);
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
    
    public Object findWithIndex( String index_name, Comparable key) throws Exception
    {
    	return _session.findObject( index_name, key);
    }

    public Enumeration getAll(String type) throws Exception
    {
        Enumeration result;
        try
        {
            result = new IteratorEnumeration(_session.getAll(Class.forName(type)));
        }
        catch ( NoSuchClassException nsce)
        {
            result = EmptyEnum.empty;
        }
        return result;
    }
    
    public boolean captureOptional( int optional_flag)
    {
    	return ( optional_flag & _captureOptional)!=0;
    }
    
    public void setCaptureOptional( int flags)
    {
    	if ( _session!=null)
    	{
    		_captureOptional=flags;
    		_session.makeRootObject( OPTIONAL_FLAGS_KEY, new Integer( _captureOptional));
    	}
    }

    public synchronized void makeCurrent()
    {
        _session.makeCurrent();
    }

	public static void printTypeKey( Comparable x)
	{
	    TypeKey y=(TypeKey)x;
	    System.out.println( y.type+"|"+y.key);
	}
	
	public Enumeration retrieveByIndex( String index_name, Comparable key)
	{
		Enumeration result=EmptyEnum.empty;
		
		if ( _session!=null)
		{
			final IndexIterator ii=_session.greaterThanOrEqualEntries( index_name, key);
			if ( ii.isExactMatch())
			{
				result=new ExactMatchIndexEnumeration( ii);
			}
		}
		
		return result;
	}
	
	private Object newInstance( String type, String key)
	throws Exception
	{
		Constructor c=(Constructor)_constructorMap.get( type);
		if ( c==null)
		{
			c=Class.forName( type).getConstructor( _argumentList);
			_constructorMap.put( type, c);
		}
		return c.newInstance( new Object[] { key, this });		
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
        /**
		 * 
		 */
		private static final long serialVersionUID = 5675201888813610421L;
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
        public ObjectStreamCustomizer getCustomizer( Class toStore)
        {
            ArrayList nameList=new ArrayList();
            nameList.add( "java.lang.Comparable");
            nameList.add( "[Ljava.lang.Comparable;");
            nameList.add( "[I");
            nameList.add( "com.antlersoft.odb.diralloc.DAKey");
            nameList.add( "com.antlersoft.analyzer.DBReference");
            nameList.add( "com.antlersoft.analyzer.DBFieldReference");
            nameList.add( "com.antlersoft.analyzer.DBCall");
            nameList.add( "com.antlersoft.analyzer.DBStringReference");
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
    
    private static class ExactMatchIndexEnumeration implements Enumeration
    {
    	private IndexIterator _ii;
    	private Object _nextObject;
    	
    	ExactMatchIndexEnumeration( IndexIterator ii)
    	{
    		_ii=ii;
    		determineNextObject();
    	}
    	
    	public boolean hasMoreElements()
    	{
    		return _nextObject!=null;
    	}
    	
    	public Object nextElement()
    	throws NoSuchElementException
    	{
    		if ( _nextObject==null)
    			throw new NoSuchElementException();
    		Object result=_nextObject;
    		determineNextObject();
    		return result;
    	}
    	
    	private void determineNextObject()
    	{
    		Object result=null;
			if ( _ii.hasNext() && _ii.isExactMatch())
			{
				result=_ii.next();
			}
			_nextObject=result;
    	}
    }
    
    static void printStatistics( DirectoryAllocator store, String index_name)
    throws Exception
    {
    	IndexStatistics stats=(IndexStatistics)store.getIndexStatistics( index_name);
    	System.out.println( index_name+" Entries per page: "+stats.getEntriesPerPage());
    	System.out.println( "Regular--"+stats.getRegular().toString());
    	System.out.println( "Overflow--"+stats.getOverflow().toString());
    }
    /**
     * Print the statistics for the indexes in this database
     * @param args The first argument should be the database directory
     */
    public static void main( String[] args)
    throws Exception
    {
    	DirectoryAllocator store=new DirectoryAllocator( new File( args[0]),
                new CFactory());
    	printStatistics( store, LOOKUP_INDEX);
    	printStatistics( store, DBType.TYPE_CLASS_INDEX);
    	printStatistics( store, DBMethod.RETURN_TYPE_INDEX);
    	printStatistics( store, DBField.FIELD_TYPE_INDEX);
    	printStatistics( store, DBArgument.ARGUMENT_TYPE_INDEX);
    	printStatistics( store, DBClass.CLASS_NAME_INDEX);
    }
}
