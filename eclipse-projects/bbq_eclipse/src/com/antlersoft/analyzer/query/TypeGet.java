/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBType;

import com.antlersoft.analyzer.query.EmptyEnumeration;

import com.antlersoft.query.SingleEnum;

/**
 * Simple set expression that gets a type from a string
 * @author mike
 *
 */
public class TypeGet extends SetExpression {
	
	private String _name;

	/**
	 * @param name of the type (class name or name of one of the built-in types, or a type name with []'s
	 * to indicate array references)
	 */
	public TypeGet( String name) {
		super( DBType.class);
		_name=name;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.query.SetExpression#execute(com.antlersoft.analyzer.AnalyzerDB)
	 */
	public Enumeration execute(AnalyzerDB db) throws Exception {
		String name=_name;
		String array_references="";
		for ( int array_offset; ( array_offset=name.lastIndexOf("[]"))!= -1;)
		{
			name=name.substring( 0, array_offset);
			array_references="["+array_references;
		}
		String base_type=DBType.TypeStringMap.userToInternal( name);
		DBType found=null;
		if ( base_type==null)
		{
			DBClass referenced=(DBClass)db.findWithKey( "com.antlersoft.analyzer.DBClass", name);
			if ( referenced==null)
				return EmptyEnumeration.emptyEnumeration;
			found=DBType.getFromClass( db, referenced);
			if ( array_references.length()>0)
				found=(DBType)db.findWithKey( "com.antlersoft.analyzer.DBType",
						array_references+found.getTypeString());
		}
		else
			found=(DBType)db.findWithKey( "com.antlersoft.analyzer.DBType", array_references+base_type);
		if ( found==null)
			return EmptyEnumeration.emptyEnumeration;
		
		return new SingleEnum( found);
	}

}
