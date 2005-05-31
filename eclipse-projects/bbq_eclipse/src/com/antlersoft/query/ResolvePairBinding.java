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

public class ResolvePairBinding implements Bindable {
	public ResolvePairBinding( Bindable a, Bindable b)
	throws BindException
	{
		m_a=a;
		m_b=b;
		resolveBinding();
	}
    public Class resultClass() {
		return m_result;
    }
    public Class appliesClass() {
		return m_applies;
    }
    public void lateBindResult(Class new_result) throws BindException {
		m_a.lateBindResult( new_result);
		m_b.lateBindResult( new_result);
		resolveBinding();
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		m_a.lateBindApplies( new_applies);
		m_b.lateBindApplies( new_applies);
		resolveBinding();
    }
	private void resolveBinding() throws BindException
	{
		resolveBindingResult();
		resolveBindingApplies();
	}

	private void resolveBindingResult()
		throws BindException
	{
	    Class appliesA=m_a.resultClass();
		Class appliesB=m_b.resultClass();
		if ( appliesA==appliesB)
		{
			m_result=appliesA;
		}
		else
		{
			if ( appliesB==null)
			{
				m_b.lateBindResult( appliesA);
				appliesB=m_b.resultClass();
			}
			if ( appliesA==null)
			{
				m_a.lateBindResult( appliesB);
				appliesA=m_a.resultClass();
			}
			if ( appliesA==appliesB)
			{
				m_result=appliesA;
			}
			else
			{
				if ( appliesA.isAssignableFrom( appliesB))
				{
					m_result=appliesA;
				}
				else if ( appliesB.isAssignableFrom( appliesA))
				{
					m_result=appliesB;
				}
				else
					throw new BindException( "Sub-expression bindings are incompatible");
			}
		}
	}
	private void resolveBindingApplies()
		throws BindException
	{
		Class appliesA=m_a.appliesClass();
		Class appliesB=m_b.appliesClass();
		if ( appliesA==appliesB)
		{
			m_applies=appliesA;
		}
		else
		{
			if ( appliesB==null)
			{
				m_b.lateBindApplies( appliesA);
				appliesB=m_b.appliesClass();
			}
			if ( appliesA==null)
			{
				m_a.lateBindApplies( appliesB);
				appliesA=m_a.appliesClass();
			}
			if ( appliesA==appliesB)
			{
				m_applies=appliesA;
			}
			else
			{
				if ( appliesA.isAssignableFrom( appliesB))
				{
					m_applies=appliesB;
				}
				else if ( appliesB.isAssignableFrom( appliesA))
				{
					m_applies=appliesA;
				}
				else
					throw new BindException( "Sub-expression bindings are incompatible");
			}
		}
	}

	private Bindable m_a;
	private Bindable m_b;
	private Class m_result;
	private Class m_applies;
}