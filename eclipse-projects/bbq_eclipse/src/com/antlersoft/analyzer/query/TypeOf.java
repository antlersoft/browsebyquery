/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.DBType;
import com.antlersoft.analyzer.HasDBType;

import com.antlersoft.query.CountPreservingValueExpression;
import com.antlersoft.query.DataSource;

/**
 * This expression returns the type of an object that implements HasDBType
 * @author Michael MacDonald
 *
 */
public class TypeOf extends CountPreservingValueExpression {

	public TypeOf()
	{
		super(HasDBType.class, DBType.class);
	}
	
	public Object transformSingleObject(DataSource source, Object toTransform) {
		return ((HasDBType)toTransform).getDBType( (AnalyzerDB)source);
	}
	
}
