/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.query.environment.ui;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

/**
 * List box showing query results, one object per line
 * Multi-select list box
 * Pop-up menu offering "Select All" and "Copy" actions
 * @author Michael A. MacDonald
 *
 */
public class ResultList extends JList {
	/**
	 * @author Michael A. MacDonald
	 *
	 */
	class CopyAction extends AbstractAction {
		CopyAction()
		{
			super("Copy");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Clipboard clipboard;
			
			try {
				clipboard = getToolkit().getSystemClipboard();
			}
			catch (Exception ex)
			{
				this.setEnabled(false);
				return;
			}
            StringBuilder results=new StringBuilder();
            String lineSep = System.getProperty("line.separator");
            for (Object o : getSelectedValues())
            {
                results.append(o.toString());
                results.append(lineSep);
            }
            StringSelection contents = new StringSelection(results.toString());
            clipboard.setContents(contents, contents);
		}

	}

	/**
	 * @author Michael A. MacDonald
	 *
	 */
	class SelectAllAction extends AbstractAction {
		SelectAllAction()
		{
			super("Select All");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int size = dlm.getSize();
			if (size > 0)
			{
				getSelectionModel().addSelectionInterval(0, size - 1);
			}
		}

	}

	DefaultListModel dlm;
	private JPopupMenu popup_menu;
	
	public ResultList()
	{
		super(new DefaultListModel());
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dlm = (DefaultListModel)getModel();
        popup_menu=new JPopupMenu();
        popup_menu.add( new SelectAllAction());
        popup_menu.add( new CopyAction());
		addMouseListener( new MouseAdapter() {
            public void mousePressed( MouseEvent e) {
                showPopup( e);
            }
            public void mouseReleased( MouseEvent e) {
                showPopup( e);
            }
		});
	}

    void showPopup( MouseEvent e)
    {
        if ( e.isPopupTrigger())
        {
            int popup_index = locationToIndex(e.getPoint());
            if ( popup_index>=0)
                popup_menu.show( this, e.getX(), e.getY());
        }
    }
	
	public void setContents(List<?> contents)
	{
		dlm.clear();
		for (Object o : contents)
			dlm.addElement(o);
	}
}
