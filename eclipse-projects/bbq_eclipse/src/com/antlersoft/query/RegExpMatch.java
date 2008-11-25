/**
 * Copyright (c) 2007 Michael A. MacDonald
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
package com.antlersoft.query;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.antlersoft.parser.RuleActionException;

/**
 * Acts like the 'matches' operator, except matches on regular expressions.
 * @author Michael A. MacDonald
 *
 */
public class RegExpMatch extends CountPreservingBoundFilter {

	/**
	 * @param to_match Regular expression to match
	 * @throws RuleActionException for an invalid regular expression, with the message from the
	 * PatternSyntaxException
	 */
	public RegExpMatch( String to_match)
	throws RuleActionException
	{
		super(null);
		try
		{
			m_pattern=Pattern.compile(to_match);
		}
		catch ( PatternSyntaxException pse)
		{
			RuleActionException rae=new RuleActionException( pse.getMessage(), pse);
			throw rae;
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.CountPreservingFilter#getCountPreservingFilterValue(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	protected boolean getCountPreservingFilterValue(DataSource source,
			Object inputObject) {
		return m_pattern.matcher(inputObject.toString()).find();
	}

	
	private Pattern m_pattern;
}
