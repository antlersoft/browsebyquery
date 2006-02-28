package com.antlersoft.query.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import com.antlersoft.query.*;

import com.antlersoft.util.IteratorEnumeration;

class Invoker {
	Invoker( String name, Transform transform)
	{
		m_name=name;
		m_transform=transform;
	}

	private void getArguments( DataSource source, Object arg)
	{
		Enumeration enum_args=((SetExpression)
							   m_transform.transformObject( source, arg)).
			evaluate( source);
		for ( ; enum_args.hasMoreElements(); )
		{
			m_parameters.add( enum_args.nextElement());
		}
		m_parameter_types=m_parameters.size()==0 ? null : new Class[m_parameters.size()];
		int j=0;
		for ( Iterator i=m_parameters.iterator(); i.hasNext(); ++j)
			m_parameter_types[j]=i.next().getClass();
		m_arguments=m_parameters.toArray();
	}

	Object invokeConstructor( DataSource source, Object arg)
	{
		getArguments( source, arg);
		Class class_obj;
		if ( arg instanceof Class)
			class_obj=(Class)arg;
		else
			class_obj=arg.getClass();
		try
		{
			Constructor const_obj=class_obj.getConstructor( m_parameter_types);
			return const_obj.newInstance( m_arguments);
		}
		catch ( Exception e)
		{
			throw new ReflectFailure( e);
		}
	}

	private Method getMethod( DataSource source, Object arg)
	{
		getArguments( source, arg);
		Class class_obj;
		if ( arg instanceof Class)
			class_obj=(Class)arg;
		else
			class_obj=arg.getClass();
		try
		{
			return class_obj.getMethod( m_name, m_parameter_types);
		}
		catch ( Exception e)
		{
			throw new ReflectFailure( e);
		}
	}

	Object invokeItemReturn( DataSource source, Object arg)
	{
		try
		{
			return getMethod(source, arg).invoke(arg, m_arguments);
		}
		catch ( Exception e)
		{
			throw new ReflectFailure( e);
		}
	}

	Enumeration invokeEnumerationReturn( DataSource source, Object arg)
	{
		Method m=getMethod( source, arg);
		Class return_type=m.getReturnType();
		Object result;
		try
		{
			result = m.invoke(arg, m_arguments);
		}
		catch (Exception e) {
			throw new ReflectFailure(e);
		}
		if ( Enumeration.class.isAssignableFrom( return_type))
			return (Enumeration)result;
		if ( Iterator.class.isAssignableFrom( return_type))
			return new IteratorEnumeration( (Iterator)result);
		return new SingleEnum( result);
	}

	Class[] m_parameter_types;
	Object[] m_arguments;
    ArrayList m_parameters;
	Transform m_transform;
	String m_name;
}