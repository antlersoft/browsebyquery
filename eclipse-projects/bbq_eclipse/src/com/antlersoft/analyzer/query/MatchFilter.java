package analyzer.query;

import analyzer.DBClass;
import analyzer.DBMethod;
import analyzer.DBField;

class MatchFilter extends Filter
{
	private String match;

	MatchFilter( String toMatch)
	{
		super( null);
		match=toMatch;
	}

	public boolean include( Object toMatch)
		throws Exception
	{
		Class c=getFilterClass();

		if ( c==null)
			throw new Exception( "Unbound match filter");
		String name=null;
		if ( c==DBClass.class)
		{
			name=((DBClass)toMatch).getName();
		}
		else if ( c==DBMethod.class)
		{
			name=((DBMethod)toMatch).getName();
		}
		else if ( c==DBField.class)
		{
			name=((DBField)toMatch).getName();
		}
		else
			throw new Exception( "Match filter bound incorrect class "+c.getName());

		return name.indexOf( match)!= -1;
	}

	public void lateBind( Class c)
	{
		_filterClass=c;
	}
}