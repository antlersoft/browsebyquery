/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import java.util.Enumeration;

import com.antlersoft.ilanalyze.db.DBClass;
import com.antlersoft.query.DataSource;
import com.antlersoft.query.EmptyEnum;
import com.antlersoft.query.UniqueTransformImpl;

class RecursiveDerivedClasses extends UniqueTransformImpl
{
	RecursiveDerivedClasses()
	{
		super( DBClass.class, DBClass.class);
	}
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