package analyzer.query;

import java.util.Enumeration;
import analyzer.AnalyzerDB;
import analyzer.DBFieldReference;

class WriteReferences implements QueryParser.FieldReferenceSet
{
	private QueryParser.FieldReferenceSet _base;

	WriteReferences( QueryParser.FieldReferenceSet base)
	{
		_base=base;
	}

	public Enumeration execute( AnalyzerDB db)
		throws Exception
	{
		return new QueryParser.FilterEnumeration( _base.execute( db)) {
			public Object filterObject( Object toFilter)
			{
				return ((DBFieldReference)toFilter).isWrite()?toFilter:null;
			}
		};
	}
}