package com.antlersoft.analyzecxx;

import java.io.IOException;

/**
 * Interface to object that handles file input,
 * including, presumably, keeping a stack of source files
 */
interface ReaderDriver
{
	void includeFile( CxxReader reader, String file, boolean search_current_directory,
					  int next_line_in_this_file)
		throws IOException;
	void popIncludeFile( CxxReader reader);

	/**
	 * Support pragma once
	 */
	void dontRepeat( CxxReader reader);
}
