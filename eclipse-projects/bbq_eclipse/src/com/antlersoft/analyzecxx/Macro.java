package com.antlersoft.analyzecxx;

/**
 * Base class for all preprocessor macro definitions
 */
class Macro
{
	private String m_identifier;

	Macro( String identifier)
	{
		m_identifier=identifier;
	}

	String getIdentifier()
	{
		return m_identifier;
	}
}