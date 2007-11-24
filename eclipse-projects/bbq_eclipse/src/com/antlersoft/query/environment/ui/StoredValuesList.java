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
package com.antlersoft.query.environment.ui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.antlersoft.query.environment.QueryLanguageEnvironment;

/**
 * Swing component (originally a JList, now a JScrollPane containing a JTable)
 * that lists the stored values in a query language environment
 * @author Michael A. MacDonald
 *
 */
public class StoredValuesList extends JScrollPane
{
	public StoredValuesList( QueryLanguageEnvironment qp)
	{
		super(new StoredValuesTable( qp));
	}
	
	static class StoredValuesTable extends JTable
	{
		/* (non-Javadoc)
		 * @see javax.swing.JTable#getPreferredScrollableViewportSize()
		 */
		public Dimension getPreferredScrollableViewportSize() {
			Dimension d=super.getPreferredScrollableViewportSize();
			d.width=450;
			d.height=getModel().getRowCount()*20+20;
			if ( d.height<100)
				d.height=100;
			return d;
		}

		StoredValuesTable( QueryLanguageEnvironment qp)
		{
			super( new StoredValuesTableModel( qp));
			getColumnModel().getColumn(0).setPreferredWidth( 80);
			getColumnModel().getColumn(1).setPreferredWidth( 80);
			getColumnModel().getColumn(2).setPreferredWidth( 240);
		}
	}
}
