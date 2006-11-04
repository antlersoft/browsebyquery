/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBType;
import com.antlersoft.analyzer.HasDBType;

import com.antlersoft.parser.RuleActionException;

import com.antlersoft.query.SingleEnum;

/**
 * This transform returns the type of an object that implements HasDBType, or the class type
 * for a class object.
 * The applies-to type must be late-bound.
 * @author Michael MacDonald
 *
 */
public class TypeOf extends TransformImpl {

	private AnalyzerDB _db;
	private TypeOfTransformer _transformer;
	
	/**
	 * The applies-to type must be late-bound.
	 */
	public TypeOf() {
		super(null, DBType.class);
	}
	
	public void startEvaluation( AnalyzerDB db)
	{
		_db=db;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.TransformImpl#transform(java.lang.Object)
	 */
	public Enumeration transform(Object toTransform) throws Exception {
		DBType type=_transformer.transform( _db, toTransform);
		return type==null ? (Enumeration)EmptyEnumeration.emptyEnumeration : new SingleEnum( type);
	}
	
	public void lateBindApplies( Class newApplies)
	throws RuleActionException
	{
		if ( newApplies == Class.class)
		{
			_applies=newApplies;
			_transformer=new TypeOfTransformer() {
				public DBType transform( AnalyzerDB db, Object toTransform)
				{
					return DBType.getFromClass( db, (DBClass)toTransform); 
				}
			};
		}
		else if ( HasDBType.class.isAssignableFrom( newApplies))
		{
			_applies=newApplies;
			_transformer=new TypeOfTransformer() {
				public DBType transform( AnalyzerDB db, Object toTransform)
				{
					return ((HasDBType)toTransform).getDBType();
				}
			};
		}
		else
			throw new RuleActionException( newApplies.getName()+" is not a typed object");
	}
	
	private static interface TypeOfTransformer
	{
		public DBType transform( AnalyzerDB db, Object toTransform) throws Exception;
	}
}
