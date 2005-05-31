/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
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
