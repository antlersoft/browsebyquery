package parser;

public class ParseState
{
	public ShiftRule[] shift_rules;
	public GotoRule[] goto_rules;
	public ReduceRule reduce_rule;

	public ParseState( ShiftRule[] sr, GotoRule[] gr, ReduceRule rr)
	{
		shift_rules=sr;
		goto_rules=gr;
		reduce_rule=rr;
	}
};
