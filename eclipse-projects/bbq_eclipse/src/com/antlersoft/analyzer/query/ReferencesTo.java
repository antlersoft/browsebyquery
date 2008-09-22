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

import java.util.Enumeration;

import com.antlersoft.analyzer.DBFieldReference;
import com.antlersoft.analyzer.DBField;
import com.antlersoft.analyzer.DBReference;
import com.antlersoft.analyzer.DBStringConstant;
import com.antlersoft.analyzer.DBStringReference;
import com.antlersoft.analyzer.IndexAnalyzeDB;

import com.antlersoft.query.BindException;
import com.antlersoft.query.DataSource;
import com.antlersoft.query.EmptyEnum;
import com.antlersoft.query.Transform;

class ReferencesTo extends Transform
{
	ReferencesTo()
	{
		m_applies=m_result=null;
	}

	public Enumeration transformObject( DataSource source, Object base)
	{
        if ( appliesClass()==null)
        {
            throw new RuntimeException( "Unbound reference to");
        }
        if ( appliesClass()==DBStringConstant.class)
        {
            return ((DBStringConstant)base).getReferencedBy((IndexAnalyzeDB)source);
        }
        else
		    return ((DBField)base).getReferencesTo((IndexAnalyzeDB)source);
	}

	public void startEvaluation( DataSource source) {}
	public Enumeration finishEvaluation( DataSource source) { return EmptyEnum.empty; }

	/* (non-Javadoc)
	 * @see com.antlersoft.query.BindImpl#appliesClass()
	 */
	public Class appliesClass() {
		return m_applies;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.BindImpl#resultClass()
	 */
	public Class resultClass() {
		return m_result;
	}

	public void lateBindResult( Class newResultClass)
		throws BindException
	{
        if ( newResultClass==DBStringReference.class && ( m_applies==null ||
            m_applies==DBStringConstant.class))
        {
            m_applies=DBStringConstant.class;
            m_result= DBStringReference.class;
        }
        else if ( newResultClass==DBFieldReference.class && ( m_applies==null
            || m_applies==DBField.class))
        {
            m_applies=DBField.class;
            m_result=DBFieldReference.class;
        }
        else if ( newResultClass==DBReference.class)
        {
        }
        else
    		throw new BindException(
    			getClass().getName()+" can not be bound for result to "+
                newResultClass.getName());
	}

	public void lateBindApplies( Class newAppliesClass)
		throws BindException
	{
        if ( newAppliesClass==DBStringConstant.class && ( m_applies==null ||
            m_applies==DBStringConstant.class))
        {
            m_applies=DBStringConstant.class;
            m_result=DBStringReference.class;
        }
        else if ( newAppliesClass==DBField.class && ( m_applies==null
            || m_applies==DBField.class))
        {
            m_applies=DBField.class;
            m_result=DBFieldReference.class;
        }
        else
    		throw new BindException(
    			getClass().getName()+" can not be bound for applies to "+
                newAppliesClass.getName());
	}
	
	private Class m_applies;
	private Class m_result;
}
