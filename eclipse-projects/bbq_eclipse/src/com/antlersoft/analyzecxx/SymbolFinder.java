package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;

/**
 * This class traverses a SymbolFinderTree one character at a time
 * to (efficiently?) identify a symbol
 */
public class SymbolFinder
{
	private SymbolFinderTree m_tree;
	private SymbolFinderTree.Node m_node;
	private Symbol m_last_symbol;
	private StringBuffer m_remainder;

	public SymbolFinder( SymbolFinderTree tree)
	{
		m_tree=tree;
		m_remainder=new StringBuffer( 20);
		reset();
	}

	/**
	 * See if the next character can move you to a new node in the tree.  If
	 * the new node matches a symbol, the function returns true and the
	 * remainder is cleared.  If the new node does not match a symbol,
	 * the function returns true and the character is added to the remainder.
	 * If no new node is available, the function returns false and
	 * the character is added to the remainder.
	 */
	public final boolean accept( char c)
	{
		SymbolFinderTree.Node new_node=( m_node==null ? null : m_node.nextNode( c));
		if ( new_node==null)
		{
			m_node=null;
			m_remainder.append( c);
			return false;
		}
		else
		{
			m_node=new_node;
			if ( new_node.m_symbol!=null)
			{
				m_last_symbol=new_node.m_symbol;
				m_remainder.setLength( 0);
			}
			else
			{
				m_remainder.append( c);
			}
		}
		return true;
	}

	/**
	 * Return false if the current node can not accept any other characters
	 */
	public final boolean canGrow()
	{
		return m_node.canGrow();
	}

	/**
	 * Returns the last recognized symbol
	 */
	public final Symbol currentSymbol()
	{
		return m_last_symbol;
	}

	/**
	 * Returns the text associated with currentSymbol
	 */
	String currentText()
	{
		return m_last_symbol.toString();
	}

	/**
	 * Resets to the root of the tree
	 */
	public final void reset()
	{
		m_last_symbol=null;
		m_node=m_tree.m_root;
		m_remainder.setLength( 0);
	}

	/**
	 * Returns the characters that have been accepted since the
	 * last recognized symbol
	 */
	public final String getRemainder()
	{
		return m_remainder.toString();
	}

	/**
	 * Return true if there is a remainder
	 */
	public final boolean isRemainder()
	{
		return m_remainder.length()>0;
	}
}