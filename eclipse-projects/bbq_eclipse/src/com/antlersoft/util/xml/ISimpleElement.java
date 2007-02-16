/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Michael A. MacDonald
 *
 */
public interface ISimpleElement {
	public void gotElement( String name, String contents,
		    Attributes attributes) throws SAXException;

}
