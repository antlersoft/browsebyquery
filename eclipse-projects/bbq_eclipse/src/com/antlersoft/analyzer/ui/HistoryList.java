package com.antlersoft.analyzer.ui;

import java.awt.*;

import java.awt.event.*;

import java.util.Enumeration;

import javax.swing.*;

public class HistoryList extends JList
{
	private DefaultListModel dlm;
	private JTextArea text;

	public HistoryList( JTextArea toChange)
	{
		super( new DefaultListModel());
		text=toChange;
		dlm=(DefaultListModel)getModel();
		setVisibleRowCount(2);
		addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					synchronized( dlm) {
						int index = locationToIndex(e.getPoint());
						text.setText( (String)dlm.elementAt(index));
						moveToTop( index);
					}
				}
			}
		});
	}

	public void addQuery( String toAdd)
	{
		synchronized ( dlm)
		{
			int j=0;
			for ( Enumeration i=dlm.elements();
				i.hasMoreElements(); j++)
			{
				if ( toAdd.equals( i.nextElement()))
				{
					moveToTop( j);
					return;
				}
			}
			dlm.insertElementAt( toAdd, 0);
		}
	}

	private void moveToTop( int index)
	{
		Object o=dlm.elementAt( index);
		dlm.remove( index);
		dlm.insertElementAt( o, 0);
		setSelectedIndex( 0);
	}
}
