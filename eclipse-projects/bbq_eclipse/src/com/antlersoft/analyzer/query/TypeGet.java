/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBType;
import com.antlersoft.analyzer.IndexAnalyzeDB;

import com.antlersoft.query.CountPreservingValueExpression;
import com.antlersoft.query.DataSource;

/**
 * Simple value expression that gets a type from a string
 * @author mike
 *
 */
public class TypeGet extends CountPreservingValueExpression {
	
	public TypeGet( ) {
		super( DBType.class, String.class);
	}

	public Object transformSingleObject(DataSource source, Object toTransform) {
		String name=(String)toTransform;
		String array_references="";
		IndexAnalyzeDB db=(IndexAnalyzeDB)source;
		for ( int array_offset; ( array_offset=name.lastIndexOf("[]"))!= -1;)
		{
			name=name.substring( 0, array_offset);
			array_references="["+array_references;
		}
		String base_type=DBType.TypeStringMap.userToInternal( name);
		DBType found=null;
		try
		{
			if ( base_type==null)
			{
				DBClass referenced=(DBClass)db.findWithIndex( DBClass.CLASS_NAME_INDEX, name);
				if ( referenced!=null)
				{
					found=DBType.getFromClass( db, referenced);
					if ( array_references.length()>0)
						found=(DBType)db.findWithIndex( DBType.TYPE_KEY_INDEX,
								array_references+found.getTypeString());
				}
			}
			else
				found=(DBType)db.findWithIndex( DBType.TYPE_KEY_INDEX, array_references+base_type);
		}
		catch ( Exception e)
		{
			
		}
		
		return found;
	}

}
