package com.antlersoft.analyzecxx;

/**
 * Used in handling token concatenation in macro replacement lists
 * when defining macros
 */
interface ConcatenatedTokenPairReplacer {
	/**
	 * Returns a token that will replace a pair of tokens with a
	 * concatenation operator between them in a token list.  May return
	 * null if no suitable replacement token is found.
	 */
	LexToken replacePair( LexToken first, LexToken second);
}