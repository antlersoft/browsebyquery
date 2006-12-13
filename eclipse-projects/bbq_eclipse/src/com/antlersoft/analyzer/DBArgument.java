package com.antlersoft.analyzer;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * Information about a method parameter.  This is only collected if the
 * captureOptional value for the AnalyzerDB has the OPTIONAL_TYPE_INFO flag set.
 * 
 * Not addressable independently by a lookup object, but a distinct object independently
 * addressable in the database; there is a non-unique index on the type field.
 * 
 * Once created, method, type and ordinal are invariant.  The name might change.
 * @author Michael MacDonald
 *
 */
public class DBArgument implements Persistent, SourceObject, HasDBType {
	
	ObjectRef _dbtype;
	ObjectRef _dbmethod;
	/**
	 * 0-based position of the argument in the method.
	 * For non-static, non-constructor methods, position 0
	 * will be the object reference.
	 */
	int _ordinal;
	String _name;
    static final String ARGUMENT_TYPE_INDEX="ArgumentType";

	private transient PersistentImpl _persistentImpl;

	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		
		return _persistentImpl;
	}
	
	public DBMethod getMethod()
	{
		return (DBMethod)_dbmethod.getReferenced();
	}
	
	public DBType getDBType( AnalyzerDB db)
	{
		return (DBType)_dbtype.getReferenced();
	}
	
	public int getOrdinal()
	{
		return _ordinal;
	}
	
	public DBClass getDBClass()
	{
		return getMethod().getDBClass();
	}

	public int getLineNumber()
	{
		return getMethod().getLineNumber();
	}
	
	DBArgument( DBMethod method, int ordinal, DBType type)
	{
		_dbmethod=new ObjectRef( method);
		_ordinal=ordinal;
		_dbtype=new ObjectRef( type);
		_name="";
		ObjectDB.makePersistent( this);
	}
	
	void setName( String name)
	{
		_name=name;
		ObjectDB.makeDirty( this);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String toString()
	{
		return getMethod().toString()+" #"+_ordinal+"; "+_name+" ("+getDBType(null).toString()+")";
	}
	
	static class ArgumentTypeKeyGenerator implements KeyGenerator
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7540201031745735736L;

		public Comparable generateKey( Object obj)
		{
			DBArgument arg=(DBArgument)obj;
			return new ObjectRefKey( arg._dbtype);
		}
	}
}
