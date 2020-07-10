/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.antlersoft.bbq.query.IDBSource;
import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.LoggingDBDriver;
import com.antlersoft.ilanalyze.db.ILDB;
import com.antlersoft.ilanalyze.db.ILDBDriver;
import com.antlersoft.ilanalyze.parseildasm.IldasmReader;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.query.environment.ui.AbstractDBContainer;

/**
 * @author Michael A. MacDonald
 *
 */
public class ILDBContainer extends AbstractDBContainer {
    static Logger logger=Logger.getLogger( ILDBContainer.class.getName());

    private ILDB analyzerDB;
	
	/** A format string for forming the disassembly command for an assembly from the assembly's path */
	private String m_disassembly_command_format;
	
	/** Pattern directory must match to pull .dll's and .exe's */
	private Pattern m_directory_match;
	/** Pattern directory might match if there is a Release build but no debug build */
	private Pattern m_release_directory_match;

	/** Oldest modification date to consider for file to add to database */
	private TreeMap<String,Date> m_oldest;
	
	/** Thread pool for analyzing files concurrently */
	private ThreadPoolExecutor m_thread_pool;
	
	/** Results of tasks submitted on the thread pool */
	private ArrayList<Future<FileReadRunnable>> m_result_list;

	private String m_path_to_ildasm;

	private String m_path_to_resource_lister;
	
	public ILDBContainer()
	{
		super("ILDB021");
		m_disassembly_command_format="monodis {0}";
		// Default to assuming it is in system path
		m_path_to_ildasm = "ildasm";
		m_path_to_resource_lister = "ResourceLister";
		if ( System.getProperty("os.name", "Finux").contains("Windows"))
		{
			m_disassembly_command_format="{1} /text /nobar /linenumber \"{0}\"";
			try
			{
				m_directory_match=Pattern.compile( "obj\\\\Debug(\\\\[^\\\\]*)?\\z");
				m_release_directory_match=Pattern.compile( "obj\\\\Release(\\\\[^\\\\]*)?\\z");
			}
			catch ( PatternSyntaxException pse)
			{
				logger.warning(pse.getLocalizedMessage());
			}
		}
	}

	public void setDisassemblyCommandFormat(String format) {
		m_disassembly_command_format = format;
	}

	public String getDisassemblyCommandFormat() {
		return m_disassembly_command_format;
	}

	public void setPathToIldasm(String path) {
		m_path_to_ildasm = path;
	}

	public String getPathToIldasm() {
		return m_path_to_ildasm;
	}

	public void setPathToResourceLister(String path) {
		m_path_to_resource_lister = path;
	}

	public String getPathToResourceLister() {
		return m_path_to_resource_lister;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.AbstractDBContainer#internalOpen(java.io.File)
	 */
	@Override
	protected IDBSource internalOpen(File f) throws Exception {
		analyzerDB=new ILDB(f, isUseMapped());
		return analyzerDB;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.AbstractDBContainer#analyze(java.io.File[])
	 */
	@Override
	public void analyze(File[] selectedFiles) throws Exception {
	   	IldasmReader reader=new IldasmReader();
	   	Object last_scanned=analyzerDB.getRootObject("LAST_SCANNED");
	   	if ( last_scanned==null || ! (last_scanned instanceof TreeMap))
	   		m_oldest=new TreeMap<String,Date>();
	   	else
	   		m_oldest=(TreeMap<String,Date>)last_scanned;

		for ( int i=0; i<selectedFiles.length; i++)
		{
		  sendFileToDB(selectedFiles[i], analyzerDB, getAnalyzerThreadCount());
		}
		
		if (getAnalyzerThreadCount() > 1)
			waitForFileReads();
       
		analyzerDB.makeRootObject("LAST_SCANNED", m_oldest);
		analyzerDB.commitAndRetain();
	}	

	public void sendFileToDB( File file, final ILDB db, int threads) throws IOException, RuleActionException
	{
		DBDriverSource driverSource;
		if (threads > 1)
		{
			driverSource = new DBDriverSource() {

				/* (non-Javadoc)
				 * @see com.antlersoft.ilanalyze.parseildasm.DBDriverSource#get()
				 */
				@Override
				public DBDriver getDBDriver() {
					return new ILDBDriver(db);
				}

				/* (non-Javadoc)
				 * @see com.antlersoft.ilanalyze.ui.DBDriverSource#getReader()
				 */
				@Override
				public IldasmReader getReader() {
					return new IldasmReader();
				}				
			};
		}
		else
		{
			driverSource = new SimpleDBDriverSource(new ILDBDriver(db), new IldasmReader());
		}
		sendFileToDriver( file, driverSource, threads, false);
	}
	
	void readIlFile(File file, IldasmReader reader, DBDriver driver)
		throws IOException, RuleActionException
	{
		logger.fine( "Reading .il file "+file.getAbsolutePath());
		driver.startAnalyzedFile(file.getAbsolutePath());
		Reader r = new BufferedReader(new FileReader(file));
		try
		{
			reader.Read( driver, r);
		}
		finally
		{
			r.close();
		}
		driver.endAnalyzedFile();		
	}
	
	void readAssemblyFile(File file, IldasmReader ild, DBDriver driver)
		throws IOException, RuleActionException
	{
		logger.fine("Reading assembly file "+file.getAbsolutePath());
		driver.startAnalyzedFile(file.getAbsolutePath());
		// Tokenize the command format, substitute the file path as necessary
		Object[] format_args=new Object[] { file.getAbsolutePath(), m_path_to_ildasm, m_path_to_resource_lister };
		Process process=Runtime.getRuntime().exec(formatCommandArgs(m_disassembly_command_format, format_args));
		Reader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		try
		{
			ild.Read( driver, reader);
			try
			{
				process.waitFor();
			}
			catch ( InterruptedException ie)
			{
				logger.warning( "Interrupted waiting for reader process: " + ie.getMessage());
			}
		}
		finally
		{
			reader.close();
		}
		try
		{
			process=Runtime.getRuntime().exec(formatCommandArgs("{2} {0}", format_args), null, null);
			reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			try
			{
				ild.readResources( driver, reader);
				try
				{
					process.waitFor();
				}
				catch ( InterruptedException ie)
				{
					logger.warning( "Interrupted waiting for resource reader process: " + ie.getMessage());
				}
			}
			finally
			{
				reader.close();						
			}
		}
		catch ( Exception e)
		{
			logger.log( Level.INFO, "Problem running ResourceLister on "+format_args[0], e);
		}
		driver.endAnalyzedFile();		
	}
	
	abstract class FileReadRunnable implements Callable<FileReadRunnable>
	{
		private File m_file;
		private IldasmReader m_reader;
		private DBDriver m_driver;
		
		IOException captureIOException;
		RuleActionException captureRuleActionException;
		
		FileReadRunnable(File file, IldasmReader reader, DBDriver driver)
		{
			m_file = file;
			m_reader = reader;
			m_driver = driver;
		}
		
		abstract void read(File file, IldasmReader reader, DBDriver driver) throws IOException, RuleActionException;
		
		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public FileReadRunnable call() throws Exception {
			try
			{
				read(m_file, m_reader, m_driver);
			}
			catch (IOException ioe)
			{
				captureIOException = ioe;
			}
			catch (RuleActionException rae)
			{
				captureRuleActionException = rae;
			}
			return this;
		}
	}
	
	private synchronized void submitFileRead(int threads, FileReadRunnable fileRead)
	{
		if (m_thread_pool == null)
		{
			m_thread_pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(threads);
			m_result_list = new ArrayList<Future<FileReadRunnable>>();
		}
		else
		{
			m_thread_pool.setMaximumPoolSize(threads);
		}
		m_result_list.add(m_thread_pool.submit(fileRead));
	}
	
	public synchronized void waitForFileReads()
		throws IOException, RuleActionException
	{
		while (m_result_list != null && m_result_list.size() > 0)
		{
			Future<FileReadRunnable> future = m_result_list.get(0);
			m_result_list.remove(0);
			FileReadRunnable r;
			try {
				r = future.get();
			} catch (InterruptedException e) {
				throw new RuleActionException("Interrupted waiting for analyzer thread", e);
			} catch (ExecutionException e) {
				throw new RuleActionException("Uncaught error in analyzer thread", e);
			}
			if (r.captureIOException != null)
				throw r.captureIOException;
			if (r.captureRuleActionException != null)
				throw r.captureRuleActionException;
		}
	}
	
	private void sendFileToDriver( File file, DBDriverSource driverSource, int threads, boolean filter) throws IOException, RuleActionException
	{
		if ( file.isDirectory())
		{
			File[] files=file.listFiles();
			for ( int i=0; i<files.length; ++i)
			{
				sendFileToDriver( files[i], driverSource, threads, true);
			}
		}
		else
		{
			String lower_file=file.getName().toLowerCase();
			if ( lower_file.endsWith(".il"))
			{
				if (threads > 1)
				{
					submitFileRead(threads, new FileReadRunnable(file, driverSource.getReader(), driverSource.getDBDriver()) {
						void read(File file, IldasmReader reader, DBDriver driver) throws IOException, RuleActionException
						{
							readIlFile(file, reader, driver);
						}
					});
				}
				else
				{
					readIlFile(file, driverSource.getReader(), driverSource.getDBDriver());
				}
			}
			else if ( lower_file.endsWith(".dll") || lower_file.endsWith(".exe"))
			{
				if ( filter)
				{
					if ( m_directory_match!=null && m_release_directory_match!=null)
					{
						String dirName = file.getParentFile().getAbsolutePath();
						if ( ! m_directory_match.matcher(dirName).find())
						{
							boolean useableRelease = false;
							if (m_release_directory_match.matcher(dirName).find()) {
								String filePath = file.getAbsolutePath();
								int releaseIndex = filePath.lastIndexOf("Release");
								if (releaseIndex > 0) {
									String debugPath = filePath.substring(0, releaseIndex) + "Debug" + filePath.substring(releaseIndex+7);
									File debugFile = new File(debugPath);
									if (! debugFile.exists() || debugFile.lastModified() < file.lastModified()) {
										useableRelease = true;
									}
								}
							}
							if (! useableRelease) {
								if (logger.isLoggable(Level.FINER))
									logger.finer("Rejecting " + file.getAbsolutePath() + " because directory doesn't match " + m_directory_match.pattern());
								return;
							}
						}
					}
					if ( m_oldest!=null )
					{
						Date d=m_oldest.get( file.getCanonicalPath());
						if ( d!=null && file.lastModified()<=d.getTime())
						{
							if ( logger.isLoggable(Level.FINER))
								logger.finer( "Rejecting "+file.getAbsolutePath()+" because it is older than "+m_oldest.toString() );
							return;						
						}
						m_oldest.put(file.getCanonicalPath(), new Date(file.lastModified()));
					}
				}
				if (threads > 1)
				{
					submitFileRead(threads, new FileReadRunnable(file, driverSource.getReader(), driverSource.getDBDriver()) {

						@Override
						void read(File file, IldasmReader reader, DBDriver driver)
								throws IOException, RuleActionException {
							readAssemblyFile(file, reader, driver);
						}
					});
				}
				else
					readAssemblyFile(file, driverSource.getReader(), driverSource.getDBDriver());
			}
		}
	}
	
	private static String[] formatCommandArgs( String commandFormat, Object[] format_args)
	{
		ArrayList<String> arg_list=new ArrayList<String>();
		for ( Enumeration e=new StringTokenizer( commandFormat); e.hasMoreElements();)
		{
			arg_list.add( MessageFormat.format( (String)e.nextElement(), format_args));
		}
		String[] cmdargs=new String[arg_list.size()];
		return arg_list.toArray(cmdargs);
	}
	
	public static void main( String[] args) throws Exception
	{
	    if (args.length == 2)
		{
			com.antlersoft.ilanalyze.db.ILDB db=new com.antlersoft.ilanalyze.db.ILDB(new File(args[1]), false);
			try
			{
				new ILDBContainer().sendFileToDriver( new File(args[0]), new SimpleDBDriverSource( 
						new LoggingDBDriver( new com.antlersoft.ilanalyze.db.ILDBDriver( db)), new IldasmReader()), 1, false);
			}
			finally
			{
				db.close();
			}
		}
		else if (args.length == 1)
		{
			com.antlersoft.ilanalyze.db.ILDB db=new com.antlersoft.ilanalyze.db.ILDB(new File(args[0]), false);
			try
			{
				BufferedReader in=new BufferedReader( new InputStreamReader( System.in));
				ILDBContainer dbContainer = new ILDBContainer();
				SimpleDBDriverSource source = new SimpleDBDriverSource(
					new LoggingDBDriver(
						new com.antlersoft.ilanalyze.db.ILDBDriver( db)),
					new IldasmReader());
				for (String line=in.readLine(); line!=null && line.length() > 0; line=in.readLine())
				{
				    dbContainer.sendFileToDriver( new File(line), source, 1, false);
			    }
			}
			finally
			{
				db.close();
			}
		}
	}
}
