package com.antlersoft.parser;

public class ReduceRule
{
	Symbol result;
	int states_to_pop;
	RuleAction reduce_action;

	public ReduceRule( Symbol res, int to_pop, RuleAction action)
	{
		result=res;
		states_to_pop=to_pop;
		reduce_action=action;
	}

	public ReduceRule( Symbol res, int to_pop)
	{
		result=res;
		states_to_pop=to_pop;
		reduce_action=null;
	}
}

