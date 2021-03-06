/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.antlersoft.bbq.db.DBString;
import com.antlersoft.ilanalyze.ReadArg;
import com.antlersoft.ilanalyze.ReadType;
import com.antlersoft.ilanalyze.Signature;

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.FromRefIteratorEnumeration;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;

import com.antlersoft.util.IteratorEnumeration;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBMethod extends DBMember {
	
	/**
	 * Generated from the version before m_casts was added to the class.
	 * 
	 *
	 */
	private static final long serialVersionUID = -2499920887934480894L;

	static final String METHOD_TYPE_INDEX="METHOD_TYPE_INDEX";
	
	/** DBArguments for this method */
	private ArrayList m_arguments;
	
	/** DBCalls from this method */
	private ArrayList m_calls;
	/** DBFieldReferences from this method */
	private ArrayList m_field_references;
	/** DBStringReferences from this method */
	private ArrayList m_string_references;
	/** DBCatches in the method */
	private ArrayList<ObjectRef<DBCatch>> m_catches;
	/** True if the method has been read from an assembly, rather than just being referenced */
	private boolean m_visited;
	/** Signature key (signature as a single string) */
	private String m_signature_key;
	private ArrayList<ObjectRef<DBCast>> m_casts;

	/**
	 * @param container
	 * @param name
	 * @param type
	 * @param signature_key String representing argument signature
	 */
	DBMethod(ObjectDB db, DBClass container, String name, DBType type, String signature_key) {
		super(container, name, type);
		m_arguments=new ArrayList();
		m_calls=new ArrayList();
		m_field_references=new ArrayList();
		m_string_references=new ArrayList();
		m_catches=new ArrayList<ObjectRef<DBCatch>>();
		m_signature_key=signature_key;
		db.makePersistent( this);
	}

	/**
	 * 
	 * @return Enumeration over DBArgument objects for this method
	 */
	public Enumeration getArguments()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_arguments.iterator()));
	}
	
	/**
	 * 
	 * @return Enumeration over DBFieldReference of references from this method
	 */
	public Enumeration getReferences()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_field_references.iterator()));
	}
	
	
	/**
	 * 
	 * @return Enumeration over DBStringReference representing string references from this method
	 */
	public Enumeration getStringReferences()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_string_references.iterator()));		
	}
	
	/**
	 * 
	 * @return Enumeration over calls <i>from</i> this method
	 */
	public Enumeration getCalls()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_calls.iterator()));
	}
	
	public Enumeration<DBCatch> getCatches()
	{
		return new FromRefIteratorEnumeration<DBCatch>( m_catches.iterator());
	}
	
	public Enumeration<DBCast> getCasts()
	{
		ArrayList<ObjectRef<DBCast>> c = m_casts;
		if (c == null)
		{
			c = new ArrayList<ObjectRef<DBCast>>();
		}
		return new FromRefIteratorEnumeration<DBCast>(c.iterator());
	}
	
	
	/**
	 * Return an enumeration over calls to this method
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over calls to this method
	 */
	public Enumeration getCallsTo( ILDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBCall.CALL_TARGET, new ObjectRefKey(this)));
	}
	
	/**
	 * 
	 * @return Arguments with type as a single string
	 */
	public String getSignatureKey()
	{
		return m_signature_key;
	}
	
	/**
	 * 
	 * @return true if the method has actually been analyzed, rather than just being referenced
	 */
	public boolean isVisited()
	{
		return m_visited;
	}
	
	public void addMethodInfo( StringBuilder sb)
	{
		sb.append( getDBClass().toString());
		sb.append( "::");
		sb.append( getName());
		sb.append('(');
		for ( Enumeration e=getArguments();e.hasMoreElements();)
		{
			((DBArgument)e.nextElement()).addArgInfo(sb);
			if ( e.hasMoreElements())
				sb.append(',');
		}
		sb.append(')');
		sb.append(' ');
		sb.append(getDBType().toString());		
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		addMethodInfo( sb);
		addPositionString(sb);
		return sb.toString();
	}
	
	void setVisited( ObjectDB db, boolean visited)
	{
		if ( visited!=m_visited)
		{
			m_visited=visited;
			db.makeDirty(this);
		}
	}
	
	synchronized boolean updateArguments( ILDB db, Signature sig) throws ITypeInterpreter.TIException
	{
		boolean updated=false;
		List arg_list=sig.getArguments();
		int size=arg_list.size();
		for ( int i=0; i<size; ++i)
		{
			ReadArg arg=(ReadArg)arg_list.get(i);
			if ( i>=m_arguments.size())
			{
				m_arguments.add( new ObjectRef( new DBArgument( db,this, getArgType( db, arg), arg.getName(), i)));
				updated=true;
			}
			else
			{
				if ( arg.getName()!=null)
				{
					DBArgument dbarg=(DBArgument)((ObjectRef)m_arguments.get(i)).getReferenced();
					dbarg.setName( db, arg.getName());
					dbarg.setDBType( db, getArgType(db, arg));
				}
			}
		}
		if ( m_arguments.size()>size)
		{
			while ( size<m_arguments.size())
			{
				db.deleteObject( ((ObjectRef)m_arguments.get(size)).getReferenced());
				m_arguments.remove(size);
			}
			m_arguments.trimToSize();
			updated=true;
		}
		
		return updated;
	}
	
	static DBType getArgType( ILDB db, ReadArg arg) throws ITypeInterpreter.TIException
	{
		ReadType type=arg.getType();
		if ( type==null)
			return null;
		return DBType.get( db, type);
	}
	
	static class MethodUpdater
	{
		private ILDB m_db;
		DBMethod m_method;
		boolean m_updated;
		ReferenceUpdater<DBCall> m_call_up;
		ReferenceUpdater<DBStringReference> m_string_up;
		ReferenceUpdater<DBFieldReference> m_field_up;
		ReferenceUpdater<DBCatch> m_catch_up;
		ReferenceUpdater<DBCast> m_cast_up;
		
		MethodUpdater( ILDB db, DBMethod method)
		{
			m_db=db;
			m_method=method;
			method.taintFileAndLine();
			method.setVisited( db,true);
			m_updated=false;
			m_call_up=new ReferenceUpdater<DBCall>(db, method.m_calls);
			m_string_up=new ReferenceUpdater<DBStringReference>( db, method.m_string_references);
			m_field_up=new ReferenceUpdater<DBFieldReference>( db, method.m_field_references);
			m_catch_up=new ReferenceUpdater<DBCatch>( db, method.m_catches);
			if (method.m_casts == null)
			{
				method.m_casts = new ArrayList<ObjectRef<DBCast>>();
			}
			m_cast_up=new ReferenceUpdater<DBCast>( db, method.m_casts);
		}
		
		void updateArguments( Signature sig) throws ITypeInterpreter.TIException
		{
			if ( m_method.updateArguments(m_db, sig))
				m_updated=true;
		}
		
		void addCall( DBMethod called, DBSourceFile file, int line)
		{
			if ( ! m_call_up.existsReference( called, file, line))
			{
				m_call_up.newReference(new DBCall(m_db, m_method, called), file, line);
			}
		}
		

		void addStringReference( DBString str, DBSourceFile file, int line)
		{
			if ( ! m_string_up.existsReference( str, file, line))
			{
				m_string_up.newReference(new DBStringReference(m_db, m_method, str), file, line);
			}
		}
		
		void addCatch( DBClass c, DBSourceFile file, int line)
		{
			if ( ! m_catch_up.existsReference( c, file, line))
			{
				m_catch_up.newReference(new DBCatch( m_db, m_method, c), file, line);
			}
		}
		
		void addCast( DBClass c, boolean isOptional, DBSourceFile file, int line)
		{
			boolean found = false;
			for ( Iterator<ObjectRef<DBCast>> i=m_cast_up.m_before_list.iterator(); i.hasNext();)
			{
				ObjectRef<DBCast> o=i.next();
				DBCast dbr=o.getReferenced();
				if (dbr.getTarget().equals(c))
				{
					dbr.setFileAndLine(m_db, file, line);
					dbr.setOptional(isOptional);
					i.remove();
					m_cast_up.m_after_list.add( o);
					found = true;
					break;
				}
			}
			if (! found)
			{
				m_cast_up.newReference(new DBCast(m_db, m_method, c, isOptional), file, line);
			}
		}
		
		void addFieldReference( DBField field, boolean isWrite, DBSourceFile file, int line)
		{
			if ( ! m_field_up.existsReference(field, file, line))
			{
				m_field_up.newReference(new DBFieldReference( m_db, m_method, field, isWrite), file, line);
			}
			else
			{
				((DBFieldReference)((ObjectRef)m_field_up.m_after_list.get(m_field_up.m_after_list.size()-1)).getReferenced()).setWrite( m_db, isWrite);
			}
		}
		
		void methodDone()
		{
			if ( m_string_up.cleanup())
			{
				m_updated=true;
				m_method.m_string_references=m_string_up.m_after_list;
			}
			if ( m_field_up.cleanup())
			{
				m_updated=true;
				m_method.m_field_references=m_field_up.m_after_list;
			}
			if ( m_call_up.cleanup())
			{
				m_updated=true;
				m_method.m_calls=m_call_up.m_after_list;
			}
			if ( m_catch_up.cleanup())
			{
				m_updated=true;
				m_method.m_catches=m_catch_up.m_after_list;
			}
			if ( m_cast_up.cleanup())
			{
				m_updated=true;
				m_method.m_casts=m_cast_up.m_after_list;
			}
			if ( m_updated)
			{
				m_db.makeDirty(m_method);
			}
		}
		
		static class ReferenceUpdater<T extends DBReference>
		{
			private ArrayList<ObjectRef<T>> m_before_list;
			ArrayList<ObjectRef<T>> m_after_list;
			private boolean m_updated;
			private ObjectDB db;
			
			ReferenceUpdater( ObjectDB database, ArrayList<ObjectRef<T>> orig)
			{
				db=database;
				m_before_list=(ArrayList<ObjectRef<T>>)orig.clone();
				m_after_list=new ArrayList<ObjectRef<T>>( orig.size());
				m_updated=false;
			}
			
			boolean existsReference(Object target, DBSourceFile file, int line)
			{
				for ( Iterator<ObjectRef<T>> i=m_before_list.iterator(); i.hasNext();)
				{
					ObjectRef<T> o=i.next();
					T dbr=o.getReferenced();
					if ( o.getReferenced().equals( target))
					{
						dbr.setFileAndLine(db, file, line);
						i.remove();
						m_after_list.add( o);
						return true;
					}
					m_updated=true;
				}
				return false;
			}
			
			void newReference(T r, DBSourceFile file, int line)
			{
				r.setFileAndLine(db, file, line);
				m_after_list.add( new ObjectRef<T>(r));
				m_updated=true;
			}
			
			boolean cleanup()
			{
				for ( Iterator<ObjectRef<T>> i=m_before_list.iterator(); i.hasNext();)
				{
					m_updated=true;
					db.deleteObject(i.next().getReferenced());
				}
				return m_updated;
			}
		}
	}
}
