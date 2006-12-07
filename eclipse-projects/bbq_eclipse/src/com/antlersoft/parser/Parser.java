/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.parser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public abstract class Parser
{
    // Public interface
	public static int max_recovery=3;
	public static Symbol _end_=Symbol.get( "_end");
	public static Symbol _error_=Symbol.get( "_error_");

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

	public Enumeration getExpectedSymbols()
	{
		return new ExpectedSymbolEnumeration( parse_states, state_stack);
	}

	// Accessor methods for creating parser generators
	public ParseState[] getParseStates()
	{
		return parse_states;
	}
	
	/**
	 * Return some object that contains the parser;
	 * the rules call this method to get access to their environment.
	 * This is better design than the old way of
	 * sub-classing the parser and casting it, although the default
	 * implementation, which returns the this pointer,
	 * allows for such an implementation.
	 * @return Some object that contains the parser and provides details of the execution environment
	 */
	public Object getParserEnvironment()
	{
		return this;
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
			state_stack.remove(state_stack.size() - 1);

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
			return (ParseState)state_stack.get( state_stack.size()-1);
		}
		while( state_stack.size()>0)
		{
			ParseState cur=(ParseState)state_stack.get( state_stack.size()-1);

			int sri;
			for ( sri=0; sri<cur.shift_rules.length; sri++)
				{
				if ( cur.shift_rules[sri].looked_for==Parser._error_)
					{

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
			state_stack.remove( state_stack.size()-1);
			value_stack.remove( value_stack.size()-1);
		}

		// No recovery state found
		return null;
	}
}

