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
