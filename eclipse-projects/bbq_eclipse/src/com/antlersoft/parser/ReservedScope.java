/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

/**
 * A collection of Symbol objects that represent the reserved words
 * specific to a particular parser.
 * @author Michael A. MacDonald
 *
 */
public class ReservedScope {

	private Hashtable m_scope;
	/**
	 * 
	 */
	public ReservedScope() {
		super();
		m_scope=new Hashtable();
	}

	/**
	 * Returns the symbol corresponding to the parameter.  If the symbol does not
	 * exist, it is created and returned.
	 * @param name String representing symbol to find.
	 * @return Symbol for name
	 */
	public Symbol getReserved( String name)
	{
		Symbol result=(Symbol)m_scope.get( name);
		
		if ( result==null)
		{
			synchronized ( m_scope)
			{
				result=(Symbol)m_scope.get( name);
				if ( result==null)
				{
					result=Symbol.get( name);
					m_scope.put( name, result);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the symbol corresponding to the parameter, or null if such a symbol
	 * does not exist.
	 * @param name String representing symbol to find.
	 * @return Symbol for name
	 */
	public Symbol findReserved( String name)
	{
		return (Symbol)m_scope.get( name);
	}
	
	/**
	 * Returns a collection of all the reserved words in this scope
	 * @return A collection of strings that are the keys to this scope; the collection is unmodifiable
	 */
	public Collection getReservedStrings()
	{
		return Collections.unmodifiableSet(m_scope.keySet());
	}
}
