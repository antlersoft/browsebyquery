/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.Comparator;

/**
 * Remove duplicates in an ordered or unordered set
 * @author Michael A. MacDonald
 *
 */
public class UniqueTransform extends UniqueTransformImpl {
	
	public UniqueTransform()
	{
		super( null, null);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.UniqueTransformImpl#uniqueTransform(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	public Object uniqueTransform(DataSource source, Object to_transform) {
		return to_transform;
	}

	public Comparator getOrdering()
	{
		return m_ordering;
	}

	public void bindOrdering( Comparator ordering)
	{
		m_ordering=ordering;
	}

	private Comparator m_ordering;
}
