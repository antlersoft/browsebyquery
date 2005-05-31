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

import com.antlersoft.parser.RuleActionException;

class ReferencesTo extends TransformImpl
{
	ReferencesTo()
	{
		super( null, null);
	}

	public Enumeration transform( Object base)
		throws Exception
	{
        if ( _applies==null)
        {
            throw new Exception( "Unbound reference to");
        }
        if ( _applies==DBStringConstant.class)
        {
            return ((DBStringConstant)base).getReferencedBy();
        }
        else
		    return ((DBField)base).getReferencedBy();
	}

 	public void lateBindResult( Class newResultClass)
		throws RuleActionException
	{
        if ( newResultClass==DBStringReference.class && ( _applies==null ||
            _applies==DBStringConstant.class))
        {
            _applies=DBStringConstant.class;
            _result=DBStringReference.class;
        }
        else if ( newResultClass==DBFieldReference.class && ( _applies==null
            || _applies==DBField.class))
        {
            _applies=DBField.class;
            _result=DBFieldReference.class;
        }
        else if ( newResultClass==DBReference.class)
        {
        }
        else
    		throw new RuleActionException(
    			getClass().getName()+" can not be bound for result to "+
                newResultClass.getName());
	}

	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException
	{
        if ( newAppliesClass==DBStringConstant.class && ( _applies==null ||
            _applies==DBStringConstant.class))
        {
            _applies=DBStringConstant.class;
            _result=DBStringReference.class;
        }
        else if ( newAppliesClass==DBField.class && ( _applies==null
            || _applies==DBField.class))
        {
            _applies=DBField.class;
            _result=DBFieldReference.class;
        }
        else
    		throw new RuleActionException(
    			getClass().getName()+" can not be bound for applies to "+
                newAppliesClass.getName());
	}
}
