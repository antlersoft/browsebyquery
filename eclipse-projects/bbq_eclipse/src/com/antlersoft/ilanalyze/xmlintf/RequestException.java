/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.xmlintf;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;

import com.antlersoft.util.xml.SimpleHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public class RequestException {

	static class Element implements IElement {
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#getElementTag()
		 */
		public String getElementTag() {
			return "RequestException";
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
			xml_writer.startElement("", "", getElementTag(), SimpleHandler.m_empty);
			SimpleHandler.writeElement( xml_writer, "Message", e.getMessage());
			SimpleHandler.writeElement( xml_writer, "StackTrace", e.getStackTrace());
			xml_writer.endElement("", "", getElementTag());
		}
		
		Element( RequestException re)
		{
			e=re;
		}
		
		private RequestException e;
	}
	
	public RequestException ( Exception excep)
	{
		message=excep.getMessage();
		StringWriter sw=new StringWriter();
		excep.printStackTrace( new PrintWriter( sw));
		trace=sw.toString();
	}
	
	public String getMessage() { return message; }
	public String getStackTrace() { return trace; }
	
	private String message;
	private String trace;

}
