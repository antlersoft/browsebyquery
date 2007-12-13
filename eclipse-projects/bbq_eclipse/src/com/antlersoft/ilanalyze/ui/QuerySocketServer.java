/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.ui;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.TransformerException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.ilanalyze.xmlintf.IBrowseByQuery;
import com.antlersoft.ilanalyze.xmlintf.QueryRequest;
import com.antlersoft.ilanalyze.xmlintf.QueryResponse;

import com.antlersoft.util.Messenger;
import com.antlersoft.util.xml.ElementTransformReader;
import com.antlersoft.util.xml.HandlerStack;

/**
 * Listens on a certain port for connections from query clients, which will make query requests,
 * and perform these requests on an IBrowseByQuery interface, which must be thread-safe.
 * 
 * @author Michael A. MacDonald
 *
 */
class QuerySocketServer implements Runnable {
	/**
	 * 20217 = 02 02 17 = BBQ
	 */
	static final int DEFAULT_PORT=20217;
	
	private ServerSocket listener;
	private IBrowseByQuery bbq;
	
	static Logger logger=Logger.getLogger("com.antlersoft.ilanalyze.ui.QuerySocketServer");
	
	QuerySocketServer( IBrowseByQuery xmlIntf, int port) throws IOException
	{
		listener=new ServerSocket( port);
		bbq=xmlIntf;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try
		{
			logger.info( "Started listening for query socket clients");
			while ( true)
			{
				Socket s=listener.accept();
				new Thread( new QuerySocketClient( s, bbq), "QuerySocketClient-"+s.getLocalPort()).start();
			}
		}
		catch ( IOException ioe)
		{
			logger.log( Level.FINE, "Stopped listening due to IO exception: "+ioe.getMessage(), ioe);
		}
		finally
		{
			logger.info( "Finished listening");			
		}
	}
	
	static class QuerySocketClient implements Runnable
	{
		private Messenger messenger;
		
		private IBrowseByQuery bbq;
		
		private SAXParser parser;
		
		QuerySocketClient( Socket s, IBrowseByQuery i) throws IOException
		{
			bbq=i;
			messenger=new Messenger(s);
		}
		
		public void run() {
			try
			{
				logger.info( Thread.currentThread().getName()+" started");
				parser=SAXParserFactory.newInstance().newSAXParser();
				messenger.receiveMessage();
				while ( true)
				{
					String s=messenger.inputString();
					logger.finer( s);
					HandlerStack stack=new HandlerStack( parser.getXMLReader());
					QueryRequest request=new QueryRequest();
					DefaultHandler top=new QueryRequest.Element( request).readFromXML( stack);
					stack.pushHandlerStack( top);
					parser.parse( new InputSource( new StringReader( s)), top);
					QueryResponse response=bbq.PerformQuery(request);
					StringWriter out=new StringWriter();
					ElementTransformReader.writeElement( new QueryResponse.Element( response), out);
					String output=out.toString();
					logger.finer(output);
					messenger.writeString( output);
					messenger.sendRequest();
				}
			}
			catch ( ParserConfigurationException pce)
			{
				logger.log( Level.WARNING, Thread.currentThread().getName()+" stopped listening due to Parser configuration exception: "+pce.getMessage(), pce);				
			}
			catch ( SAXException se)
			{
				logger.log( Level.WARNING, Thread.currentThread().getName()+" stopped listening due to SAX exception: "+se.getMessage(), se);				
			}
			catch ( TransformerException te)
			{
				logger.log( Level.WARNING, Thread.currentThread().getName()+" stopped listening due to Transformer exception: "+te.getMessage(), te);				
			}
			catch ( IOException ioe)
			{
				logger.log( Level.FINE, Thread.currentThread().getName()+" stopped listening due to IO exception: "+ioe.getMessage(), ioe);				
			}
			finally
			{
				logger.info( Thread.currentThread().getName()+" ending");
				try
				{
					messenger.close();
				}
				catch ( IOException ioe)
				{
					
				}
			}
		}
	}
}
