
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb;

import java.util.Iterator;

/**
 * An iterator resulting from a search on a key.
 */
public interface IndexIterator extends Iterator
{
    /**
     * Returns true if the search that produced this iterator matched
     * the key exactly.
     */
    public boolean isExactMatch();
}