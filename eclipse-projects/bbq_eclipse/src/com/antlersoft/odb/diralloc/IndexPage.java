
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb.diralloc;

import java.io.Serializable;

public class IndexPage implements Serializable
{
    int size;
    Comparable[] keyArray;
    int[] nextOffsetArray;
    // Null if this is not a leaf page
    int[] reuseArray;

    transient boolean modified;
    transient IndexPageKey thisPage;
}