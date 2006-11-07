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

public class BindImpl implements Bindable {
	private Class m_result;
	private Class m_applies;

	public BindImpl( Class result, Class applies)
	{
		m_result=result;
		m_applies=applies;
	}
    public Class resultClass() {
		return m_result;
    }
    public Class appliesClass() {
		return m_applies;
    }
    public void lateBindResult(Class new_result) throws BindException {
		boolean changed=false;
		if ( m_result!=null)
		{
			if ( m_result!=new_result)
			{
				if ( new_result.isAssignableFrom( m_result))
				{
					m_result= new_result;
					changed=true;
				}
				else
					throw new BindException( "Can't bind result class "+new_result.getName()+" in place of "+
											 m_result.getName());
			}
		}
		else
		{
			m_result= new_result;
			changed=true;
		}
		if ( changed && m_applies==null)
		{
			m_applies= new_result;
		}
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		boolean changed=false;
		if ( m_applies!=null)
		{
			if ( m_applies!=new_applies)
			{
				if ( m_applies.isAssignableFrom( new_applies))
				{
					m_applies= new_applies;
					changed=true;
				}
				else
					throw new BindException( "Can't bind applies class "+new_applies.getName()+" in place of "+
											 m_applies.getName());
			}
		}
		else
		{
			m_applies= new_applies;
			changed=true;
		}
		if ( changed && m_result==null)
		{
			m_result= new_applies;
		}
    }
}