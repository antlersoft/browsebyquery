using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace com.antlersoft.BBQAddIn
{
    public interface IBrowseByQuery
    {
        QueryResponse PerformQuery( QueryRequest request);
    }

    public class QueryRequest
    {
        public QueryRequest()
        { }

        public QueryRequest(String text)
        {
            queryText = text;
        }

        [XmlElement]
        public String QueryText
        {
            get { return QueryText; }
            set { QueryText=value; }
        }

        private String queryText=String.Empty;

    }

    public class QueryResponse
    {
        private int responseCount = 0;

        public int ResponseCount
        {
            get { return responseCount; }
            set { responseCount = value; }
        }
        private ResponseObject[] responses = null;

        [XmlArray]
        public ResponseObject[] Responses
        {
            get { return responses; }
            set { responses = value; }
        }

        private RequestException exception = null;

        public RequestException Exception
        {
            get { return exception; }
            set { exception = value; }
        }
    }

    public class RequestException
    {
        [XmlElement]
        public String Message
        {
            get { return message; }
        }

        [XmlElement]
        public String StackTrace
        {
            get { return stackTrace; }
        }

        private String message = String.Empty;
        private String stackTrace = String.Empty;
    }

    public class ResponseObject
    {
        private String objectType = String.Empty;

        public String ObjectType
        {
            get { return objectType; }
            set { objectType = value; }
        }
        private String description = String.Empty;

        public String Description
        {
            get { return description; }
            set { description = value; }
        }
        private String fileName = String.Empty;

        public String FileName
        {
            get { return fileName; }
            set { fileName = value; }
        }
        private int lineNumber = 0;

        public int LineNumber
        {
            get { return lineNumber; }
            set { lineNumber = value; }
        }
    }
}
