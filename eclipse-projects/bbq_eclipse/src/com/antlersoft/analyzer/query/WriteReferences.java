package analyzer.query;

import analyzer.DBFieldReference;

class WriteReferences extends Filter
{
	WriteReferences()
	{
		super( DBFieldReference.class);
	}

	protected boolean include( Object toFilter)
		throws Exception
	{
		return ((DBFieldReference)toFilter).isWrite();
	}
}
