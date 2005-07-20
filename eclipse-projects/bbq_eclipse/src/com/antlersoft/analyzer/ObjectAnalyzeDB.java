/*
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
 */
package com.antlersoft.analyzer;

import java.io.File;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Arrays;

import com.antlersoft.odb.schemastream.SchemaAllocatorStore;
import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentHashtable;
import com.antlersoft.odb.PersistentImpl;

public class ObjectAnalyzeDB implements AnalyzerDB
{
    ObjectDB _session;
    PersistentHashtable _classHash;
	private int createCount;

    private static String[] schemaClasses={
        "com.antlersoft.analyzer.DBClass",
        "com.antlersoft.analyzer.DBField",
        "com.antlersoft.analyzer.DBMethod",
        "com.antlersoft.analyzer.DBReference",
        "com.antlersoft.analyzer.DBCall",
        "com.antlersoft.analyzer.DBFieldReference"
        };

    public ObjectAnalyzeDB()
    {
		_session=null;
		_classHash=null;
		createCount=0;
    }

    // Implement AnalyzeDB
    public synchronized void openDB( String dbName)
	throws Exception
    {
		if ( _session!=null)
            closeDB();
		ObjectDB tempSession=new ObjectDB( SchemaAllocatorStore.getSchemaStore(
            new File( dbName), Arrays.asList( schemaClasses)));
        _classHash=(PersistentHashtable)tempSession.getRootObject( "_classHash");
		if ( _classHash==null)
		{
			_classHash=new PersistentHashtable();
			tempSession.makeRootObject( "_classHash", _classHash);
		}
		_session=tempSession;
    }

    public synchronized void closeDB()
        throws Exception
    {
		verifyState();
        _session.close();
		_session=null;
		_classHash=null;
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
        f.delete();
        openDB( dbName);
    }

    public synchronized Object getWithKey( String type, String key)
	throws Exception
    {
		verifyState();
		Object retVal=findWithKey( type, key);
		if ( retVal==null)
		{
			retVal=Class.forName( type).getConstructor( new Class[] { String.class, AnalyzerDB.class }).newInstance( new Object[] { key, this });
			_session.makeRootObject( type+"|"+key, retVal);
			getObjectVector( type).add( retVal);
			createCount++;
			if ( createCount==1000)
			{
				_session.commitAndRetain();
				createCount=0;
			}
		}
		return retVal;
    }

    public synchronized Object findWithKey( String type, String key)
	throws Exception
    {
		verifyState();
		return _session.getRootObject( type+"|"+key);
    }

    public synchronized Enumeration getAll( String type)
	throws Exception
    {
		verifyState();
		return getObjectVector( type).elements();
    }

    public synchronized void makeCurrent()
    {
        _session.makeCurrent();
    }

    private ObjectVector getObjectVector( String type)
    {
		ObjectVector objects=(ObjectVector)_classHash.get( type);
		if ( objects==null)
		{
			objects=new ObjectVector();
			_classHash.put( type, objects);
		}
		return objects;
    }

    private void verifyState()
    {
		if ( _session==null)
			throw new IllegalStateException( "Database not initialized");
    }

	static private class ObjectVector implements Persistent
	{
		private transient PersistentImpl _persistentImpl;

		Vector v;

		public PersistentImpl _getPersistentImpl()
		{
			if ( _persistentImpl==null)
				_persistentImpl=new PersistentImpl();
			return _persistentImpl;
		}

		ObjectVector()
		{
			v=new Vector();
			_persistentImpl=new PersistentImpl();
			ObjectDB.makePersistent( this);
		}

		void add( Object o)
		{
			v.addElement( new ObjectRef( o));
			ObjectDB.makeDirty( this);
		}

		Enumeration elements()
		{
			return new FromRefEnumeration( v.elements());
		}
	}
}
