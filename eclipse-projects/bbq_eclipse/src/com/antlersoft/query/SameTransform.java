package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public class SameTransform extends TransformImpl {
	public SameTransform()
	{
		super( null, null);
	}

	public Enumeration transformObject( DataSource source, Object
										to_transform)
	{
		return new SingleEnum( to_transform);
	}

	public Comparator getOrdering()
	{
		return m_ordering;
	}

	public void bindOrdering( Comparator ordering)
	{
		m_ordering=ordering;
	}

	Comparator m_ordering;
}