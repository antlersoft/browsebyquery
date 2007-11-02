/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import java.util.Enumeration;

import com.antlersoft.ilanalyze.db.DBClass;
import com.antlersoft.query.DataSource;
import com.antlersoft.query.EmptyEnum;
import com.antlersoft.query.UniqueTransformImpl;

class RecursiveBaseClasses extends UniqueTransformImpl
{
	RecursiveBaseClasses()
	{
		super( DBClass.class, DBClass.class);
	}
    private void addSuperClasses( DBClass current)
    {
        Enumeration superEnum=current.getBaseClasses();
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