/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.xmlintf;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public class QueryResponse {
	

	private RequestException re;
	
	private ArrayList<ResponseObject> responseList;
	
	private String resultClassName;

	public RequestException getRequestException()
	{
		return re;
	}
	
	public int getResponseCount()
	{
		return responseList.size();
	}
	
	public String getResultClassName()
	{
		return resultClassName;
	}
	
	public List<ResponseObject> getResponses()
	{
		return responseList;
	}
	
	public QueryResponse( RequestException excep)
	{
		responseList=new ArrayList<ResponseObject>(0);
		resultClassName = "";
		re=excep;
	}
	
	public QueryResponse( Class<?> resultClass, Enumeration<?> e)
	{
		resultClassName = resultClass.getName();
		re=null;
		responseList=new ArrayList<ResponseObject>();
		for ( ; e.hasMoreElements(); )
		{
			responseList.add( new ResponseObject( e.nextElement()));
		}
	}
	
	public static class Element implements IElement
	{
		private QueryResponse qr;
		
		public Element( QueryResponse response)
		{
			qr=response;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#getElementTag()
		 */
		public String getElementTag() {
			return "QueryResponse";
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#readFromXML(com.antlersoft.util.xml.IHandlerStack)
		 */
		public DefaultHandler readFromXML(IHandlerStack handler_stack) {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
		 */
		public void writeToXML(ContentHandler xml_writer) throws SAXException {
			AttributesImpl impl=new AttributesImpl();
			impl.addAttribute("", "", "ResponseCount", "CDATA", Integer.toString(qr.getResponseCount()));
			impl.addAttribute("","", "ResultClassName", "CDATA", qr.getResultClassName());
			xml_writer.startElement("", "", getElementTag(), impl);
			if ( qr.getRequestException()!=null)
			{
				new RequestException.Element( qr.getRequestException()).writeToXML(xml_writer);
			}
			xml_writer.startElement( "", "", "Responses", SimpleHandler.m_empty);
			for ( Iterator<ResponseObject> i=qr.getResponses().iterator(); i.hasNext();)
			{
				new ResponseObject.Element( i.next()).writeToXML(xml_writer);
			}
			xml_writer.endElement( "", "", "Responses");
			xml_writer.endElement( "", "", getElementTag());
		}
	
	}
}
