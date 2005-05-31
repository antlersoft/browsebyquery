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
package com.antlersoft.odb;

import java.io.Serializable;

/**
 * Interface that defines the only method objects need to support to be persistent.
 * Obviously, we assume that the objects are also serializable.  It is also possible
 * to implement this interface so persistence breaks, but you would have to avoid
 * the obvious implementation.
 *
 * The first class in the class hierarchy that is persistent should contain a
 * private transient PersistentImpl member, which this class will return.  It should initialize
 * this member when constructed by calling its empty constructor.  Classes derived
 * from a Persistent class won't have to do anything.
 */
public interface Persistent extends Serializable
{
    public PersistentImpl _getPersistentImpl();
}
