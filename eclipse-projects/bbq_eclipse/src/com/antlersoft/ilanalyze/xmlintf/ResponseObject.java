/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.xmlintf;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.ilanalyze.db.DBSourceFile;
import com.antlersoft.ilanalyze.db.DBSourceObject;

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;

/**
 * @author Michael A. MacDonald
 *
 */
public class ResponseObject {
	
	ResponseObject( ObjectDB db, Object o)
	{
		file="";
		line= -1;
		if ( o instanceof DBSourceObject)
		{
			DBSourceObject so=((DBSourceObject)o);
			DBSourceFile f=so.getSourceFile();
			if ( f!=null)
			{
				file=f.toString();
				line=so.getLineNumber();
			}
		}
		type=o.getClass().getName();
		description=o.toString();
		if (o instanceof Persistent)
		{
			objectKey = db.keyToString(db.getObjectKey(o));
		}
		else
			objectKey = "";
	}
	public String getObjectType()
	{
		return type;
	}
	
	public String getDescription() { return description; }
	
	public String getFileName() { return file; }
	
	public int getLineNumber() { return line; }
	
	public String getObjectKey() { return objectKey; }
	
	private String type;
	private String description;
	private String file;
	private int line;
	private String objectKey;
	
	static class Element implements IElement
	{
		private ResponseObject ro;
		Element( ResponseObject response)
		{
			ro=response;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#getElementTag()
		 */
		public String getElementTag() {
			return "ResponseObject";
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
			impl.addAttribute("", "", "Type", "CDATA", ro.getObjectType());
			impl.addAttribute("", "", "Description", "CDATA", ro.getDescription());
			impl.addAttribute("", "", "FileName", "CDATA", ro.getFileName());
			impl.addAttribute("", "", "LineNumber", "CDATA", Integer.toString(ro.getLineNumber()));
			impl.addAttribute("", "", "ObjectKey", "CDATA", ro.getObjectKey());
			xml_writer.startElement("", "", getElementTag(), impl);
			xml_writer.endElement("", "", getElementTag());
		}
	}
}
