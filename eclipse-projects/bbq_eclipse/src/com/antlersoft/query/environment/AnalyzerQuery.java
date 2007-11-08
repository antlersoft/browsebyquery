/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.query.BasicBase;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.ISimpleElement;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * A query language environment specialized to support a set of strings as part of its state representing imported
 * packages (or namespaces...)
 * @author Michael A. MacDonald
 *
 */
public class AnalyzerQuery extends QueryLanguageEnvironment {
	/**
	 * @author Michael A. MacDonald
	 *
	 */
	class AnalyzerQueryElement extends DefaultHandler implements IElement, ISimpleElement {

		IElement m_base;
		IHandlerStack m_stack;
		
		private static final String ELEMENT_TAG="analyzer_query";
		
		/**
		 * 
		 */
		public AnalyzerQueryElement( IElement base_element) {
			super();
			m_base=base_element;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#readFromXML(com.antlersoft.util.xml.IHandlerStack)
		 */
		public DefaultHandler readFromXML(IHandlerStack handler_stack) {
			m_stack=handler_stack;
			return this;
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
		 */
		public void writeToXML(ContentHandler xml_writer) throws SAXException {
			xml_writer.startElement( "", "", ELEMENT_TAG, SimpleHandler.m_empty);
			for ( Iterator i=importedPackages.iterator(); i.hasNext();)
			{
				SimpleHandler.writeElement( xml_writer, "import", (String)i.next());
			}
			m_base.writeToXML( xml_writer);
			xml_writer.endElement( "", "", ELEMENT_TAG);
		}

		public String getElementTag() { return ELEMENT_TAG; }

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if ( qName.equals( ELEMENT_TAG))
				m_stack.popHandlerStack();
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if ( qName.equals( "import"))
			{
				m_stack.startWithHandler( new SimpleHandler(m_stack, this), uri, localName, qName, attributes);
			}
			else if ( qName.equals( m_base.getElementTag()))
			{
				m_stack.startWithHandler( m_base.readFromXML( m_stack), uri, localName, qName, attributes);
			}
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.ISimpleElement#gotElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void gotElement(String name, String contents, Attributes attributes) throws SAXException {
			importedPackages.add( contents);
		}
	}

	public AnalyzerQuery( BasicBase parser)
	{
		super( parser);
        importedPackages=new TreeSet();
	}
	
	public IElement getElement()
	{
		return new AnalyzerQueryElement( super.getElement());
	}
	
	public Collection getImported()
	{
		return importedPackages;
	}

    // Package interface
    private TreeSet importedPackages;
    
}
