/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import com.antlersoft.bbq.db.DBAnnotatable;
import com.antlersoft.bbq.db.DBAnnotationBase;
import com.antlersoft.bbq.db.DBClassBase;

import com.antlersoft.odb.ObjectDB;

/**
 * An annotation defined on a Java object in the analyzed system
 * @param A Type of object that is being annotated
 * @author Michael A. MacDonald
 *
 */
public class DBAnnotation<A extends DBAnnotatable & SourceObject> extends DBAnnotationBase implements SourceObject {
	
	private boolean hiddenAtRuntime;
	
	protected DBAnnotation( DBClassBase annotationClass, A annotated, boolean hidden)
	{
		super( annotationClass, annotated);
		hiddenAtRuntime=hidden;
		
		ObjectDB.makePersistent(this);
	}
	
	public boolean isHiddenAtRuntime() { return hiddenAtRuntime; }
	
	public void setIsHiddenAtRuntime(boolean hidden) {
		if ( hiddenAtRuntime!=hidden)
		{
			hiddenAtRuntime=hidden;
			ObjectDB.makeDirty(this);
		}
	}
	
	@Override
	public A getAnnotatedObject()
	{
		return (A)super.getAnnotatedObject();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.SourceObject#getDBClass()
	 */
	public DBClass getDBClass() {
		return getAnnotatedObject().getDBClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzer.SourceObject#getLineNumber()
	 */
	public int getLineNumber() {
		return getAnnotatedObject().getLineNumber();
	}
}