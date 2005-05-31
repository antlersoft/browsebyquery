/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
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