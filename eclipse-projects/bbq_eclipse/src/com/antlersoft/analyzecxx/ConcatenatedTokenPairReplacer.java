package com.antlersoft.analyzecxx;

/**
 * Used in handling token concatenation in macro replacement lists
 * when defining macros
 */
interface ConcatenatedTokenPairReplacer {
	LexToken replacePair( LexToken first, LexToken second);
}