/*
 * <p>Copyright (c) 2003-2004  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.appcontext;

import java.awt.*;

import java.awt.event.*;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.*;

/**
 * Convenience class for showing a simple Swing dialog when
 * the program settings indicate the program version has changed.
 */
public class ReleaseNotes extends JDialog
{
	public static final String VERSION_STRING="version";
	public static final String RELEASE_NOTES_STRING="release_notes";
	public static final String TITLE="Release Notes";

	/**
	 * Shows the release notes in a bundle if the version does not match
	 * the version in the Properties.  Also, updates the version
	 * in the Properties.
	 */
	public static void showNotes( Frame parent, Properties properties, String version,
		String bundle_name, String title, Icon notes_icon, String intro_text)
	{
		String property_version=properties.getProperty( VERSION_STRING);
		if ( property_version==null || ! property_version.equals( version))
		{
			showNotes( parent, bundle_name, title, notes_icon, intro_text);
			properties.put( VERSION_STRING, version);
		}
	}

	/**
	 * shows the release notes in bundle unconditionally.
	 */
	public static void showNotes( Frame parent, String bundle_name, String title, Icon notes_icon, String intro_text)
	{
		String release_text;
		try
		{
			release_text=ResourceBundle.getBundle( bundle_name).getString( RELEASE_NOTES_STRING);
			if ( release_text==null)
				throw new Exception();
		}
		catch ( Exception e)
		{
			release_text="(Missing release notes)";
		}
		new ReleaseNotes( parent, release_text, title, notes_icon, intro_text).show();
	}

	private ReleaseNotes( Frame parent, String release_text, String title, Icon notes_icon, String intro_text)
	{
		super( parent, title==null ? TITLE : title, true);
		Container content_pane=getContentPane();
		content_pane.setLayout( new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.ipadx=c.ipady=2;
		if ( notes_icon!=null)
		{
			c.gridx=0;
			c.gridy=0;
			c.anchor=c.NORTH;
			c.gridwidth=1;
			c.gridheight=2;
			c.fill=c.NONE;
			content_pane.add( new JLabel( notes_icon), c);
		}
		if ( intro_text!=null)
		{
			if ( notes_icon!=null)
			{
				c.gridx=1;
				c.gridwidth=1;
			}
			else
			{
				c.gridx=0;
				c.gridwidth=2;
			}
			c.gridy=0;
			c.gridheight=1;
			c.fill=c.HORIZONTAL;
			c.weightx=1.0;
			c.anchor=c.CENTER;
			content_pane.add( new JLabel( intro_text), c);
		}
		if ( notes_icon==null)
		{
			c.gridwidth=2;
			c.gridx=0;
		}
		else
		{
			c.gridwidth=1;
			c.gridx=1;
		}
		if ( intro_text==null)
		{
			c.gridheight=2;
			c.gridy=0;
		}
		else
		{
			c.gridheight=1;
			c.gridy=1;
		}
		c.fill=c.BOTH;
		c.weightx=c.weighty=1.0;
		JEditorPane text_area=new JEditorPane();
		text_area.setContentType( "text/html");
		text_area.setEditable( false);
		text_area.setText( release_text);
		content_pane.add( new JScrollPane( text_area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), c);
		c.fill=c.NONE;
		c.gridwidth=2;
		c.gridheight=1;
		c.gridx=0;
		c.gridy=2;
		c.weightx=c.weighty=0;
		JButton ok_button=new JButton( "OK");
		ok_button.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent ae)
				{
					dispose();
				}
			});
		content_pane.add( ok_button, c);
		setSize( 400, 550);
	}
}
