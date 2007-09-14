/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.List;

/**
 * A pair of value objects that share context and type binding; used to construct more complex
 * value objects piecewise
 * @author Michael A. MacDonald
 *
 */
public class ValuePair extends ResolvePairBinding implements ValueObject {

	/**
	 * @param a
	 * @param b
	 * @throws BindException
	 */
	public ValuePair(ValueObject a, ValueObject b) throws BindException {
		super(a, b);
		m_context=new ResolvePairContext( a, b);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getContext()
	 */
	public ValueContext getContext() {
		return m_context;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getValueCollection()
	 */
	public List getValueCollection() {
		return m_context.getValueCollection();
	}
	
	private ResolvePairContext m_context;

}
