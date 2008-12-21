package com.antlersoft.bbq_eclipse.views;


import java.io.PrintWriter;
import java.io.StringWriter;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.handlers.*;
import org.eclipse.ui.part.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;


import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;
import com.antlersoft.bbq_eclipse.searchresult.Query;
import com.antlersoft.bbq_eclipse.builder.BBQBuilder;

import com.antlersoft.query.SetExpression;
import com.antlersoft.query.environment.AnalyzerQuery;
import com.antlersoft.query.environment.ParseException;

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
	private Action _rebuildAction;
	
	private String[] _savedState;
	
	private IHandlerActivation _activation;
	
	final static String QUERY_VIEW_TYPE="com.antlersoft.bbq_eclipse.views.QueryView";
	final static String RUN_QUERY_COMMAND="com.antlersoft.bbq_eclipse.command.RunQuery";

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
		initializeHistoryList();
		makeActions();
		_activation=((IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class)).
		activateHandler(RUN_QUERY_COMMAND, new ActionHandler(_queryAction));
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		((IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class)).deactivateHandler(_activation);
		_activation = null;
		super.dispose();
	}

	/**
	 * Save the state of this view, which is the history list
	 *
	 */
	public void saveState( IMemento state)
	{
		super.saveState( state);
		if ( _historyList==null)
			return;
		
		IMemento queryViewState=state.createChild( QUERY_VIEW_TYPE);
		int count=_historyList.getItemCount();
		String[] items=_historyList.getItems();
		queryViewState.putInteger( "count", count);
		for ( int i=0; i<count; ++i)
		{
			queryViewState.putString( "a"+Integer.toString( i), items[i]);
		}
	}
	
	public synchronized void init( IViewSite site, IMemento state)
	throws PartInitException
	{
		super.init( site, state);
		if ( state==null)
			return;
		try
		{
			IMemento queryViewState=state.getChild( QUERY_VIEW_TYPE);
			if ( queryViewState!=null)
			{
				Integer ic=queryViewState.getInteger( "count");
				if ( ic==null)
					throw new PartInitException("count not found");
				int count=ic.intValue();
				if ( count<0)
					throw new PartInitException( "count < 0");
				if ( count>0)
				{
					_savedState=new String[count];
					for ( int i=0; i<count; ++i)
					{
						_savedState[i]=queryViewState.getString( "a"+Integer.toString( i));
						if ( _savedState[i]==null)
							throw new PartInitException( "Text for "+i+" not found");
					}
					initializeHistoryList();
				}
			}
		}
		catch ( PartInitException pie)
		{
			Bbq_eclipsePlugin.getDefault().getLog().log( pie.getStatus());
			_savedState=null;
		}
	}
	
	void addText( String text)
	{
		_queryText.append( " "+text+" ");
	}

	private synchronized void initializeHistoryList()
	{
		if ( _savedState!=null && _historyList!=null)
		{
			for ( int i=0; i<_savedState.length; ++i)
				_historyList.add( _savedState[i]);
			_savedState=null;
		}
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
		manager.add( new Separator());
		manager.add(_selectAction);
		manager.add(_deleteAction);
		manager.add(new Separator());
		manager.add(_rebuildAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(_selectAction);
		manager.add(_deleteAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(_queryAction);
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
                AnalyzerQuery qp=Bbq_eclipsePlugin.getDefault().getQueryParser();
                qp.setLine( line);
                try
                {
                	SetExpression se=qp.getExpression();
                	NewSearchUI.runQueryInBackground( new Query( se, line));
                    _historyList.add( line, 0);
               }
                catch ( ParseException pe)
                {
                    MessageBox mb=new MessageBox( getSite().getShell(), SWT.ICON_ERROR|SWT.OK);
                    mb.setText( "Error parsing query");
                    mb.setMessage( pe.getMessage());
                    mb.open();
                }
         	}
        };
        _queryAction.setText( "Query");
        
        _rebuildAction=new Action( "Rebuild") {
        	public void run()
        	{
        		try
        		{
	        		PlatformUI.getWorkbench().getProgressService().
	        		   busyCursorWhile(new IRunnableWithProgress(){
	        		      public void run(IProgressMonitor monitor) {
	        		    	  try
	        		    	  {
	        		    		  BBQBuilder.buildWorkspace( monitor);
	        		    	  }
							catch ( CoreException ce)
							{
								handleError( ce);
							}
	        		      }
	        		   });
        		}
        		catch ( InvocationTargetException ite)
        		{
        			handleError( ite.getCause()==null ? ite : ite.getCause());
        		}
        		catch ( InterruptedException ie)
        		{
        			handleError( ie);
        		}
        	}
        	
        	public void handleError( Throwable e)
        	{
        		try
        		{
        			// Will this mean it gets logged twice?
        			Bbq_eclipsePlugin.getDefault().logError( e.getLocalizedMessage(), e);
        		}
        		catch ( CoreException ce)
        		{
        			// drop logged exception
        		}
        		displayException( "Error Rebuilding", e);
        	}
        };
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
		_historyList.addMouseListener( new MouseAdapter() {
			public void mouseDoubleClick( MouseEvent e)
			{
				_selectAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		_sashForm.setFocus();
	}
}