package analyzer.query;

public class ParseException extends Exception
{
    static String parseMessage( QueryParser qp)
    {
	StringBuffer retval=new StringBuffer();

	int i;
	for ( i=0; i<qp.tokens.length; i++)
	{
	    retval.append( qp.tokens[i].toString());
	    if ( i==qp.maxIndex)
		retval.append( "<<here>>");
	    retval.append( ' ');
	}
	if ( i==qp.maxIndex)
	    retval.append( "<<here>>");
	return retval.toString();
    }

    ParseException( QueryParser qp)
    {
	super( parseMessage( qp));
    }
}
