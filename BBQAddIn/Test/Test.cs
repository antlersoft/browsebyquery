using System;
using System.IO;
using System.Xml.Serialization;

namespace com.antlersoft.BBQClient
{
	class Test
	{
		public static void Main( String[] args)
		{
            //QueryRequest request = new QueryRequest("methods in class \"System.Int32\"");
            QueryRequest request = new QueryRequest("calls to selection");
            StringWriter sw = new StringWriter();
            request.ObjectKeys.Add("2306:0");
            request.ObjectKeys.Add("3374:0");
            new XmlSerializer(typeof(QueryRequest)).Serialize(sw, request);
            Console.WriteLine(sw.ToString());
            Object o = new BrowseByQueryBySocket().PerformQuery(
				request);
			sw=new StringWriter();
			new XmlSerializer(typeof(QueryResponse)).Serialize( sw, o);
			Console.WriteLine( sw.ToString());
			Console.WriteLine( "Done!");
		}
	}
}
