package com.antlersoft.analyzecxx;

/**
 * Records things found in source files
 */
public interface DBDriver
{
	void startTranslationUnit( String file_name);
	void finishTranslationUnit();
	void includeFile( String included, int from_line);
	void setCurrentFile( String file);
	void defineMacro( String name, boolean is_function, int from_line);
	void undefineMacro( String name, int from_line);
	void checkForMacroDefinition( String name, int from_line);
	void expandMacro( String name, boolean is_function, int from_line);
}