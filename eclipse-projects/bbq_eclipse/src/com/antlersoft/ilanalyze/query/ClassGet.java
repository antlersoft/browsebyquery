/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import com.antlersoft.ilanalyze.db.ILDB;
import com.antlersoft.ilanalyze.db.DBClass;

import com.antlersoft.query.DataSource;
import com.antlersoft.query.SetExpression;

/**
 * Find DBClass objects that match the user name, searching with the set of prefixes specified.
 * @author Michael A. MacDonald
 *
 */
class ClassGet extends SetExpression {

    private String _className;
    private Collection _set;

    /**
     * 
     * @param className User-readable class name
     * @param set Set of namespaces to search
     */
    ClassGet( String className, Collection set)
    {
        _className=className;
        _set=set;
    }

	/* (non-Javadoc)
	 * @see com.antlersoft.query.SetExpression#evaluate(com.antlersoft.query.DataSource)
	 */
	public Enumeration evaluate(DataSource source) {
        Vector tmp=new Vector( 1);
        ILDB db=(ILDB)source;
        Object q=db.findObject( DBClass.CLASS_BY_NAME_INDEX, _className);
        if ( q!=null)
            tmp.addElement( q);
        for ( Iterator i=_set.iterator(); i.hasNext() && q==null;)
        {
            q=db.findObject( DBClass.CLASS_BY_NAME_INDEX, ((String)i.next())+"."+_className);
            if ( q!=null)
                tmp.addElement( q);
        }
        return tmp.elements();
 	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.SetExpression#getResultClass()
	 */
	public Class getResultClass() {
    	return DBClass.class;
    }


}
