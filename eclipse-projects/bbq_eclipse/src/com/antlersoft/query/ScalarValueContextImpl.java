package com.antlersoft.query;

public class ScalarValueContextImpl implements ScalarValueContext {
    public void startGroup(ValueObject vobj, DataSource source) {
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		return false;
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
    }
    public void inputObject(ValueObject value, DataSource source, Object input) {
    }
    public int getContextType() {
		return ValueContext.SCALAR;
    }
}