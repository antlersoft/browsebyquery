package analyzer.query;

public class ParseException extends Exception
{
    static String parseMessage( QueryParser qp)
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
            retval.append( qp.tokens[i].toString());
            if ( i==qp.currentIndex)
                retval.append( "<<here>>");
            retval.append( ' ');
        }
        if ( i==qp.currentIndex)
            retval.append( "<<here>>");
        return retval.toString();
    }

    ParseException( QueryParser qp)
    {
        super( parseMessage( qp));
    }
}
