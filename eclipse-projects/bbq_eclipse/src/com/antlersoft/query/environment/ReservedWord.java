/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.Hashtable;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Symbol.DuplicateSymbolException;

/**
 * @author Michael A. MacDonald
 *
 */
public class ReservedWord extends Symbol {

    static Hashtable wordList=new Hashtable();
    private ReservedWord( String w)
        throws DuplicateSymbolException
    {
        super( w);
        wordList.put( w, this);
    }

    public static ReservedWord getReserved( String w)
    {
        ReservedWord result;
        try
        {
            result=new ReservedWord( w);
        }
        catch ( DuplicateSymbolException dse)
        {
            result=(ReservedWord)dse.duplicate;
        }
        return result;
    }
}
