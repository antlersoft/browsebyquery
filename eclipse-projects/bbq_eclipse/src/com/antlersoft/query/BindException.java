package com.antlersoft.query;

import com.antlersoft.parser.RuleActionException;

public class BindException extends RuleActionException {
	public BindException( String msg)
	{
		super( msg);
	}
}