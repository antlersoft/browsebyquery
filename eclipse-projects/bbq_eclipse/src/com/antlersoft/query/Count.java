package com.antlersoft.query;

public class Count extends GroupExpression {
	public Count()
	{
		super( Integer.class, null);
	}
    public Object getValue() {
		return new Integer( m_count);
    }
    public void startGroup(ValueObject vobj, DataSource source) {
		m_count=0;
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		++m_count;
		return true;
    }

	private int m_count;
}