/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.DBType;
import com.antlersoft.analyzer.HasDBType;

import com.antlersoft.parser.RuleActionException;

import com.antlersoft.query.SingleEnum;

/**
 * @author Michael MacDonald
 *
 */
public class TypeOf extends TransformImpl {

	/**
	 * @param applies
	 * @param result
	 */
	public TypeOf() {
		super(null, DBType.class);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.TransformImpl#transform(java.lang.Object)
	 */
	public Enumeration transform(Object toTransform) throws Exception {
		DBType type=((HasDBType)toTransform).getDBType();
		return type==null ? (Enumeration)EmptyEnumeration.emptyEnumeration :
			(Enumeration)new SingleEnum( type);
	}
	
	public void lateBindApplies( Class newApplies)
	throws RuleActionException
	{
		if ( HasDBType.class.isAssignableFrom( newApplies))
		{
			_applies=newApplies;
		}
		else
			throw new RuleActionException( newApplies.getName()+" is not a typed object");
	}
}
