package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

	public DefaultReaderDriver( Collection include_paths,
						 Properties initial_defines)
	{
		m_include_paths=new ArrayList( include_paths);
		m_initial_defines=initial_defines;
		m_include_stack=new Stack();
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
	}

	public void analyze( File translation_unit)
	throws IOException, RuleActionException, LexException
	{
		m_include_stack.clear();
		ReaderFile reader_file=new ReaderFile();
		reader_file.m_file=translation_unit;
		reader_file.m_line=1;
		reader_file.m_stream=new BufferedInputStream( new FileInputStream( translation_unit));
		m_include_stack.push( reader_file);
		CxxReader reader=new CxxReader( this, translation_unit.getCanonicalPath(),
										m_initial_defines);
		while ( ! m_include_stack.isEmpty())
		{
			ReaderFile top=(ReaderFile)m_include_stack.peek();
			int c=top.m_stream.read();
			if ( c== -1)
				reader.endOfFile();
			else
				reader.nextCharacter( (char)c);
		}
	}

	/* Implementation of ReaderDriver interface */

    public void includeFile(CxxReader reader, String file, boolean search_current_directory, int next_line_in_this_file) throws IOException {
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
				reader.m_file = reader_file.m_file.getCanonicalPath();
				reader.m_line = reader_file.m_line;
			}
		}
		catch ( IOException ioe)
		{
			System.err.println( "Error popping include file stack: filenames will be wrong");
		}
    }

	public void dontRepeat( String file)
	{
		m_no_repeat_files.add( file);
	}

	public static void main( String args[])
	throws Exception
	{
		DefaultReaderDriver drd=new DefaultReaderDriver( new ArrayList(), new Properties());
		drd.analyze( new File( args[0]));
	}
}