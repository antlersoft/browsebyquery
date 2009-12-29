/**
 * Copyright (c) 2009 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.bbq.db.AnnotationBase;
import com.antlersoft.bbq.db.DBAnnotatable;
import com.antlersoft.bbq.db.DBAnnotationBase;
import com.antlersoft.bbq.db.DBClassBase;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBAnnotation extends DBSourceObject implements AnnotationBase {
	
	private DBAnnotationBase m_base;

	/**
	 * @param annotationClass
	 * @param annotated
	 */
	public DBAnnotation(DBClassBase annotationClass, DBAnnotatable annotated) {
		m_base = new DBAnnotationBase(annotationClass, annotated);
		ObjectDB.makePersistent(this);
	}

	/**
	 * @return
	 * @see com.antlersoft.bbq.db.DBAnnotationBase#getAnnotatedObject()
	 */
	public DBAnnotatable getAnnotatedObject() {
		return m_base.getAnnotatedObject();
	}

	/**
	 * @return
	 * @see com.antlersoft.bbq.db.DBAnnotationBase#getAnnotationClass()
	 */
	public DBClassBase getAnnotationClass() {
		return m_base.getAnnotationClass();
	}

	/**
	 * @return
	 * @see com.antlersoft.bbq.db.DBAnnotationBase#getAnnotationClassRef()
	 */
	public ObjectRef<DBClassBase> getAnnotationClassRef() {
		return m_base.getAnnotationClassRef();
	}

	/**
	 * @return
	 * @see com.antlersoft.bbq.db.DBAnnotationBase#toString()
	 */
	public String toString() {
		return m_base.toString() + getPositionString();
	}

	
	static class AnnotationClassKeyGenerator implements KeyGenerator {

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey((DBClass)((DBAnnotation)o1).getAnnotationClass());
		}
		
	}
}
