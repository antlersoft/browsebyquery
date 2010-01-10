package com.antlersoft.bbq_eclipse.searchresult;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.part.Page;


public class QueryResultView extends Page implements ISearchResultPage {

  private String     _id = "com.antlersoft.bbq_eclipse.searchresult.QueryResultView";
  public TableViewer _viewer;
  ISelection         _selection;
  private IAction    _selectAction;

  public String getID() {

    return _id;
  }

  public String getLabel() {

    return "BBQ Result";
  }

  public Object getUIState() {

    // TODO Auto-generated method stub
    return _id;
  }

  public void restoreState(IMemento memento) {

  }

  public void saveState(IMemento memento) {

    // TODO Auto-generated method stub
  }

  public void setID(String id) {

    _id = id;
  }

  public void setInput(ISearchResult search, Object uiState) {

    _viewer.setInput(search);
  }

  public void setViewPart(ISearchResultViewPart part) {
  }

  public void createControl(Composite parent) {

    _viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
    _viewer.setContentProvider(new QueryResultViewContentProvider(this));
    _viewer.setLabelProvider(new QueryResultViewViewLabelProvider(this));
    _viewer.setSorter(new QueryResultViewNameSorter(this));
    _viewer.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent evt) {
        _selection = evt.getSelection();
      }
    });
    _selectAction = new QueryResultViewSelectAction(this);
    MenuManager manager = new MenuManager("#PopupResult");
    manager.add(_selectAction);
    _viewer.getTable().setMenu(manager.createContextMenu(_viewer.getTable()));
    _viewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent evt) {
        _selectAction.run();
      }
    });
    _viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

  }

  public Control getControl() {

    return _viewer.getControl();
  }

  public void setFocus() {

    _viewer.getControl().setFocus();
  }
}
