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
