package com.antlersoft.bbq_eclipse.views;


import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import com.antlersoft.analyzer.query.ParseException;
import com.antlersoft.analyzer.query.QueryParser;
import com.antlersoft.analyzer.query.SetExpression;

import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;
import com.antlersoft.bbq_eclipse.searchresult.Query;

/**
 *
 */

public class QueryView extends ViewPart {
	
	private SashForm _sashForm;
	private Composite _queryArea;
	private Text _queryText;
	private List _historyList;
	private Action _queryAction;
	private Action _selectAction;
	private Action _deleteAction;

	/**
	 * The constructor.
	 */
	public QueryView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		_sashForm=new SashForm( parent, SWT.VERTICAL);
		_queryArea=new Composite( _sashForm,0);
		_queryArea.setLayout( new FillLayout( SWT.HORIZONTAL));
		_historyList=new List( _sashForm, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL);
		_queryText=new Text( _queryArea, SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				QueryView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(_historyList);
		_historyList.setMenu(menu);
		//getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(_queryAction);
		manager.add(new Separator());
		manager.add(_selectAction);
		manager.add(_deleteAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(_selectAction);
		manager.add(_deleteAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(_queryAction);
		manager.add(_selectAction);
		manager.add(_deleteAction);
	}

	private void makeActions() {
        _selectAction=new Action() {
        	
	        public void run()
	        {
	            synchronized( _historyList)
	            {
	            	int popup_index=_historyList.getSelectionIndex();
	                if (popup_index >= 0) {
	                	String text=_historyList.getItem( popup_index);
	                	_queryText.setText( text);
	                	_historyList.remove( popup_index);
	                	_historyList.add( text, 0);
	                }
	            }
	        }
        };
        _selectAction.setText( "Select");
        
        _deleteAction=new Action() {
        	public void run()
        	{
        		synchronized ( _historyList)
        		{
        			int popup_index=_historyList.getSelectionIndex();
        			if ( popup_index>=0)
        				_historyList.remove( popup_index);
        		}
        	}
        };
        _deleteAction.setText( "Delete");
        
        _queryAction=new Action() {
        	public void run()
        	{
                String line=_queryText.getText();
                if ( line==null || line.length()==0)
                    return;
                String[] lines=_historyList.getItems();
                for ( int i=0; i<lines.length; ++i)
                	if ( lines[i].equals( line))
                	{
                		_historyList.remove( i);
                		break;
                	}
                QueryParser qp=Bbq_eclipsePlugin.getDefault().getQueryParser();
                qp.setLine( line);
                try
                {
                	SetExpression se=qp.getExpression();
                	NewSearchUI.runQueryInBackground( new Query( se, line));
                }
                catch ( ParseException pe)
                {
                	displayException( pe.getMessage(), pe);
                }
                _historyList.add( line, 0);
        	}
        };
        _queryAction.setText( "Query");
	}

    void displayException( String caption, Throwable t)
    {
        StringWriter sw=new StringWriter( 1000);
        PrintWriter pw=new PrintWriter( sw);
        t.printStackTrace( pw);
        pw.close();
        MessageBox mb=new MessageBox( getSite().getShell(), SWT.ICON_ERROR|SWT.OK);
        mb.setText( caption);
        mb.setMessage( sw.toString());
        mb.open();
    }

	private void hookDoubleClickAction() {
		/*
		_historyList..addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
		*/
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		_sashForm.setFocus();
	}
}