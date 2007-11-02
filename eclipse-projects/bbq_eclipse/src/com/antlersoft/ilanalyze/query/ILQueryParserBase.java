/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import java.util.Enumeration;

import com.antlersoft.ilanalyze.db.*;

import com.antlersoft.parser.ParseState;

import com.antlersoft.query.*;

import com.antlersoft.util.IteratorEnumeration;

/**
 * @author Michael A. MacDonald
 *
 */
class ILQueryParserBase extends BasicBase {

	/**
	 * @author Michael A. MacDonald
	 *
	 */
	static class WriteReference extends CountPreservingBoundFilter {

		/**
		 * @param applies
		 */
		public WriteReference() {
			super(DBFieldReference.class);
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.query.CountPreservingFilter#getCountPreservingFilterValue(com.antlersoft.query.DataSource, java.lang.Object)
		 */
		protected boolean getCountPreservingFilterValue(DataSource source,
				Object inputObject) {
			return ((DBFieldReference)inputObject).isWrite();
		}

	}
	
	static class AllGet extends SetExpression
	{
		private Class _result;
		
		AllGet( Class result)
		{
			_result=result;
		}
		
		public Class getResultClass()
		{
			return _result;
		}
		
		public Enumeration evaluate( DataSource source)
		{
			return new IteratorEnumeration( ((ILDB)source).getAll(_result));
		}
	}

	/**
	 * @param states
	 */
	public ILQueryParserBase(ParseState[] states) {
		super(states);
	}
	
	static CountPreservingValueExpression m_TypeOf=new CountPreservingValueExpression( DBType.class, HasDBType.class) {
		public Object transformSingleObject( DataSource source, Object toTransform)
		{
			return ((HasDBType)toTransform).getDBType();
		}
	};


}
