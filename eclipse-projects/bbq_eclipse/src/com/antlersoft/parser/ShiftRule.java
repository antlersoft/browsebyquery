package parser;

public class ShiftRule
{
		Symbol looked_for;
		int state_index;

		public ShiftRule( Symbol looked, int index)
		{
			looked_for=looked;
			state_index=index;
		}
}

