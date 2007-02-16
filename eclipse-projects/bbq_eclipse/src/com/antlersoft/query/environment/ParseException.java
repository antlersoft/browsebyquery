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
package com.antlersoft.query.environment;

public class ParseException extends QueryException
{
    static String parseMessage( QueryLanguageEnvironment qp)
    {
        StringBuffer retval=new StringBuffer();

        if ( qp.getParser().getRuleMessage()!=null)
        {
                retval.append( qp.getParser().getRuleMessage());
                retval.append( ": ");
        }

        int i;
        for ( i=0; i<qp.tokens.length; i++)
        {
            if ( i==qp.currentIndex)
                retval.append( "<<here>>");
            retval.append( qp.tokens[i].toString());
            retval.append( ' ');
        }
        if ( i==qp.currentIndex)
            retval.append( "<<here>>");
        return retval.toString();
    }

    ParseException( QueryLanguageEnvironment qp)
    {
        super( parseMessage( qp));
    }
}
