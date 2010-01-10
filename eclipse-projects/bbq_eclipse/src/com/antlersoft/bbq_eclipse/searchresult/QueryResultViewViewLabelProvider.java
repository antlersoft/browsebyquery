package com.antlersoft.bbq_eclipse.searchresult;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class QueryResultViewViewLabelProvider extends LabelProvider implements ITableLabelProvider {

  public QueryResultViewViewLabelProvider(QueryResultView queryResultView) {
  }

  public String getColumnText(Object obj, int index) {
    return getText(obj);
  }

  public Image getColumnImage(Object obj, int index) {
    return null;
  }
}