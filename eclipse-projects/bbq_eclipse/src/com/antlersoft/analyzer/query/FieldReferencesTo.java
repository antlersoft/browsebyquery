package analyzer.query;

import java.util.Enumeration;
import analyzer.DBField;
import analyzer.AnalyzerDB;

class FieldReferencesTo implements QueryParser.FieldReferenceSet
{
	private QueryParser.FieldSet _fields;
	FieldReferencesTo( QueryParser.FieldSet fields)
	{
		_fields=fields;
	}

	public Enumeration execute( AnalyzerDB db)
		throws Exception
	{
		return new QueryParser.BiEnumeration( _fields.execute( db)) {
			public Enumeration getNextCurrent( Object base)
			{
				return ((DBField)base).getReferencedBy();
			}
		};
	}
}