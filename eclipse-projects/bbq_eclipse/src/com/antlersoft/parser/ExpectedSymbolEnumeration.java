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
import java.util.NoSuchElementException;

class ExpectedSymbolEnumeration implements Enumeration
{
	private ArrayList m_state_stack;
	private int m_shift_position;
	private Symbol m_next_symbol;
	private ParseState[] m_parse_states;

	ExpectedSymbolEnumeration( ParseState[] parse_states, ArrayList current_state_stack)
	{
		m_state_stack=(ArrayList)current_state_stack.clone();
		m_parse_states=parse_states;
		m_shift_position=0;
		m_next_symbol=null;
	}

    private void moveToNext()
	{
		while ( m_state_stack.size()>0)
		{
			ParseState state=(ParseState)m_state_stack.get( m_state_stack.size()-1);
			if ( state.shift_rules.length<=m_shift_position)
			{
				m_shift_position=0;
				if ( state.reduce_rule!=null)
				{
					int i;
					ParseState next=(ParseState)m_state_stack.get( m_state_stack.size()-1-state.reduce_rule.states_to_pop);
					for ( i=0; i<next.goto_rules.length; i++)
						if ( next.goto_rules[i].looked_for==state.reduce_rule.result)
							break;
					if ( i==next.goto_rules.length)
						throw new IllegalStateException(
							"Reduced symbol not found in goto list");
					for (int j = 0; j < state.reduce_rule.states_to_pop; j++)
						m_state_stack.remove(m_state_stack.size() - 1);

					m_state_stack.add(m_parse_states[next.goto_rules[i].state_index]);
				}
				else
				{
					m_state_stack.clear();
				}
			}
			else
			{
				m_next_symbol=state.shift_rules[m_shift_position++].looked_for;
				break;
			}
		}
    }

	public boolean hasMoreElements()
	{
		if ( m_next_symbol==null)
		{
			moveToNext();
		}
		return m_next_symbol!=null;
	}

    public Object nextElement()
	{
		if ( m_next_symbol==null)
		{
			moveToNext();
			if ( m_next_symbol==null)
				throw new NoSuchElementException();
		}
		Symbol result= m_next_symbol;
		m_next_symbol=null;
		return result;
    }
}