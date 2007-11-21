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
    	int len=value.length();
    	StringBuilder sb=new StringBuilder( len+5);
    	sb.append( '"');
    	for ( int i=0; i<len; ++i)
    	{
    		char c=value.charAt( i);
    		if ( c=='\\' || c=='"')
    			sb.append( '\\');
    		sb.append( c);
    	}
    	sb.append('"');
        return sb.toString();
    }
}