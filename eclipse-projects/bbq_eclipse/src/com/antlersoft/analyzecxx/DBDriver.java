package com.antlersoft.analyzecxx;

/**
 * Records things found in source files
 */
public interface DBDriver
{
	void startTranslationUnit( String file_name);
	void finishTranslationUnit();
	void includeFile( String included);
	void setCurrentFile( String file);
	void setCurrentLine( int line);
	String getCurrentFile();
	int getCurrentLine();
	void defineMacro( String name, boolean is_function);
	void undefineMacro( String name);
	void checkForMacroDefinition( String name);
	void preprocessorConditional( boolean success);
	void expandMacro( String name, boolean is_function);
}