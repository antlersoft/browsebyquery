
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

public class EntryPageHeader implements Serializable
{
    int offset;
    transient EntryPage page;

    EntryPageHeader()
    {
        offset=0;
        page=new EntryPage();
    }
}