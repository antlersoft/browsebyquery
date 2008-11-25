
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.antlersoft.bbq.db.DBString;

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.IndexIterator;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectStreamCustomizer;

import com.antlersoft.odb.diralloc.CustomizerFactory;
import com.antlersoft.odb.diralloc.DirectoryAllocator;
import com.antlersoft.odb.diralloc.IndexStatistics;
import com.antlersoft.odb.diralloc.NoSuchClassException;

import com.antlersoft.odb.schemastream.SchemaCustomizer;

import com.antlersoft.query.DataSource;
import com.antlersoft.query.EmptyEnum;

import com.antlersoft.util.IteratorEnumeration;

public class IndexAnalyzeDB implements DataSource
{
    private IndexObjectDB _session;
    private int createCount;
    private static Logger _logger=Logger.getLogger( IndexAnalyzeDB.class.getName());
 
    private static Properties TYPEKEY_INDEX_PROPS;
    private static Properties CLASSNAME_INDEX_PROPS;
    
    static
    {
    	TYPEKEY_INDEX_PROPS=new Properties();
    	TYPEKEY_INDEX_PROPS.put( DirectoryAllocator.ENTRIES_PER_PAGE, "220");
    	CLASSNAME_INDEX_PROPS=new Properties();
    	CLASSNAME_INDEX_PROPS.put( DirectoryAllocator.ENTRIES_PER_PAGE, "150");
    }

	public IndexAnalyzeDB()
	{
		_session=null;
        createCount=0;
	}

    public void openDB(String dbName) throws Exception
    {
        if ( _session!=null)
            closeDB();
        createCount=0;
        _session=new IndexObjectDB( new DirectoryAllocator( new File( dbName),
            new CFactory()));
        _session.redefineIndex( DBArgument.ARGUMENT_TYPE_INDEX, DBArgument.class,
        		new DBArgument.ArgumentTypeKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBCall.CALL_TARGET, DBCall.class,
        		new DBReference.ReferenceTargetGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBClass.CLASS_INTERNAL_NAME_INDEX, DBClass.class,
        		new DBClass.InternalNameKeyGenerator(), false, true, CLASSNAME_INDEX_PROPS);
        _session.redefineIndex( DBClass.CLASS_NAME_INDEX, DBClass.class,
        	new DBClass.NameKeyGenerator(), false, true, CLASSNAME_INDEX_PROPS);
        _session.redefineIndex( DBField.FIELD_TYPE_INDEX, DBField.class,
        		new DBMember.MemberTypeKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBFieldReference.FRTARGET, DBFieldReference.class,
        		new DBReference.ReferenceTargetGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBMethod.RETURN_TYPE_INDEX, DBMethod.class,
        		new DBMember.MemberTypeKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBPackage.PACKAGE_NAME_INDEX, DBPackage.class,
        		new DBPackage.PackageNameKeyGenerator(), false, true, CLASSNAME_INDEX_PROPS);
        _session.redefineIndex( DBString.STRING_INDEX, DBString.class,
        		new DBString.StringKeyGenerator(), false, true, CLASSNAME_INDEX_PROPS);
        _session.redefineIndex( DBString.SRTARGET, DBStringReference.class,
        		new DBReference.ReferenceTargetGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBType.TYPE_CLASS_INDEX, DBType.class,
        		new DBType.TypeClassKeyGenerator(), false, false, TYPEKEY_INDEX_PROPS);
        _session.redefineIndex( DBType.TYPE_KEY_INDEX, DBType.class,
        		new DBType.TypeKeyGenerator(), false, true, CLASSNAME_INDEX_PROPS);
    }
    
    /**
     * Call when you've added a class; commits when a certain number of classes have been
     * added
     * @throws Exception
     */
    void incrementCreateCount()
    throws Exception
    {
    	if ( (++createCount % 1000) == 0)
    		commit();
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
            {
            	if ( children[i].isFile() && children[i].getName().indexOf('.')== -1)
            		children[i].delete();            	
            }
        }
        openDB( dbName);
    }
    
    public synchronized void commit()
    throws Exception
    {
    	if ( _session != null)
    		_session.commitAndRetain();
    }

    public Object findWithIndex( String index_name, Comparable key)
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
    
    public synchronized void makeCurrent()
    {
        _session.makeCurrent();
    }
    
    public void deleteObject( Object o)
    {
    	_session.deleteObject(o);
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
	
	public IndexObjectDB getSession() { return _session; }
	
    static class CFactory extends CustomizerFactory
    {
        public ObjectStreamCustomizer getCustomizer( Class toStore)
        {
            ArrayList<String> nameList=new ArrayList<String>();
			nameList.add( "java.lang.Comparable");
			nameList.add( "[Ljava.lang.Comparable;");
			nameList.add( "[I");
			nameList.add( "java.util.Collection");
			nameList.add( "java.util.Map$Entry");
			nameList.add( "java.util.TreeMap");
			nameList.add( "com.antlersoft.odb.diralloc.DAKey");
			nameList.add( "com.antlersoft.odb.diralloc.UniqueKey");
			nameList.add( "com.antlersoft.odb.ObjectKeyHashMap");
			nameList.add( "com.antlersoft.odb.ObjectKeyHashSet");
			nameList.add( "com.antlersoft.odb.ObjectRef");
			nameList.add( "com.antlersoft.analyzer.DBMember");
            nameList.add( "com.antlersoft.analyzer.DBReference");
            nameList.add( toStore.getName());
            return new SchemaCustomizer( nameList);
        }
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
    	store.logAllStatistics();
    }
}
