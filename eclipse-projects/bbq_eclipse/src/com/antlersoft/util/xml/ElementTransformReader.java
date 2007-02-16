/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import java.io.IOException;
import java.io.Writer;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.sax.SAXSource;

import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * To be used to construct a javax.xml.transform.sax.SAXSource that will
 * work with javax.xml.transform.Transform to write an IElement to the javax.xml.transform.Result
 * of your choice.
 * 
 * @author Michael A. MacDonald
 *
 */
public class ElementTransformReader implements XMLReader {
	
	/**
	 * Adds a new line after the end of every element.
	 * 
	 * @author Michael A. MacDonald
	 *
	 */
	static class FormattingContentHandler implements ContentHandler {
		
		ContentHandler m_base;
		
		static char[] NEW_LINE={ '\n' };

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
		 */
		public void characters(char[] ch, int start, int length) throws SAXException {
			m_base.characters(ch, start, length);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endDocument()
		 */
		public void endDocument() throws SAXException {
			m_base.endDocument();
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		public void endElement(String uri, String localName, String qName) throws SAXException {
			m_base.endElement(uri, localName, qName);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
		 */
		public void endPrefixMapping(String prefix) throws SAXException {
			m_base.endPrefixMapping(prefix);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
		 */
		public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
			m_base.ignorableWhitespace(ch, start, length);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
		 */
		public void processingInstruction(String target, String data) throws SAXException {
			m_base.processingInstruction(target, data);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
		 */
		public void setDocumentLocator(Locator locator) {
			m_base.setDocumentLocator(locator);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
		 */
		public void skippedEntity(String name) throws SAXException {
			m_base.skippedEntity(name);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startDocument()
		 */
		public void startDocument() throws SAXException {
			m_base.startDocument();
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			m_base.ignorableWhitespace( NEW_LINE, 0, 1);
			m_base.startElement(uri, localName, qName, atts);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
		 */
		public void startPrefixMapping(String prefix, String uri) throws SAXException {
			m_base.startPrefixMapping(prefix, uri);
		}


	}

	private FormattingContentHandler m_formatting;
	private DTDHandler m_dtd_handler;
	private ErrorHandler m_error_handler;
	private EntityResolver m_entity_resolver;
	private IElement m_element;

	/**
	 * 
	 */
	public ElementTransformReader( IElement element) {
		m_element=element;
		m_formatting=new FormattingContentHandler();
	}
	
	/**
	 * Write an element to the output stream as an XML document
	 * @param element
	 * @param output
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void writeElement( IElement element, Writer output)
	throws IOException, SAXException, TransformerException
	{
		TransformerFactory.newInstance().newTransformer().transform(
				new SAXSource(new ElementTransformReader( element), new InputSource()),
				new StreamResult(output));
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#getFeature(java.lang.String)
	 */
	public boolean getFeature(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#setFeature(java.lang.String, boolean)
	 */
	public void setFeature(String name, boolean value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#getProperty(java.lang.String)
	 */
	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		throw new SAXNotRecognizedException( name);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#setProperty(java.lang.String, java.lang.Object)
	 */
	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new SAXNotRecognizedException( name);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#setEntityResolver(org.xml.sax.EntityResolver)
	 */
	public void setEntityResolver(EntityResolver resolver) {
		m_entity_resolver=resolver;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#getEntityResolver()
	 */
	public EntityResolver getEntityResolver() {
		return m_entity_resolver;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#setDTDHandler(org.xml.sax.DTDHandler)
	 */
	public void setDTDHandler(DTDHandler handler) {
		m_dtd_handler=handler;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#getDTDHandler()
	 */
	public DTDHandler getDTDHandler() {
		return m_dtd_handler;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#setContentHandler(org.xml.sax.ContentHandler)
	 */
	public void setContentHandler(ContentHandler handler) {
		m_formatting.m_base=handler;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#getContentHandler()
	 */
	public ContentHandler getContentHandler() {
		return m_formatting.m_base;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#setErrorHandler(org.xml.sax.ErrorHandler)
	 */
	public void setErrorHandler(ErrorHandler handler) {
		m_error_handler=handler;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#getErrorHandler()
	 */
	public ErrorHandler getErrorHandler() {
		return m_error_handler;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#parse(org.xml.sax.InputSource)
	 */
	public void parse(InputSource input) throws IOException, SAXException {
		m_formatting.startDocument();
		m_element.writeToXML( m_formatting);
		m_formatting.endDocument();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.XMLReader#parse(java.lang.String)
	 */
	public void parse(String systemId) throws IOException, SAXException {
		parse( new InputSource( systemId));
	}

}
