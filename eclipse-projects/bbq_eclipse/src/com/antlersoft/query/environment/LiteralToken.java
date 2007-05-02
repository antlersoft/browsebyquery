/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.lang.StringBuffer;

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
    	int len=value.length();
    	StringBuffer sb=new StringBuffer( len+5);
    	sb.append( '"');
    	for ( int i=0; i<len; ++i)
    	{
    		char c=value.charAt( i);
    		if ( c=='\\' || c=='"')
    			sb.append( '\\');
    		sb.append( c);
    	}
        return sb.toString();
    }
}