package com.antlersoft.query;

import java.util.List;

public class FirstOf extends GroupExpression {
   public Object getValue() {
	   return m_result;
    }
    public void startGroup(ValueObject vobj, DataSource source) {
		m_result=null;
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		if ( m_result==null)
			m_result=to_add;
		return false;
    }

	private Object m_result;
}