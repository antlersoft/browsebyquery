package analyzer;

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

class SimpleAnalyzerDB implements AnalyzerDB
{
    private Hashtable _typeHash; // String, Hashtable
    private String _dbName;

    SimpleAnalyzerDB()
    {
	_typeHash=null;
    }

    // Implement AnalyzeDB
    public void openDB( String dbName)
	throws Exception
    {
	_dbName=dbName;
	FileInputStream fis;
	try
	{
	    fis=new FileInputStream( dbName);
	}
	catch ( FileNotFoundException fnfe)
	{
	    ObjectOutputStream oos=new ObjectOutputStream( new FileOutputStream( dbName));
	    oos.writeObject( new Hashtable());
	    oos.close();
	    fis=new FileInputStream( dbName);
	}
	_typeHash=(Hashtable)(new ObjectInputStream( new BufferedInputStream(fis))).readObject();
    }

    public void closeDB()
        throws Exception
    {
	verifyState();
	ObjectOutputStream oos=new ObjectOutputStream( new BufferedOutputStream( new FileOutputStream( _dbName)));
	oos.writeObject( _typeHash);
	oos.close();
    }

    public Object getWithKey( String type, String key)
	throws Exception
    {
	verifyState();
	Hashtable objectHash=getObjectHash( type);
	Object retVal=objectHash.get( key);
	if ( retVal==null)
	{
	    retVal=Class.forName( type).getConstructor( new Class[] { String.class, AnalyzerDB.class }).newInstance( new Object[] { key, this });
	    objectHash.put( key, retVal);
	}
	return retVal;
    }

    public Object findWithKey( String type, String key)
	throws Exception
    {
	verifyState();
	Hashtable objectHash=getObjectHash( type);
	return objectHash.get( key);
    }

    public Enumeration getAll( String type)
	throws Exception
    {
	verifyState();
	return getObjectHash( type).elements();
    }

    private Hashtable getObjectHash( String type)
    {
	 Hashtable objectHash=(Hashtable)_typeHash.get( type);
	 if ( objectHash==null)
	 {
	     objectHash=new Hashtable();
	     _typeHash.put( type, objectHash);
	 }
	 return objectHash;
    }

    private void verifyState()
	throws IllegalStateException
    {
	if ( _typeHash==null)
	    throw new IllegalStateException();
    }
}
