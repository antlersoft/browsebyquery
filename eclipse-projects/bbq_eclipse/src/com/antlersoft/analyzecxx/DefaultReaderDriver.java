package com.antlersoft.analyzecxx;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;
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

	/**
	 * Entry on stack of currently open files (top level is translation
	 * unit, others are nested include files)
	 */
	static class ReaderFile
	{
		File m_file;
		InputStream m_stream;
	}

	public DefaultReaderDriver( File translation_unit, Collection include_paths,
						 Properties initial_defines)
	{

	}

	/* Implementation of ReaderDriver interface */

    public void includeFile(CxxReader reader, String file, boolean search_current_directory, int next_line_in_this_file) throws IOException {
	/**@todo Implement this com.antlersoft.analyzecxx.ReaderDriver method*/
	throw new java.lang.UnsupportedOperationException("Method includeFile() not yet implemented.");
    }
    public void popIncludeFile(CxxReader reader) {
	/**@todo Implement this com.antlersoft.analyzecxx.ReaderDriver method*/
	throw new java.lang.UnsupportedOperationException("Method popIncludeFile() not yet implemented.");
    }
}