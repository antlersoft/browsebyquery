/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbqweb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		Object o=arg0.getServletContext().getAttribute( "com.antlersoft.bbqweb.DBBean");
		if ( o!=null && o instanceof DBBean)
		{
			((DBBean)o).destroy();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		arg0.getServletContext().setAttribute( "com.antlersoft.bbqweb.DBBean", new DBBean());
	}

}
