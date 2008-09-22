/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBType;
import com.antlersoft.analyzer.HasDBType;
import com.antlersoft.analyzer.IndexAnalyzeDB;

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
		super(DBType.class, HasDBType.class);
	}
	
	public Object transformSingleObject(DataSource source, Object toTransform) {
		return ((HasDBType)toTransform).getDBType( (IndexAnalyzeDB)source);
	}
	
}
