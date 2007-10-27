/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.Collection;

/**
 * A type referenced in the analyzed system as it is read in (as opposed to how it is
 * stored in the database)
 * @author Michael A. MacDonald
 *
 */
public interface ReadType {
	/**
	 * Determine if this type is fully specified (no unspecified parameters) or not
	 * @return true if the type reference isn't in a generic class/method, or if all the parameters of the type are
	 * specified 
	 */
	public boolean isFullySpecified();
	/**
	 * A collection of the generic arguments for the type
	 * @return A collection of ReadType objects that represent the generic arguments that fully or partially
	 * specify this type, or null if it is not a generic type.
	 */
	public Collection getGenericArgs();
	
	/**
	 * 
	 * @return true if the type represents a class type
	 */
	public boolean isClassType();
}
