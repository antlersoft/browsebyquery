package com.antlersoft.analyzer.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JList;

import com.antlersoft.analyzer.query.QueryParser;

public class StoredValuesList extends JList implements PropertyChangeListener
{
	private QueryParser m_qp;

	public StoredValuesList( QueryParser qp)
	{
		super();
		m_qp=qp;
		qp.addStoredValuesListener( this);
		setVisibleRowCount(2);
		setListData( qp.getStoredValues());
	}

	public void propertyChange( PropertyChangeEvent pce)
	{
		setListData( m_qp.getStoredValues());
	}
}
