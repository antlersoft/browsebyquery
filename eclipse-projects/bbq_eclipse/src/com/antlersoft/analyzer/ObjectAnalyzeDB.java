package com.antlersoft.analyzer;

import java.io.File;
import java.util.Vector;
import java.util.Enumeration;

import com.antlersoft.odb.DiskAllocatorStore;
import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentHashtable;
import com.antlersoft.odb.PersistentImpl;

class ObjectAnalyzeDB implements AnalyzerDB
{
    ObjectDB _session;
    PersistentHashtable _classHash;
	private int createCount;

    ObjectAnalyzeDB()
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
		ObjectDB tempSession=new ObjectDB( new DiskAllocatorStore( new File( dbName)));
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
		_session.commit();
		_session=null;
		_classHash=null;
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
				_session.commit();
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
			ObjectDB.setPersistent( this);
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
