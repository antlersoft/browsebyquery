/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.antlersoft.ilanalyze.ReadArg;
import com.antlersoft.ilanalyze.ReadType;
import com.antlersoft.ilanalyze.Signature;

import com.antlersoft.odb.FromRefEnumeration;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;

import com.antlersoft.util.IteratorEnumeration;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBMethod extends DBMember {
	
	static final String METHOD_TYPE_INDEX="METHOD_TYPE_INDEX";
	
	/** DBArguments for this method */
	private ArrayList m_arguments;
	
	/** DBCalls from this method */
	private ArrayList m_calls;
	/** DBFieldReferences from this method */
	private ArrayList m_field_references;
	/** DBStringReferences from this method */
	private ArrayList m_string_references;
	/** True if the method has been read from an assembly, rather than just being referenced */
	private boolean m_visited;

	/**
	 * @param container
	 * @param name
	 * @param type
	 * @param sig_string String representing argument signature
	 */
	DBMethod(DBClass container, String name, DBType type) {
		super(container, name, type);
		m_arguments=new ArrayList();
		m_calls=new ArrayList();
		m_field_references=new ArrayList();
		m_string_references=new ArrayList();
		ObjectDB.makePersistent( this);
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
	 * @return Enumeration over calls <i>from</i> this method
	 */
	public Enumeration getCalls()
	{
		return new FromRefEnumeration( new IteratorEnumeration( m_calls.iterator()));
	}
	
	/**
	 * 
	 * @return true if the method has actually been analyzed, rather than just being referenced
	 */
	public boolean isVisited()
	{
		return m_visited;
	}
	
	void setVisited( boolean visited)
	{
		if ( visited!=m_visited)
		{
			m_visited=visited;
			ObjectDB.makeDirty(this);
		}
	}
	
	static class MethodUpdater
	{
		private ILDB m_db;
		DBMethod m_method;
		boolean m_updated;
		ReferenceUpdater m_call_up;
		ReferenceUpdater m_string_up;
		ReferenceUpdater m_field_up;
		
		MethodUpdater( ILDB db, DBMethod method)
		{
			m_db=db;
			m_method=method;
			method.taintFileAndLine();
			method.setVisited(true);
			m_updated=false;
			m_call_up=new ReferenceUpdater( method.m_calls);
			m_string_up=new ReferenceUpdater( method.m_string_references);
			m_field_up=new ReferenceUpdater( method.m_field_references);
		}
		
		void updateArguments( Signature sig) throws ITypeInterpreter.TIException
		{
			synchronized ( m_method)
			{
				List arg_list=sig.getArguments();
				int size=arg_list.size();
				for ( int i=0; i<size; ++i)
				{
					ReadArg arg=(ReadArg)arg_list.get(i);
					if ( i>=m_method.m_arguments.size())
					{
						m_method.m_arguments.add( new ObjectRef( new DBArgument( getArgType( arg), arg.getName(), i)));
						m_updated=true;
					}
					else
					{
						if ( arg.getName()!=null)
						{
							DBArgument dbarg=(DBArgument)((ObjectRef)m_method.m_arguments.get(i)).getReferenced();
							dbarg.setName( arg.getName());
							dbarg.setDBType( getArgType(arg));
						}
					}
				}
				if ( m_method.m_arguments.size()>size)
				{
					while ( size<m_method.m_arguments.size())
					{
						m_db.deleteObject( ((ObjectRef)m_method.m_arguments.get(size)).getReferenced());
						m_method.m_arguments.remove(size);
					}
					m_method.m_arguments.trimToSize();
					m_updated=true;
				}
			}
		}
		
		void addCall( DBMethod called, DBSourceFile file, int line)
		{
			if ( ! m_call_up.existsReference( called, file, line))
			{
				m_call_up.newReference(new DBCall(m_method, called), file, line);
			}
		}
		

		void addStringReference( DBString str, DBSourceFile file, int line)
		{
			if ( ! m_string_up.existsReference( str, file, line))
			{
				m_string_up.newReference(new DBStringReference(m_method, str), file, line);
			}
		}
		
		void addFieldReference( DBField field, boolean isWrite, DBSourceFile file, int line)
		{
			if ( ! m_field_up.existsReference(field, file, line))
			{
				m_field_up.newReference(new DBFieldReference( m_method, field, isWrite), file, line);
			}
			else
			{
				((DBFieldReference)((ObjectRef)m_field_up.m_after_list.get(m_field_up.m_after_list.size()-1)).getReferenced()).setWrite( isWrite);
			}
		}
		
		void methodDone()
		{
			if ( m_string_up.cleanup(m_db))
			{
				m_updated=true;
				m_method.m_string_references=m_string_up.m_after_list;
			}
			if ( m_field_up.cleanup(m_db))
			{
				m_updated=true;
				m_method.m_field_references=m_field_up.m_after_list;
			}
			if ( m_call_up.cleanup(m_db))
			{
				m_updated=true;
				m_method.m_calls=m_call_up.m_after_list;
			}
			if ( m_updated)
			{
				ObjectDB.makeDirty(m_method);
			}
		}
		
		private DBType getArgType( ReadArg arg) throws ITypeInterpreter.TIException
		{
			ReadType type=arg.getType();
			if ( type==null)
				return null;
			return DBType.get( m_db, type);
		}
		
		static class ReferenceUpdater
		{
			private ArrayList m_before_list;
			ArrayList m_after_list;
			private boolean m_updated;
			
			ReferenceUpdater(  ArrayList orig)
			{
				m_before_list=(ArrayList)orig.clone();
				m_after_list=new ArrayList( orig.size());
				m_updated=false;
				
				for ( Iterator i=orig.iterator(); i.hasNext();)
				{
					m_before_list.add( i.next());
				}
			}
			
			boolean existsReference( Object target, DBSourceFile file, int line)
			{
				for ( Iterator i=m_before_list.iterator(); i.hasNext();)
				{
					ObjectRef o=(ObjectRef)i.next();
					DBReference dbr=(DBReference)o.getReferenced();
					if ( o.getReferenced().equals( target))
					{
						dbr.setFileAndLine(file, line);
						i.remove();
						m_after_list.add( o);
						return true;
					}
					m_updated=true;
				}
				return false;
			}
			
			void newReference( DBReference r, DBSourceFile file, int line)
			{
				r.setFileAndLine(file, line);
				m_after_list.add( new ObjectRef(r));
				m_updated=true;
			}
			
			boolean cleanup(ILDB db)
			{
				for ( Iterator i=m_before_list.iterator(); i.hasNext();)
				{
					m_updated=true;
					db.deleteObject(((ObjectRef)i.next()).getReferenced());
				}
				return m_updated;
			}
		}
	}
}
