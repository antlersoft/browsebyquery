using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Text;

using System.Xml.Serialization;

namespace com.antlersoft.BBQAddIn
{
    enum BBQSocketState
    {
        UNCONNECTED,
        CONNECTING,
        CONNECTED,
        CLOSING
    };
    class BrowseByQueryBySocket : IBrowseByQuery, IDisposable
    {
        static const int DEFAULT_PORT = 20217;
        static const string DEFAULT_HOST = "localhost";

        XmlSerializer queryRequestSerializer;
        XmlSerializer queryResponseSerializer;
        BBQSocketState state;
        Socket socket;

        public BrowseByQueryBySocket()
        {
            queryRequestSerializer=new XmlSerializer( typeof(QueryRequest));
            queryResponseSerializer = new XmlSerializer(typeof(QueryResponse));
            state = BBQSocketState.UNCONNECTED;
        }

        public void Connect()
        {
            try
            {
                socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                state = BBQSocketState.CONNECTING;
                socket.Connect(DEFAULT_HOST, DEFAULT_PORT);
                state = BBQSocketState.CONNECTED;
            }
            finally
            {
                if (state == BBQSocketState.CONNECTING)
                {
                    state = BBQSocketState.UNCONNECTED;
                    socket.Close();
                    socket = null;
                }
            }
        }

        #region IBrowseByQuery Members

        public QueryResponse PerformQuery(QueryRequest request)
        {
            throw new Exception("The method or operation is not implemented.");
        }

        #endregion

        #region IDisposable Members

        public void Dispose()
        {
            if ( socket!=null && state!=BBQSocketState.UNCONNECTED)
                socket.Close();
        }

        #endregion
    }
}
