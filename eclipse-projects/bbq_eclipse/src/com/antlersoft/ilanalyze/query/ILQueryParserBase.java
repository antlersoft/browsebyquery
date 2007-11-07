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

    static class ClassContaining extends CountPreservingValueExpression
    {
        ClassContaining()
        {
            super( DBClass.class, null);
        }

        public Object transformSingleObject( DataSource source, Object toTransform)
        {
            if ( appliesClass()==DBMethod.class)
                return ((DBMethod)toTransform).getDBClass();
            if ( appliesClass()==DBField.class)
                return ((DBField)toTransform).getDBClass();
            if ( appliesClass()==DBClass.class)
            	return ((DBClass)toTransform).getContainingClass();
            throw new RuntimeException( "Class of incorrectly bound");
        }

        public void lateBindApplies( Class c)
            throws BindException
        {
            if ( c!=DBMethod.class && c!=DBField.class && c!=DBClass.class)
            {
                throw new BindException( "Operator only applies to methods, fields and classes");
            }
            super.lateBindApplies( c);
        }
    }

    static class UncalledMethod extends CountPreservingBoundFilter
    {
        UncalledMethod()
        {
        	super( DBMethod.class);
        }

        protected boolean getCountPreservingFilterValue( DataSource source, Object toCheck)
        {
            return ! ((DBMethod)toCheck).getCallsTo((ILDB)source).hasMoreElements();
        }
    }

    static class UncalledPolymorphic extends UncalledMethod
    {
        protected boolean getCountPreservingFilterValue( DataSource source, Object toInclude)
        {
        	try
        	{
	            DBMethod method=(DBMethod)toInclude;
	            boolean result=true;
	            for ( Enumeration e=(new TransformSet( new Polymorphic(), new SingleMethod( method))).evaluate( source);
	                e.hasMoreElements() && result;)
	            {
	                if ( ((DBMethod)e.nextElement()).
	                    getCallsTo((ILDB)source).hasMoreElements())
	                    result=false;
	            }
                return result;
        	}
        	catch (BindException be)
        	{
        		throw new RuntimeException( be);
        	}
        }
    }

    static class SingleMethod extends SetExpression
    {
        DBMethod _method;

        SingleMethod( DBMethod c)
        {
            _method=c;
        }
        
        public Class getResultClass()
        {
        	return _method.getClass();
        }

        public Enumeration evaluate( DataSource db)
        {
            return new SingleEnum( _method);
        }
    }
    
    /**
	 * @param states
	 */
	public ILQueryParserBase(ParseState[] states) {
		super(states);
	}
}
