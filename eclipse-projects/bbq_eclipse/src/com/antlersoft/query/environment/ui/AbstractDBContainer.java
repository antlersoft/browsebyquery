/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.query.environment.ui;

import java.io.File;
import java.io.IOException;

import java.util.Properties;

import com.antlersoft.appcontext.AppContext;
import com.antlersoft.appcontext.ApplicationContext;
import com.antlersoft.appcontext.Starter;

import com.antlersoft.bbq.query.IDBSource;

import com.antlersoft.odb.ObjectStoreException;

import com.antlersoft.query.DataSource;

/**
 * Implementation of DBContainer where the datasource is an IDBSource, the file system
 * location is a directory containing the IndexObjectDB and an environment file.  There
 * is a key in the IndexObjectDB's root object containing an app-level db version.
 * 
 * @see com.antlersoft.bbq.query.IDBSource, com.antlersoft.odb.IndexObjectDB
 * @author Michael A. MacDonald
 *
 */
public abstract class AbstractDBContainer implements DBContainer {
	
	public static final String ENVIRONMENT_FILE_NAME="environment.xml";
	public static final String DATABASE_PROPERTY_KEY="database_path";
	public static final String DB_VERSION_KEY="db_version";
	public static final String USE_MAPPED_PROPERTY="com.antlersoft.query.environment.ui.AbstractDBContainer.USE_MAPPED";
	public static final String ANALYZER_THREADS_PROPERTY="com.antlersoft.query.environment.ui.AbstractDBContainer.ANALYZER_THREADS";
	
	/**
	 * Common resources in the containing application; may be null if this container
	 * was not initialized with a context.
	 */
	protected AppContext context;
	/**
	 * Directory containing currently open database
	 */
	protected File appDirectory;
	protected boolean cleared;
	/**
	 * Handle for currently open database
	 */
	protected IDBSource session;
	protected String title;
	protected String versionString;
	protected String configStreamKey;
	protected String openMessage;
	
	private boolean useMapped;
	private int analyzerThreadCount;
	
	/**
	 * Attempt to open a IndexObjectDB at a particular point in the file system;
	 * if the implementation throws an exception, the underlying database will be
	 * cleared (with clearDB()) and internalOpen will be called again.
	 * @param f Location in the file system to open the database
	 * @return IDBSource referencing IndexObjectDB opened at f
	 * @throws Exception Throw when there was a problem opening the database
	 */
	abstract protected IDBSource internalOpen( File f) throws Exception;
	
	private void checkPathString( String path_string)
	{
		if ( path_string!=null)
		{
			appDirectory=new File(path_string);
			if ( ! ((appDirectory.exists() && appDirectory.isDirectory()) || ( ! appDirectory.exists() && appDirectory.getParentFile().canWrite())))
				appDirectory=null;
		}
	}
	
	protected AbstractDBContainer( String version)
	{
		versionString=version;
		title="Default";
		session=null;
		appDirectory=null;
		useMapped = false;
		analyzerThreadCount = 1;
	}
	
	public static AppContext createContextWithLegacyCommandLine( String argv[])
	{
		ApplicationContext ac=new ApplicationContext(argv);
		if ( ac.getParameter(DATABASE_PROPERTY_KEY)==null && argv.length>0 &&
				! argv[argv.length-1].contains("="))
			ac=new ApplicationContext( argv[argv.length-1], argv);
		return ac;
	}
	
	public void initializeFromContext( AppContext context, String default_path, String configStreamKey)
	throws Exception
	{
		if ( appDirectory!=null)
			throw new IllegalStateException( "Can't call initializeFromContext after a database has been opened");
		this.context=context;
		this.configStreamKey=configStreamKey;
		
		// First check provided on command line
		checkPathString( context.getParameter( DATABASE_PROPERTY_KEY));
		
		if ( appDirectory==null)
			checkPathString( context.getParameter("app_class"));
		
		// Check in config file
		if ( appDirectory==null)
		{
			try
			{
				checkPathString(Starter.getProperties(context,configStreamKey).getProperty( DATABASE_PROPERTY_KEY));
			}
			catch ( IOException ioe)
			{
				
			}
		}
		if ( appDirectory==null)
		{
			checkPathString( default_path);
			// Only use default path if it already exists; for legacy compatibility
			if ( appDirectory!=null && ! appDirectory.exists())
			{
				appDirectory=null;
			}
		}
		
		if ( appDirectory!=null)
		{
			// Only set appDirectory on successful open
			File p=appDirectory;
			appDirectory=null;
			openDB( p);
		}
		else
		{
			openMessage = "Please select a directory to hold the BBQ database";
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#analyze(java.io.File[])
	 */
	public void analyze(File[] selected_files) throws Exception {
		cleared=false;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#clearDB()
	 */
	public void clearDB() throws Exception {
		try
		{
			closeDB();
		}
		catch ( Exception e)
		{
			// Ignore exceptions closing the database
		}
		cleared=true;
		if ( appDirectory!=null && appDirectory.exists())
		{
            File[] children=appDirectory.listFiles();
            for ( int i=0; i<children.length; ++i)
            {
            	if ( children[i].isFile() && children[i].getName().indexOf('.')== -1)
            		if ( ! children[i].delete())
            			throw new ObjectStoreException("Failed to delete file: "+children[i].getName());
            }
		}
		session=internalOpen(appDirectory);
		session.getSession().makeRootObject(DB_VERSION_KEY, versionString);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#closeDB()
	 */
	public void closeDB() throws Exception {
		if ( session!=null)
		{
			session.getSession().close();
			session=null;
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#getDataSource()
	 */
	public DataSource getDataSource() {
		return session;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#getEnvironmentFile()
	 */
	public File getEnvironmentFile() {
		if ( appDirectory==null )
			return null;
		return new File(appDirectory,ENVIRONMENT_FILE_NAME);
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#getOpenMessage()
	 */
	public String getOpenMessage() {
		return openMessage;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#isCleared()
	 */
	public boolean isCleared() throws Exception {
		return cleared;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#openDB(java.io.File)
	 */
	public void openDB(File location) throws Exception {
		closeDB();
		openMessage=null;
		appDirectory=location;
		boolean existing=( location.exists() && new File(location,"overhead").exists()); 
		try
		{
			session=internalOpen(location);
			if ( ! existing)
				session.getSession().makeRootObject(DB_VERSION_KEY, versionString);
		}
		catch ( Exception e)
		{
			cleared=true;
			clearDB();
			openMessage="There was a problem opening the existing database, so it was cleared,\nand you must rebuild it by analyzing classes.";
		}
		String v=(String)session.getSession().getRootObject(DB_VERSION_KEY);
		if ( v==null || (v!=null && ! v.equals(versionString)))
		{
			if (v==null)
				v="(no version)";
			cleared=true;
			clearDB();
			openMessage="The existing database was for a different version of BBQ, so it was cleared,\nand you must rebuild it by analyzing classes."+v;
		}
		if ( context!=null )
		{
			try
			{
				Properties p=Starter.getProperties(context, configStreamKey);
				p.setProperty( DATABASE_PROPERTY_KEY, location.getCanonicalPath());
				Starter.saveProperties(context, configStreamKey, p);
			}
			catch ( IOException ioe)
			{
				// Not a problem not to save
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#saveDB()
	 */
	public void saveDB() throws Exception {
		session.getSession().commitAndRetain();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.DBContainer#titleOfDB()
	 */
	public String titleOfDB() {
		if ( appDirectory!=null)
		{
			return appDirectory.getName();
		}
		return "";
	}

	/**
	 * @return the useMapped
	 */
	public boolean isUseMapped() {
		return useMapped;
	}

	/**
	 * If set to true, will make a best effort to use memory-mapped files for the database.
	 * <p>
	 * Changing the value when a database is open will cause the database to be re-opened.
	 * 
	 * @param useMapped the useMapped to set
	 */
	public void setUseMapped(boolean useMapped)
		throws Exception
	{
		if (this.useMapped != useMapped)
		{
			boolean reopen = (session != null);
			if (reopen)
			{
				closeDB();
			}
			this.useMapped = useMapped;
			if (reopen)
			{
				openDB(appDirectory);
			}
		}
	}

	/**
	 * @return the analyzerThreadCount
	 */
	public int getAnalyzerThreadCount() {
		return analyzerThreadCount;
	}

	/**
	 * @param analyzerThreadCount the analyzerThreadCount to set
	 */
	public void setAnalyzerThreadCount(int analyzerThreadCount) {
		this.analyzerThreadCount = analyzerThreadCount;
	}
	
	public void updateDBParamsFromProperties(Properties p)
		throws Exception
	{
		if (p.containsKey(USE_MAPPED_PROPERTY))
		{
			setUseMapped(Boolean.valueOf(p.getProperty(USE_MAPPED_PROPERTY)));
		}
		if (p.containsKey(ANALYZER_THREADS_PROPERTY))
		{
			setAnalyzerThreadCount(Integer.valueOf(p.getProperty(ANALYZER_THREADS_PROPERTY)));
		}
	}
	
	public void updatePropertiesWithDBParams(Properties p)
	{
		p.setProperty(USE_MAPPED_PROPERTY, Boolean.toString(isUseMapped()));
		p.setProperty(ANALYZER_THREADS_PROPERTY, Integer.toString(getAnalyzerThreadCount()));
	}
}
