package parser;

import java.util.List;

public interface RuleAction
{
	public abstract Object ruleFire( Parser parser, List valueStack) throws RuleActionException;
}
