package analyzer.query;

import java.util.Enumeration;
import analyzer.AnalyzerDB;
import analyzer.DBClass;

class FieldsIn implements QueryParser.FieldSet
{
	QueryParser.ClassSet _classSet;

	FieldsIn( QueryParser.ClassSet classSet)
	{
		_classSet=classSet;
	}

	public Enumeration execute( AnalyzerDB db)
		throws Exception
	{
		return new QueryParser.BiEnumeration( _classSet.execute( db)) {
			public Enumeration getNextCurrent( Object base)
			{
				return ((DBClass)base).getFields();
			}
		};
	}
}