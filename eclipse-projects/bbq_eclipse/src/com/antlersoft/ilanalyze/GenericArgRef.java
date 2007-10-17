/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An unspecified type that references a generic parameter in the containing
 * class or method.
 * The parameter may be reference by name or index.
 * @author Michael A. MacDonald
 *
 */
public class GenericArgRef implements ReadType {
	
	private boolean m_is_method;
	private int m_param_index;
	private String m_param_name;
	
	/**
	 * Generic parameter reference by position
	 * @param param_index 0 based index of parameter in generic parameter list this is referencing
	 * @param is_method true if type references a generic method parameter, false if it is a generic class parameter
	 */
	public GenericArgRef( int param_index, boolean is_method)
	{
		m_param_index=param_index;
		m_is_method=is_method;
	}
	
	/**
	 * Generic parameter reference by name
	 * @param param_name name of generic parameter this type references
	 * @param is_method true if type references a generic method parameter, false if it is a generic class parameter
	 */
	public GenericArgRef( String param_name, boolean is_method)
	{
		m_param_name=param_name;
		m_is_method=is_method;
		m_param_index= -1;
	}
	
	

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#getGenericArgs()
	 */
	public Collection getGenericArgs() {
		return new ArrayList();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#isFullySpecified()
	 */
	public boolean isFullySpecified() {
		return false;
	}
	
	/**
	 * 
	 * @return 0-based index in parameter list, or -1 if parameter is reference by name
	 */
	public int getParamIndex()
	{
		return m_param_index;
	}
	
	/**
	 * 
	 * @return parameter name, or null if parameter is reference by index
	 */
	public String getParamName()
	{
		return m_param_name;
	}
	
	/**
	 * 
	 * @return true if type represents a generic method parameter, false if it is a generic class parameter
	 */
	public boolean isMethodArg()
	{
		return m_is_method;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append('!');
		if ( m_is_method)
			sb.append('!');
		if ( m_param_index!= -1)
			sb.append( m_param_index);
		else
			sb.append( m_param_name);
		return sb.toString();
	}

}
