package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;

/**
 * Interface implemented by objects that represent lexing states
 */
interface LexState
{
	LexState nextCharacter( char c)
		throws IOException, RuleActionException, LexException;
	LexState endOfFile()
		throws IOException, RuleActionException, LexException;
}
