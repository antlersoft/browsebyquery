
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
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
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb;

public class ObjectDBException extends RuntimeException
{
    public ObjectDBException()
    {
	super();
	underlying=null;
    }

    public ObjectDBException( String msg)
    {
	super( msg);
	underlying=null;
    }

    public ObjectDBException( String msg, Exception cause)
    {
	super( msg+":"+cause.getMessage());
	underlying=cause;
    }

    public void printStackTrace( java.io.PrintWriter pw)
    {
        super.printStackTrace( pw);
        if ( underlying!=null)
            underlying.printStackTrace( pw);
    }

    public void printStackTrace( java.io.PrintStream ps)
    {
        printStackTrace( new java.io.PrintWriter( ps));
    }

    public Exception getUnderlying()
    {
	return underlying;
    }

    private Exception underlying;
}