package parser;

import java.util.ArrayList;
import java.util.List;
       
public abstract class Parser
{
    // Public interface
	public static int max_recovery=3;
	public static Symbol _end_=Symbol.get( "_end");
	public static Symbol _error_=Symbol.get( "_error_");

	// allowable not implemented
	// public Collection allowable();
	public List getValueStack() { return (List)value_stack.clone(); }

	public String getRuleMessage()
	{
		return ruleMessage;
	}

	public boolean parse( Symbol next_symbol, Object value)
	{
		next_value.clear();
		next_value.add( next_symbol);
		next_value.add( value);
		boolean erred_out=false;
		ParseState cur=(ParseState)state_stack.get( state_stack.size()-1);

		while ( ! erred_out && ( next_value.size()>0 ||
			cur.shift_rules.length==0))
		{
{
for ( int q=0; q<parse_states.length; q++)
{
if ( parse_states[q]==cur)
{
System.out.println( "Parser examining state "+Integer.toString( q));
break;
}
}
}           
			int sri;
			int compare_length=cur.shift_rules.length;
			for ( sri=0; sri<compare_length; sri++)
			{
				if ( cur.shift_rules[sri].looked_for==next_value.get(0))
				{
					if ( cur.shift_rules[sri].state_index>=0)
					{   
						/* If you are in error state, keep track
						 * of how many tokens you've successfully
						 * pushed after, up to max_recovery */
						if ( recovery_count!=0)
						{
							++recovery_count;
							if ( recovery_count>max_recovery)
							   recovery_count=0;
						}
						cur=parse_states[cur.shift_rules[sri].state_index];
						state_stack.add( cur);
						value_stack.add( next_value.get( next_value.size()-1));
						next_value.clear();
					}
					else
					{              
						// Token forces error
						cur=pushError();
						erred_out=(cur==null);
					}
					break;
				}
			}
			if ( sri==compare_length)
			{
				if ( cur.reduce_rule==null)
				{
					// Can't shift and can't reduce
					/* try to push error state */
					cur=pushError();
					erred_out=(cur==null);
				}
				else
				{
					try
					{
						if ( tryReduce())
						{
							// If try reduce returns true, we are done; reset parser and return	
							reset();
							return false;
						}
						else
							cur=(ParseState)state_stack.get( state_stack.size()-1);
					}
					catch ( RuleActionException rae)
					{
						// Error firing the rule; interpret the same as no reduce rule at all

						// Preserve the message that cause the exception
						ruleMessage=rae.getMessage();
						cur=pushError();
						erred_out=(cur==null);
					}
				}
			}
		}
System.out.println( "parse function returning");
		return erred_out;
	}

	public boolean parse( Symbol nextSymbol)
			{ return parse( nextSymbol, new ArrayList()); }
	public void reset()
	{
		state_stack.clear();
		state_stack.add( parse_states[0]);
		value_stack.clear();
		value_stack.add( new ArrayList());
		ruleMessage=null;
		recovery_count=0;
	}

	public void clearRecovery()
	{
		recovery_count=0;
	}

	// Accessor methods for creating parser generators
	public ParseState[] getParseStates() 
	{
		return parse_states;
	}

    // Protected interface
	protected Parser( ParseState[] states)
	{
		parse_states=states;
		ruleMessage=null;
		recovery_count=0;
		state_stack=new ArrayList();
		state_stack.add( parse_states[0]);
		value_stack=new ArrayList();
		value_stack.add( "");
		next_value=new ArrayList(2);
	}

	protected void clearNextValue()
	{
		next_value.clear();
	}

 	protected static Symbol _complete=Symbol.get( "_complete");

   // Private implementation
	private boolean tryReduce() throws RuleActionException
	{
		ParseState cur=(ParseState)state_stack.get( state_stack.size()-1);
		if ( cur.reduce_rule==null)
			throw new IllegalStateException(
				"Trying to reduce without reduce rule");
		if ( state_stack.size()<=cur.reduce_rule.states_to_pop)
			throw new IllegalStateException(
			"Rule would pop more states than there are");
		Symbol result=cur.reduce_rule.result;
		if ( result==_complete)
			return true;
		int i;
		ParseState next=(ParseState)state_stack.get( state_stack.size()-1-cur.reduce_rule.states_to_pop);
		for ( i=0; i<next.goto_rules.length; i++)
			if ( next.goto_rules[i].looked_for==result)
				break;
		if ( i==next.goto_rules.length)
			throw new IllegalStateException(
				"Reduced symbol not found in goto list");
		
		if ( cur.reduce_rule.reduce_action!=null)
		{    
			Object to_push=cur.reduce_rule.reduce_action.ruleFire( this, getValueStack());
			for ( int j=0; j<cur.reduce_rule.states_to_pop; j++)
				value_stack.remove( value_stack.size()-1);
			value_stack.add( to_push);
		}              
		else
		{
			ArrayList to_push=new ArrayList( cur.reduce_rule.states_to_pop);
			for ( int j=0; j<cur.reduce_rule.states_to_pop; j++)
			{
				to_push.add( 0, value_stack.get( value_stack.size()-1));
				value_stack.remove( value_stack.size()-1);             
			}
			value_stack.add( to_push);
		}
		for ( int j=0; j<cur.reduce_rule.states_to_pop; j++)
			state_stack.remove( state_stack.size()-1);			
	
		state_stack.add( parse_states[next.goto_rules[i].state_index]);
		if ( state_stack.size()!=value_stack.size())
		{
			throw new IllegalStateException(
				"state_stack has different number of elements than value stack");
		}
	
		return false;
	}

	private String ruleMessage;
	private int recovery_count;
	private ParseState[] parse_states;
	private ArrayList value_stack;
	private ArrayList state_stack;
	private ArrayList next_value;
	private ParseState pushError()
	{
System.out.println( "pushError called");
		/* If in recovery state, throw away token
		 * silently */
		if ( recovery_count!=0 && next_value.size()>0)
		{
			/* If token is end token, can't throw it away.  If we are still
			 * in recovery mode, getting the end token as an error is bad.
			 * Force error handling.
			 */
			if ( next_value.get(0)==_end_)
				return null;
			recovery_count=1;
			next_value.clear();
System.out.println( "in recovery state-- throwing away token");
			return (ParseState)state_stack.get( state_stack.size()-1);
		}
		while( state_stack.size()>0)
		{
			ParseState cur=(ParseState)state_stack.get( state_stack.size()-1);
			
			int sri;
{
for ( int q=0; q<parse_states.length; q++)
{
if ( parse_states[q]==cur)
{
System.out.println( "Push error examining state "+Integer.toString( q));
break;
}
}
}           
			for ( sri=0; sri<cur.shift_rules.length; sri++)
				{
				if ( cur.shift_rules[sri].looked_for==Parser._error_)
					{
System.out.println( "Error shift found");

					if ( cur.shift_rules[sri].state_index<0)
						throw new IllegalStateException( "Error recovery would force error");
					recovery_count=1;
					cur=parse_states[cur.shift_rules[sri].state_index];
					state_stack.add( cur);
					if ( next_value.size()>0)
						value_stack.add( next_value.get( next_value.size()-1));
					else
						value_stack.add( new ArrayList());
					return cur;
					}        
				}
System.out.println( "Popping state off stack");               
			state_stack.remove( state_stack.size()-1);
			value_stack.remove( value_stack.size()-1);
		}

		// No recovery state found
System.out.println( "pushError returns null");			                                                   
		return null;			                                                 
	}
}

