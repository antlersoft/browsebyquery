/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A class can implement ISimpleElement so it can use SimpleHandler to make implementing
 * IElement easier.
 * @author Michael A. MacDonald
 *
 */
public interface ISimpleElement {
	public void gotElement( String name, String contents,
		    Attributes attributes) throws SAXException;

}
