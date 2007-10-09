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
package com.antlersoft.parser.lex;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.antlersoft.parser.Symbol;

public class SymbolFinderTree
{
	static class Node
	{
		Symbol m_symbol;
		Link[] m_links;

		final boolean canGrow()
		{
			return m_links.length>0;
		}

		final Node nextNode( char c)
		{
			for ( int i=0; i<m_links.length; ++i)
			{
				if ( c==m_links[i].m_match)
					return m_links[i].m_next;
				if ( c<m_links[i].m_match)
					break;
			}
			return null;
		}
	}

	static class BuildNode
	{
		Symbol m_symbol;
		TreeMap m_tree;

		BuildNode()
		{
			m_tree=new TreeMap();
		}

		Node makeNode()
		{
			Node result=new Node();
			result.m_symbol=m_symbol;
			result.m_links=new Link[m_tree.size()];
			int i=0;
			for ( Iterator it=m_tree.entrySet().iterator(); it.hasNext(); ++i)
			{
				Map.Entry entry=(Map.Entry)it.next();
				result.m_links[i]=new Link();
				result.m_links[i].m_match=((Character)entry.getKey()).charValue();
				result.m_links[i].m_next=((BuildNode)entry.getValue()).makeNode();
			}
			return result;
		}
	}

	static class Link
	{
		char m_match;
		Node m_next;
	}

	Node m_root;

	public SymbolFinderTree( String[] to_find)
	{
		BuildNode root=new BuildNode();
		for ( int i=0; i<to_find.length; ++i)
		{
			String s=to_find[i];
			int l=s.length();
			BuildNode current=root;
			for ( int j=0; j<l; ++j)
			{
				Character c=new Character( s.charAt( j));
				BuildNode next=(BuildNode)current.m_tree.get( c);
				if ( next==null)
				{
					next=new BuildNode();
					current.m_tree.put( c, next);
				}
				current=next;
			}
			current.m_symbol=Symbol.get( s);
			if ( current.m_symbol.toString()==null)
				break;
		}
		m_root=root.makeNode();
	}
}