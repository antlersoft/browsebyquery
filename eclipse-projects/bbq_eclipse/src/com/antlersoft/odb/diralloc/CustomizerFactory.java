
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

import com.antlersoft.odb.ObjectStreamCustomizer;

public class CustomizerFactory
{
    public static CustomizerFactory BASE_FACTORY=new CustomizerFactory();

    public CustomizerFactory()
    {
    }

    /**
     * Override this to provide custom customizers
     */
    public ObjectStreamCustomizer getCustomizer( Class toStore)
    {
        return ObjectStreamCustomizer.BASE_CUSTOMIZER;
    }
}