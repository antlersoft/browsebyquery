package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import java.util.Hashtable;

import com.antlersoft.analyzer.AnalyzerDB;

abstract class UniqueTransform extends TransformImpl
{
    private Hashtable ht;
    UniqueTransform( Class fromClass, Class toClass)
    {
		super( fromClass, toClass);
    }

	public void startEvaluation( AnalyzerDB db)
	{
		ht=new Hashtable();
	}

    public Enumeration transform( Object toTransform)
        throws Exception
    {
		Object c=uniqueTransform( toTransform);
		if ( c instanceof Enumeration)
		{
			Enumeration enum=(Enumeration)c;
			while ( enum.hasMoreElements())
			{
				Object e=enum.nextElement();
				ht.put( e, e);
			}
		}
		else
			ht.put( c, c);
        return EmptyEnumeration.emptyEnumeration;
    }

	abstract public Object uniqueTransform( Object toTransform)
		throws Exception;

	public Enumeration finishEvaluation()
	{
		Enumeration result= ht.keys();
		ht=null;
		return result;
	}
}
