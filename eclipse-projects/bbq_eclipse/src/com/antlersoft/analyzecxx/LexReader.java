package com.antlersoft.analyzecxx;

/**
 * Interface that receives LexTokens after they are read from the input
 * characters
 */
interface LexReader
{
	void processToken( LexToken next_token)
		throws com.antlersoft.parser.RuleActionException;

	void noMoreTokens()
		throws com.antlersoft.parser.RuleActionException;
}