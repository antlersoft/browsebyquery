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
package com.antlersoft.query;

import java.util.ArrayList;
import java.util.Enumeration;

public class RecursiveTransform extends Transform {

	public RecursiveTransform( Transform main)
	{
		m_main_transform=main;
	}
	public RecursiveTransform( Transform main, Transform sub)
	{
		m_main_transform=main;
		m_sub_transform=sub;
	}
    public Enumeration finishEvaluation(DataSource source) {
		return m_main_transform.finishEvaluation( source);
    }
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_main_transform.lateBindApplies( new_applies);
		if ( m_sub_transform!=null)
		{
			if ( appliesClass()!=null)
				m_sub_transform.lateBindResult( appliesClass());
			if ( resultClass()!=null)
				m_sub_transform.lateBindApplies( resultClass());
		}
    }
    public void startEvaluation(DataSource source) {
		m_main_transform.startEvaluation( source);
		if ( m_sub_transform!=null)
			m_sub_transform.startEvaluation( source);
    }
    public Class resultClass() {
		return m_main_transform.resultClass();
    }
    public Class appliesClass() {
		return m_main_transform.appliesClass();
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		m_main_transform.lateBindResult( new_result);
		if ( m_sub_transform!=null)
		{
			if ( resultClass()!=null)
				m_sub_transform.lateBindApplies( resultClass());
			if ( appliesClass()!=null)
				m_sub_transform.lateBindResult( appliesClass());
		}
    }
    public Enumeration transformObject(DataSource source, Object to_transform) {
		return new RecursiveEnumeration( source, m_main_transform.transformObject( source, to_transform));
    }

	Transform m_main_transform;
	Transform m_sub_transform;

	class RecursiveEnumeration implements Enumeration
	{
		RecursiveEnumeration( DataSource source, Enumeration initial)
		{
			m_source=source;
			if ( initial.hasMoreElements())
				m_enum_stack.add( initial);
		}

		public boolean hasMoreElements()
		{
			return ! m_enum_stack.isEmpty();
		}

		public Object nextElement()
		{
			Enumeration top_enum=(Enumeration)m_enum_stack.get( m_enum_stack.size()-1);
			Object result=top_enum.nextElement();
			if ( ! top_enum.hasMoreElements())
				m_enum_stack.remove( m_enum_stack.size()-1);
			if ( m_sub_transform==null)
			{
				Enumeration next_enum=m_main_transform.transformObject( m_source, result);
				if ( next_enum.hasMoreElements())
					m_enum_stack.add( next_enum);
			}
			else
			{
				Enumeration sub_enum=m_sub_transform.transformObject( m_source, result);
				while ( sub_enum.hasMoreElements())
				{
					Enumeration next_enum=m_main_transform.transformObject( m_source, sub_enum.nextElement());
					if ( next_enum.hasMoreElements())
						m_enum_stack.add( next_enum);
				}
			}
			return result;
		}
		private DataSource m_source;
		private ArrayList m_enum_stack;
	}
}