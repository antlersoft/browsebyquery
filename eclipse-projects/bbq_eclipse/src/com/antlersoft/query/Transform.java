package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public abstract class Transform implements Bindable {
	public abstract void startEvaluation( DataSource source);
	public abstract Enumeration transformObject( DataSource source, Object to_transform);
	public abstract Enumeration finishEvaluation( DataSource source);
	public boolean isAbleToUseMore()
	{ return true; }
    public Comparator getOrdering()
	{
		return null;
	}
	void bindOrdering( Comparator ord) {};
}