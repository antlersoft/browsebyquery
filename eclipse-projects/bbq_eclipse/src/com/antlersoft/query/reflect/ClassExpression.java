package com.antlersoft.query.reflect;

import com.antlersoft.query.*;


public class ClassExpression extends CountPreservingValueExpression {
    public ClassExpression()
	throws BindException
	{
		super( Class.class, String.class);
    }
    protected Object transformSingleObject(DataSource source, Object to_transform) {
		try
		{
			return getClass().getClassLoader().loadClass( (String) to_transform);
		}
		catch ( ClassNotFoundException cnfe)
		{
			return null;
		}
    }
}