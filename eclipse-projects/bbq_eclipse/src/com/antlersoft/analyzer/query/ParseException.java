package com.antlersoft.analyzer.query;

public class ParseException extends Exception
{
    static String parseMessage( QueryParserBase qp)
    {
        StringBuffer retval=new StringBuffer();

        if ( qp.getRuleMessage()!=null)
        {
                retval.append( qp.getRuleMessage());
                retval.append( ": ");
        }

        int i;
        for ( i=0; i<qp.tokens.length; i++)
        {
            if ( i==qp.currentIndex)
                retval.append( "<<here>>");
            retval.append( qp.tokens[i].toString());
            retval.append( ' ');
        }
        if ( i==qp.currentIndex)
            retval.append( "<<here>>");
        return retval.toString();
    }

    ParseException( QueryParserBase qp)
    {
        super( parseMessage( qp));
    }
}
