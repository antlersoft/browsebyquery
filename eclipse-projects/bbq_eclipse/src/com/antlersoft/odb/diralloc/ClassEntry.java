
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

import com.antlersoft.odb.DiskAllocator;

import java.io.Serializable;
import java.util.ArrayList;

class ClassEntry implements Serializable
{
    ClassEntry()
    {
    }

    String className;
    String fileName;
    int index;
    int reuseCount;
    /**
     * List of IndexEntry records that apply to this class
     */
    ArrayList indices;
    /**
     * Allocator for class object
     */
    transient StreamPair objectStreams;
    transient StreamPair indexStreams;
}