package com.antlersoft.query;

public interface GroupValueContext extends ValueContext {
	void startGroup( ValueObject vobj, DataSource source);
	boolean addObject( ValueObject vobj, DataSource source, Object to_add);
	void finishGroup( ValueObject vobj, DataSource source);
}