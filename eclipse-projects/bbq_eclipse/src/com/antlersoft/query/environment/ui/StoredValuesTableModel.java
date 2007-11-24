/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment.ui;

import com.antlersoft.query.Transform;
import com.antlersoft.query.environment.QueryLanguageEnvironment;
import com.antlersoft.query.environment.TokenSequence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

/**
 * Abstract table model for displaying stored values from the QueryLanguageEnvironment
 * (set expressions and transforms)
 * @author Michael A. MacDonald
 *
 */
public class StoredValuesTableModel extends AbstractTableModel implements
		PropertyChangeListener {
	
	static String[] columnNames={ "Name", "Type", "Value" };
	
	static ColumnValue[] valueGetters={
		new ColumnValue() {
			public Object getValue( String name, TokenSequence seq)
			{
				return name;
			}
		},
		new ColumnValue() {
			public Object getValue( String name, TokenSequence seq)
			{
				Object result;
				if ( seq.getResult() instanceof Transform)
					result="Transform";
				else
					result="Set Expression";
				return result;
			}
		},
		new ColumnValue() {
			public Object getValue( String name, TokenSequence seq)
			{
				return seq.toString();
			}
		}
	};
	
	private QueryLanguageEnvironment environment;
	
	StoredValuesTableModel( QueryLanguageEnvironment env)
	{
		environment=env;
		env.addStoredValuesListener(this);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		fireTableDataChanged();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return environment.getStoredValues().length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		String name=environment.getStoredValues()[rowIndex];
		return valueGetters[columnIndex].getValue(name, environment.getStoredSequence(name));
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	static interface ColumnValue
	{
		Object getValue( String name, TokenSequence seq);
	}

}
