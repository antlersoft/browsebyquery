package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;

/**
 * Straightforward implementation of a ReaderDriver; given a file
 * and a set of include paths sends file to CxxReader as a translation
 * unit
 */
public class DefaultReaderDriver implements ReaderDriver
{
	private Stack m_include_stack;
	private ArrayList m_include_paths;
	private Properties m_initial_defines;
	private HashSet m_no_repeat_files;
	private CxxReader m_reader;

	/**
	 * Entry on stack of currently open files (top level is translation
	 * unit, others are nested include files)
	 */
	static class ReaderFile
	{
		File m_file;
		InputStream m_stream;
		int m_line;
	}

	public DefaultReaderDriver( DBDriver db, Collection include_paths,
						 Properties initial_defines)
	{
		m_include_paths=new ArrayList( include_paths);
		m_initial_defines=initial_defines;
		m_include_stack=new Stack();
		m_no_repeat_files=new HashSet();
		m_reader=new CxxReader( this, db, initial_defines);
	}

	public void analyze( File translation_unit)
	throws IOException, RuleActionException, LexException
	{
		m_include_stack.clear();
		m_no_repeat_files.clear();
		ReaderFile reader_file=new ReaderFile();
		reader_file.m_file=translation_unit;
		reader_file.m_line=1;
		reader_file.m_stream=new BufferedInputStream( new FileInputStream( translation_unit));
		m_include_stack.push( reader_file);
		m_reader.startTranslationUnit( translation_unit.getCanonicalPath());
		while ( ! m_include_stack.isEmpty())
		{
			ReaderFile top=(ReaderFile)m_include_stack.peek();
			int c=top.m_stream.read();
			if ( c== -1)
				m_reader.endOfFile();
			else
				m_reader.nextCharacter( (char)c);
		}
		m_reader.finishTranslationUnit();
	}

	private boolean tryIncludeFile( CxxReader reader, File parent, String child, int next_line)
	throws IOException
	{
		File f=new File( parent, child);
		if ( f.canRead())
		{
			String canon=f.getCanonicalPath();
			if ( ! m_no_repeat_files.contains( canon))
			{
				ReaderFile rf=new ReaderFile();
				rf.m_file=f;
				rf.m_stream=new BufferedInputStream( new FileInputStream( f));
				((ReaderFile)m_include_stack.peek()).m_line=next_line+1;
				m_include_stack.push( rf);
				reader.setFileAndLine( canon, 0);
			}
			return true;
		}
		return false;
	}
	/* Implementation of ReaderDriver interface */

    public void includeFile(CxxReader reader, String file, boolean search_current_directory, int next_line_in_this_file) throws IOException {
		if ( search_current_directory)
		{
			File parent=((ReaderFile)m_include_stack.peek()).m_file.getParentFile();
			if ( tryIncludeFile( reader, parent, file, next_line_in_this_file))
				return;
		}
		for ( Iterator i=m_include_paths.iterator(); i.hasNext(); )
		{
			if ( tryIncludeFile( reader, (File)i.next(), file, next_line_in_this_file))
				return;
		}
		throw new FileNotFoundException( file);
    }
    public void popIncludeFile(CxxReader reader)
	{
		ReaderFile reader_file=(ReaderFile)m_include_stack.pop();
		try
		{
			reader_file.m_stream.close();
			if ( ! m_include_stack.isEmpty())
			{
				reader_file = (ReaderFile) m_include_stack.peek();
				m_reader.setFileAndLine( reader_file.m_file.getCanonicalPath(),
										 reader_file.m_line);
			}
		}
		catch ( IOException ioe)
		{
			System.err.println( "Error popping include file stack: filenames will be wrong");
		}
    }

	public void dontRepeat( CxxReader reader)
	{
		try
		{
			m_no_repeat_files.add( ( (ReaderFile) m_include_stack.peek()).
								  m_file.getCanonicalPath());
		}
		catch ( IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public static void main( String args[])
	throws Exception
	{
		Properties initial_defines=new Properties();
		ArrayList includes=new ArrayList();
		includes.add( new File( "/usr/include"));
		includes.add( new File( "/usr/include/g++-3"));
		includes.add( new File( "/usr/include/linux"));
		includes.add( new File( "/usr/lib/gcc-lib/i386-slackware-linux/2.95.3/include"));
		includes.add( new File( "/home/mike/src/cppquery"));
		/*
		#define __linux__ 1
		#define linux 1
		#define __i386__ 1
		#define __i386 1
		#define i386 1
		#define __unix 1
		#define __unix__ 1
		#define __linux 1
		#define __ELF__ 1
		#define unix 1
		*/
	   //initial_defines.setProperty( "__STDC__", "1");
	   initial_defines.setProperty( "__linux__", "1");
	   initial_defines.setProperty( "linux", "1");
	   initial_defines.setProperty( "__i386__", "1");
	   initial_defines.setProperty( "__i386", "1");
	   initial_defines.setProperty( "i386", "1");
	   initial_defines.setProperty( "__unix", "1");
	   initial_defines.setProperty( "__unix__", "1");
	   initial_defines.setProperty( "__linux", "1");
	   initial_defines.setProperty( "__ELF__", "1");
	   initial_defines.setProperty( "unix", "1");
	   DBDriver db=new com.antlersoft.analyzecxx.db.CxxIndexObjectDB( args[1]);
		DefaultReaderDriver drd=new DefaultReaderDriver( db, includes, initial_defines);
		try
		{
			drd.analyze(new File(args[0]));
		}
		catch ( RuleActionException rae)
		{
System.err.println( rae.getMessage());
rae.printStackTrace();
		}
	}
}