using System;
using System.IO;
using System.Xml.Serialization;

namespace com.antlersoft.BBQAddIn
{
	class Test
	{
		public static void Main( String[] args)
		{
			Object o=new BrowseByQueryBySocket().PerformQuery(
				new QueryRequest("nmethods in class \"System.Int32\""));
			StringWriter sw=new StringWriter();
			new XmlSerializer(typeof(QueryResponse)).Serialize( sw, o);
			Console.WriteLine( sw.ToString());
			Console.WriteLine( "Done!");
		}
	}
}
