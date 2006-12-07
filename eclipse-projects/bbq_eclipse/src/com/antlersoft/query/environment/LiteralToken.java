/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import com.antlersoft.parser.Token;

import com.antlersoft.query.BasicBase;

public class LiteralToken extends Token
{
    LiteralToken( String val)
    {
        super( BasicBase.literalString, val);
    }

    public String toString()
    {
        return "\""+value+"\"";
    }
}