/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.ISimpleElement;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public class AutoQueryNode implements IElement {
	private AutoQueryNode _parent;
	private ArrayList<AutoQueryNode> _children;
	private ArrayList<String> _queryTemplates;
	private String _typeKey;
	
	public AutoQueryNode(AutoQueryNode parent, String typeKey)
	{
		_parent = parent;
		_typeKey = typeKey;
		_children = new ArrayList<AutoQueryNode>();
		_queryTemplates = new ArrayList<String>();
	}
	
	private void addTemplatesToList(ArrayList<String> templates)
	{
		templates.addAll(_queryTemplates);
		if (_parent != null)
			_parent.addTemplatesToList(templates);
	}
	
	public List<String> getTemplatesForType(String type)
	{
		if (type.equals(_typeKey))
		{
			ArrayList<String> templates = new ArrayList<String>();
			addTemplatesToList(templates);
			return templates;
		}
		for (AutoQueryNode i : _children)
		{
			List<String> templates = i.getTemplatesForType(type);
			if (templates != null)
				return templates;
		}
		return null;
	}
	
	// IElement implementation
	
	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IElement#getElementTag()
	 */
	@Override
	public String getElementTag() {
		return "AutoQueryNode";
	}
	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IElement#readFromXML(com.antlersoft.util.xml.IHandlerStack)
	 */
	@Override
	public DefaultHandler readFromXML(IHandlerStack handlerStack) {
		return new Handler(handlerStack);
	}
	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
	 */
	@Override
	public void writeToXML(ContentHandler xmlWriter) throws SAXException {
		AttributesImpl attrs = new AttributesImpl();
		attrs.addAttribute("", "", "type_key", "CDATA", _typeKey);
		xmlWriter.startElement("","",getElementTag(),attrs);
		for (AutoQueryNode n : _children)
		{
			n.writeToXML(xmlWriter);
		}
		for (String n : _queryTemplates)
		{
			SimpleHandler.writeElement(xmlWriter, "template", n);
		}
		xmlWriter.endElement("", "", getElementTag());
	}
	
	class Handler extends DefaultHandler
	{
		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			_stack.popHandlerStack();
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (! _started)
			{
				_started = true;
				return;
			}
			if (qName.equals(getElementTag()))
			{
				AutoQueryNode child = null;
				String typeKey = attributes.getValue("type_key");
				for (AutoQueryNode i : _children)
				{
					if (i._typeKey.equals(typeKey))
					{
						child = i;
						break;
					}
				}
				if (child == null)
					child = new AutoQueryNode(AutoQueryNode.this, typeKey);
				_children.add(child);
				_stack.startWithHandler( child.readFromXML(_stack), uri, localName, qName, attributes);
			}
			else if (qName.equals("template"))
			{
				_stack.startWithHandler(new TemplateElement().readFromXML(_stack), uri, localName, qName, attributes);
			}
		}

		IHandlerStack _stack;
		boolean _started;
		
		Handler(IHandlerStack s)
		{
			_stack = s;
			_started = false;
		}
	}
	
	class TemplateElement implements ISimpleElement, IElement
	{
		TemplateElement()
		{
			
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#getElementTag()
		 */
		@Override
		public String getElementTag() {
			return "template";
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#readFromXML(com.antlersoft.util.xml.IHandlerStack)
		 */
		@Override
		public DefaultHandler readFromXML(IHandlerStack handlerStack) {
			return new SimpleHandler(handlerStack, this);
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
		 */
		@Override
		public void writeToXML(ContentHandler xmlWriter) throws SAXException {
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.ISimpleElement#gotElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void gotElement(String name, String contents,
				Attributes attributes) throws SAXException {
			_queryTemplates.add(contents);
		}
		
	}
}
