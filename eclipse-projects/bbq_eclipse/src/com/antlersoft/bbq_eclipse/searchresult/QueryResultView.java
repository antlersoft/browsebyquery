package com.antlersoft.bbq_eclipse.searchresult;

import java.io.*;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.part.Page;

import com.antlersoft.analyzer.SourceObject;
import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

public class QueryResultView extends Page implements ISearchResultPage {
	
	private String _id="com.antlersoft.bbq_eclipse.searchresult.QueryResultView";
	private TableViewer _viewer;
	private ISelection _selection;
	
	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#getID()
	 */
	public String getID() {
		return _id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#getLabel()
	 */
	public String getLabel() {
		return "BBQ Result";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#getUIState()
	 */
	public Object getUIState() {
		// TODO Auto-generated method stub
		return _id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#restoreState(org.eclipse.ui.IMemento)
	 */
	public void restoreState(IMemento memento) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento memento) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#setID(java.lang.String)
	 */
	public void setID(String id) {
		_id=id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#setInput(org.eclipse.search.ui.ISearchResult, java.lang.Object)
	 */
	public void setInput(ISearchResult search, Object uiState) {
		_viewer.setInput( search);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResultPage#setViewPart(org.eclipse.search.ui.ISearchResultViewPart)
	 */
	public void setViewPart(ISearchResultViewPart part) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		_viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		_viewer.setContentProvider(new ViewContentProvider());
		_viewer.setLabelProvider(new ViewLabelProvider());
		_viewer.setSorter(new NameSorter());
		_viewer.addSelectionChangedListener( new ISelectionChangedListener() {
			public void selectionChanged( SelectionChangedEvent evt)
			{
				_selection=evt.getSelection();
			}
		});
		_viewer.addDoubleClickListener( new IDoubleClickListener() {
			public void doubleClick( DoubleClickEvent evt) {
				openSelection();
			}
		});
		_viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#getControl()
	 */
	public Control getControl() {
		return _viewer.getControl();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#setFocus()
	 */
	public void setFocus() {
		_viewer.getControl().setFocus();
	}
	
	void openSelection()
	{
		if ( _selection!=null && ! _selection.isEmpty() && _selection instanceof IStructuredSelection)
		{
			Object selected=((IStructuredSelection)_selection).getFirstElement();
			if ( selected instanceof SourceObject)
			{
				try
				{
					SourceObject source=(SourceObject)selected;
					if ( source.getDBClass().getSourceFile()==null)
						return;
					IWorkspaceRoot root=ResourcesPlugin.getWorkspace().getRoot();
					IFile file=null;
					Path sourcePath=new Path( source.getDBClass().getSourceFile());
					try
					{
						file=root.getFile( sourcePath);
					}
					catch ( IllegalArgumentException iae)
					{
						IProject[] projects=root.getProjects();
						for ( int i=0; i<projects.length && file==null; ++i)
						{
							IJavaProject project=JavaCore.create( projects[i]);
							if ( project==null)
								continue;
							IPackageFragmentRoot[] fragments=project.getPackageFragmentRoots();
							for ( int j=0; j<fragments.length; ++j)
							{
								if ( fragments[j].getKind()==IPackageFragmentRoot.K_SOURCE)
								{
									try
									{
										file=root.getFile( fragments[j].getPath().append( sourcePath));
									}
									catch ( IllegalArgumentException ae)
									{
										continue;
									}
									if ( file.exists())
										break;
									else
										file=null;
								}
							}
						}
					}
					if ( file==null)
						return;
					InputStreamReader input=new InputStreamReader( file.getContents());
					int ch;
					int offset=0;
					try
					{
						for ( int currentLine=1; ( ch=input.read() )>0 && currentLine<source.getLineNumber(); offset++)
						{
							if ( ch=='\n')
								currentLine++;
						}
						input.close();
					}
					catch ( IOException ioe)
					{
						Bbq_eclipsePlugin.getDefault().logError( "IO Error finding linenumber", ioe);
					}
					JavaUI.revealInEditor( JavaUI.openInEditor( JavaCore.create( file)),
							new AnalyzerSourceReference( offset));
				}
				catch ( CoreException ce)
				{
					Bbq_eclipsePlugin.getDefault().getLog().log( ce.getStatus());
				}
			}
		}
	}
	
	static class AnalyzerSourceReference implements ISourceReference, ISourceRange
	{
		private int _offset;
		
		AnalyzerSourceReference( int offset) { _offset=offset; }
		
		public String getSource() { return ""; }
		public boolean exists() { return true; }
		public ISourceRange getSourceRange() { return this; }
		public int getOffset() { return _offset; }
		public int getLength() { return 0; }
 	}

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider, ISearchResultListener {
		ArrayList _pendingAdds=new ArrayList();
		
		public synchronized void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if ( oldInput!=null)
				((QueryResult)oldInput).removeListener( this);
			if ( newInput!=null)
				((QueryResult)newInput).addListener( this);
			_pendingAdds.clear();
		}
		public void dispose() {
		}
		public synchronized void searchResultChanged( SearchResultEvent evt)
		{
			ArrayList l=((QueryResult)evt.getSearchResult()).getResultItems();
			_pendingAdds.add( l.get( l.size()-1));
			if ( _pendingAdds.size()==1)
			{
				getSite().getShell().getDisplay().asyncExec( new Runnable() {
					public void run() {
						synchronized( ViewContentProvider.this) {
							if ( _pendingAdds.size()>0)
							{
								//_viewer.add( _pendingAdds.toArray());
								_pendingAdds.clear();
								_viewer.refresh();
							}
						}
					}
				});
			}
		}
		public Object[] getElements(Object parent) {
			QueryResult result=(QueryResult)parent;
			return result.getResultItems().toArray();
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			//return getImage(obj);
			return null;
		}
		/*
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
		*/
	}
	class NameSorter extends ViewerSorter {
	}
}
