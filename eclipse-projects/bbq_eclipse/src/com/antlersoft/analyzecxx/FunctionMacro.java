package com.antlersoft.analyzecxx;

import java.util.ArrayList;

class FunctionMacro extends Macro
{
	private ArrayList m_replacement_list;

	FunctionMacro( String name, int arguments, ArrayList replacement_list)
	{
		super( name);
	}

	void expandTo( LexReader reader, ArrayList arguments, ArrayList expanded_arguments)
	{

	}
}