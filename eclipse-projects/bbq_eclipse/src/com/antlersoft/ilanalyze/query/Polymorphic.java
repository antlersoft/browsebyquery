/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import com.antlersoft.ilanalyze.db.DBClass;
import com.antlersoft.ilanalyze.db.DBMethod;
import com.antlersoft.ilanalyze.db.ILDB;

import com.antlersoft.query.BindException;
import com.antlersoft.query.CombineEnum;
import com.antlersoft.query.DataSource;
import com.antlersoft.query.FilterEnumeration;
import com.antlersoft.query.SetExpression;
import com.antlersoft.query.SingleEnum;
import com.antlersoft.query.TransformImpl;
import com.antlersoft.query.TransformSet;

/**
 * @author Michael A. MacDonald
 *
 */
class Polymorphic extends TransformImpl {
	private ILDB _db;

	/**
	 * 
	 */
	public Polymorphic() {
		super(DBMethod.class, DBMethod.class);
	}

    public void startEvaluation( DataSource adb)
    {
        _db=(ILDB)adb;
    }
    
	/* (non-Javadoc)
	 * @see com.antlersoft.query.Transform#transformObject(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	public Enumeration transformObject(DataSource source, Object to_transform) {
        final DBMethod soughtMethod=(DBMethod)to_transform;
 		try
		{
	        DBClass c=soughtMethod.getDBClass();
	        Enumeration fromBase=new RealMethodFilter(
	            ( new TransformSet( new RecursiveBaseClasses(),
	            		new SingleClass( c))).evaluate( _db), _db, soughtMethod);
	        Enumeration fromDerived=new  RealMethodFilter(
	                ( new TransformSet( new RecursiveDerivedClasses(),
	                		new SingleClass( c))).evaluate( _db), _db, soughtMethod);
	        return new CombineEnum( new SingleEnum(
	                soughtMethod), new CombineEnum( fromBase,
	                fromDerived));
		}
		catch ( BindException be)
		{
			// This should never happen
			throw new RuntimeException( be);
		}
	}

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
 
    static class RealMethodFilter extends FilterEnumeration
    {
    	private DBMethod soughtMethod;
    	private ILDB db;
    	
    	RealMethodFilter( Enumeration base, ILDB source, DBMethod sought)
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
                DBMethod m=filterc.findMethod( soughtMethod.getName(), soughtMethod.getSignatureKey());
                return m;
            }
            catch ( Exception e)
            {
                throw new NoSuchElementException(
                    e.getMessage());
            }
        }
    	
    }
}
