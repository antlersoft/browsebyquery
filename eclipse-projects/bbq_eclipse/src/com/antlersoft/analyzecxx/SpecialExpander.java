package com.antlersoft.analyzecxx;

import java.util.ArrayList;
import java.util.HashSet;

import com.antlersoft.parser.RuleActionException;

/**
 * Implemented by objects within function macro replacement lists that expand
 * specially
 */
interface SpecialExpander {
	public void expandTo( MacroExpander reader, HashSet no_expand, ArrayList arguments, ArrayList expanded_arguments)
	throws RuleActionException;
}