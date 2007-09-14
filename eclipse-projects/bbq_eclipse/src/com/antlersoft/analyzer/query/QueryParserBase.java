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
package com.antlersoft.analyzer.query;

import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.TreeSet;

import com.antlersoft.analyzer.*;

import com.antlersoft.parser.ParseState;

import com.antlersoft.query.*;

class QueryParserBase extends BasicBase
{
    public QueryParserBase( ParseState[] parseStates)
    {
        super( parseStates);
    }

    static class PackageGet extends SetExpression
    {
    	private String _packageName;
    	
    	PackageGet( String packageName)
    	{
    		_packageName=packageName;
    	}
    	
    	public Class getResultClass()
    	{
    		return DBPackage.class;
    	}
    	
    	public Enumeration evaluate( DataSource source)
    	{
    		Vector tmp=new Vector(1);
    		try
    		{
	    		Object pack=((IndexAnalyzeDB)source).findWithKey( "com.antlersoft.analyzer.DBPackage", _packageName);
	    		if ( pack!=null)
	    			tmp.add( pack);
    		}
    		catch (Exception e)
    		{
    		
    		}
    		return tmp.elements();
    	}
    }
    
    static class ClassGet extends SetExpression
    {
        private String _className;
        private TreeSet _set;

        ClassGet( String className, TreeSet set)
        {
            _className=className;
            _set=set;
        }
        
        public Class getResultClass()
        {
        	return DBClass.class;
        }

        public Enumeration evaluate( DataSource source)
        {
            Vector tmp=new Vector( 1);
            IndexAnalyzeDB db=(IndexAnalyzeDB)source;
            try
            {
	            Object q=db.findWithIndex( DBClass.CLASS_NAME_INDEX, _className);
	            if ( q!=null)
	                tmp.addElement( q);
	            for ( Iterator i=_set.iterator(); i.hasNext() && q==null;)
	            {
	                q=db.findWithIndex( DBClass.CLASS_NAME_INDEX, ((String)i.next())+"."+_className);
	                if ( q!=null)
	                    tmp.addElement( q);
	            }
	            return tmp.elements();
            }
            catch ( Exception e)
            {
            	throw new RuntimeException( e);
            }
        }
    }

    static class ClassesGet extends SetExpression
    {
    	public Class getResultClass()
    	{
    		return DBClass.class;
    	}
    	
        public Enumeration evaluate( DataSource db)
        {
        	try
        	{
        		return ((AnalyzerDB)db).getAll( "com.antlersoft.analyzer.DBClass");
        	}
        	catch ( Exception e)
        	{
        		throw new RuntimeException( e);
        	}
        }
    }

    static class PackagesGet extends SetExpression
    {
    	public Class getResultClass()
    	{
    		return DBPackage.class;
    	}
    	
        public Enumeration evaluate( DataSource db)
        {
        	try
        	{
        		return ((AnalyzerDB)db).getAll( "com.antlersoft.analyzer.DBPackage");
        	}
        	catch ( Exception e)
        	{
        		throw new RuntimeException( e);
        	}
        }
    }

static abstract class ClassTransform extends UniqueTransformImpl
    {
        ClassTransform( )
        {
            super( DBClass.class, DBClass.class);
        }
    }

    static class BaseClasses extends ClassTransform
    {
        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            DBClass c=(DBClass)toTransform;
            return c.getSuperClasses();
        }
    }

    static class DerivedClasses extends ClassTransform
    {
        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            DBClass c=(DBClass)toTransform;
            return c.getDerivedClasses();
        }
    }

    static class RecursiveBaseClasses extends ClassTransform
    {
        private void addSuperClasses( DBClass current)
        {
            Enumeration superEnum=current.getSuperClasses();
            while ( superEnum.hasMoreElements())
            {
                DBClass superClass=(DBClass)superEnum.nextElement();
                if ( ! m_set.contains( superClass))
                {
                    m_set.add( superClass);
                    addSuperClasses( superClass);
                }
            }
        }

        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            addSuperClasses( (DBClass)toTransform);
            return EmptyEnum.empty;
        }
    }

    static class RecursiveDerivedClasses extends ClassTransform
    {
        private void addDerivedClasses( DBClass current)
        {
            Enumeration derivedEnum=current.getDerivedClasses();
            while ( derivedEnum.hasMoreElements())
            {
                DBClass derivedClass=(DBClass)derivedEnum.nextElement();
                if ( ! m_set.contains( derivedClass))
                {
                    m_set.add( derivedClass);
                    addDerivedClasses( derivedClass);
                }
            }
        }

        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            addDerivedClasses( (DBClass)toTransform);
            return EmptyEnum.empty;
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

/*
    static class ClassMatching implements ClassSet
    {
        ClassSet _toFilter;
        String _filterString;

        ClassMatching( ClassSet toFilter, String filterString)
        {
            _toFilter=toFilter;
            _filterString=filterString;
        }

        public Enumeration execute( AnalyzerDB db)
            throws Exception
        {
            return new FilterEnumeration( _toFilter.execute( db)) {
                protected Object filterObject( Object next)
                {
                    Object retVal=null;
                    if ( ((DBClass)next).getName().indexOf(
                        _filterString)!= -1)
                        retVal=next;
                    return retVal;
                }
            };
        }
    }
*/

    static class SingleClass extends SetExpression
    {
        DBClass _class;

        SingleClass( DBClass c)
        {
            _class=c;
        }
        
        public Class getResultClass()
        {
        	return _class.getClass();
        }

        public Enumeration evaluate( DataSource source)
        {
            return new SingleEnum( _class);
        }
    }

    /*
      MethodSet : methods in ClassSet
                | polymorphic MethodSet
                | matching "x" MethodSet
                | uncalled MethodSet
                | uncalled polymorphic MethodSet
     */
    static class MethodsIn extends TransformImpl
    {
        MethodsIn()
        {
            super( DBMethod.class, DBClass.class);
        }

        public Enumeration transformObject( DataSource source, Object toTransform)
        {
            return ((DBClass)toTransform).getMethods();
        }
    }

    /**
     * Implements an enumeration that changes the objects produced by a base enumeration
     * into another object with the filterObject abstract method.
     * @author Michael A. MacDonald
     *
     */
    static public abstract class FilterEnumeration implements Enumeration
    {
        private Enumeration _baseEnumeration;
        private Object next;
        // Kludge because of compiler bug
        private boolean initialized;

        public FilterEnumeration( Enumeration base)
        {
            _baseEnumeration=base;
            // Kludge because of compiler bug
            initialized=false;
        }

        /**
         * Changes the output of the underlying enumeration.
         * Must produce an object or null for each object passed in.
         * @param next Object from base enumeration
         * @return transformed object
         */
        abstract protected Object filterObject( Object next);

        private void getNextObject()
        {
            for ( next=null; next==null &&
                _baseEnumeration.hasMoreElements();)
            {
                next=filterObject( _baseEnumeration.nextElement());
            }
        }

        public boolean hasMoreElements()
        {
            // Kludge because of compiler bug
            if ( ! initialized)
            {
                getNextObject();
                initialized=true;
            }
            return next!=null;
        }

        public Object nextElement()
        {
            // Kludge because of compiler bug
            if ( ! initialized)
            {
                getNextObject();
                initialized=true;
            }
            if ( next==null)
                throw new NoSuchElementException();
            Object retVal=next;
            getNextObject();
            return retVal;
        }
    }

    static class Polymorphic extends TransformImpl
    {
        private AnalyzerDB db;

        Polymorphic() {
            super( DBMethod.class, DBMethod.class);
        }

        public void startEvaluation( DataSource adb)
        {
            db=(AnalyzerDB)adb;
        }
        
        static class RealMethodFilter extends FilterEnumeration
        {
        	private DBMethod soughtMethod;
        	private AnalyzerDB db;
        	
        	RealMethodFilter( Enumeration base, AnalyzerDB source, DBMethod sought)
        	{
        		super( base);
        		db=source;
        		soughtMethod=sought;
        	}
            protected Object filterObject( Object base)
            {
                DBClass filterc=(DBClass)base;
                try
                {
                    DBMethod m=(DBMethod) db.findWithKey( "com.antlersoft.analyzer.DBMethod",
                        DBMethod.makeKey( filterc.getInternalName(),
                        soughtMethod.getName(),
                        soughtMethod.getSignature()));
                    return m;
                }
                catch ( Exception e)
                {
                    throw new NoSuchElementException(
                        e.getMessage());
                }
            }
        	
        }

        public Enumeration transformObject( DataSource source, Object toTransform)
        {
        	try
        	{
	            final DBMethod soughtMethod=(DBMethod)toTransform;
	            DBClass c=soughtMethod.getDBClass();
	            Enumeration fromBase=new RealMethodFilter(
	                ( new TransformSet( new RecursiveBaseClasses(),
	                		new SingleClass( c))).evaluate( db), db, soughtMethod);
	            Enumeration fromDerived=new  RealMethodFilter(
		                ( new TransformSet( new RecursiveDerivedClasses(),
		                		new SingleClass( c))).evaluate( db), db, soughtMethod);
	            return new CombineEnum( new SingleEnum(
	                soughtMethod), new CombineEnum( fromBase,
	                fromDerived));
        	}
        	catch ( BindException be)
        	{
        		throw new RuntimeException( be);
        	}
        }
    }

    static class UncalledMethod extends CountPreservingFilter
    {
        UncalledMethod()
        {
        	m_bind=new BindImpl( BOOLEAN_CLASS, DBMethod.class);
        }

        protected boolean getCountPreservingFilterValue( DataSource source, Object toCheck)
        {
            return ! ((DBMethod)toCheck).getCalledBy().hasMoreElements();
        }
        
        private BindImpl m_bind;

		/* (non-Javadoc)
		 * @see com.antlersoft.query.BindImpl#appliesClass()
		 */
		public Class appliesClass() {
			return m_bind.appliesClass();
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.query.BindImpl#lateBindApplies(java.lang.Class)
		 */
		public void lateBindApplies(Class new_applies) throws BindException {
			m_bind.lateBindApplies(new_applies);
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
	                    getCalledBy().hasMoreElements())
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
}