package com.antlersoft.parser;

public class GotoRule
{
		Symbol looked_for;
		int state_index;

		public GotoRule( Symbol looked, int index)
		{
			looked_for=looked;
			state_index=index;
		}
}
