/*
 * <p>Copyright (c) 2003-2004  Michael A. MacDonald<p>
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
package com.antlersoft.appcontext;

class Starter
{
	static void start( AppContext context)
		throws Exception
	{
		String app_class=context.getParameter( "app_class");
		if ( app_class==null)
			return;
		String app_start=context.getParameter( "app_start");
		if ( app_start==null)
			app_start="ApplicationMain";
		ClassLoader loader=context.getClass().getClassLoader();
		Class[] arg_list=new Class[] { loader.loadClass( "com.antlersoft.appcontext.AppContext") };
		Class start_class=loader.loadClass( app_class);
		Object[] args=new Object[] { context };
		start_class.getMethod( app_start, arg_list).invoke( null, args);
	}
}
