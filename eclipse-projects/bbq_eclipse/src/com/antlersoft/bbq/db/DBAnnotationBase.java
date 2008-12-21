/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import java.util.ArrayList;
import java.util.Enumeration;

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectKeyHashMap;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.query.EmptyEnum;
import com.antlersoft.query.SingleEnum;

/**
 * Base class for annotations-- data added to an element in the analyzed system
 * that is available on reflection.
 * 
 * An annotation is a relationship between the annotated element and the
 * class of the annotation type in the analyzed system.
 * 
 * @param C Type of object representing a class in the analyzed system
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBAnnotationBase implements Persistent {

	public final static String ANNOTATION_CLASS="ANNOTATION_CLASS";

	private transient PersistentImpl _persistentImpl;
	
	private ObjectRef<DBClassBase> annotationClass;
	private ObjectRef<DBAnnotatable> annotatedObject;
	
	protected DBAnnotationBase( DBClassBase annotationClass, DBAnnotatable annotated)
	{
		this.annotationClass=new ObjectRef<DBClassBase>(annotationClass);
		this.annotatedObject=new ObjectRef<DBAnnotatable>(annotated);
	}
	
	public DBClassBase getAnnotationClass()
	{
		return annotationClass.getReferenced();
	}
	
	public DBAnnotatable getAnnotatedObject()
	{
		return annotatedObject.getReferenced();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		sb.append(annotationClass.getReferenced().toString());
		sb.append( " annotation of ");
		sb.append( annotatedObject.getReferenced().toString());
		
		return sb.toString();
	}

	public static Enumeration getAnnotationsWithType( IndexObjectDB db, Persistent annotationClass)
	{
		return new ExactMatchIndexEnumeration(
				db.greaterThanOrEqualEntries(ANNOTATION_CLASS, new ObjectRefKey(annotationClass)));
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
		{
			_persistentImpl = new PersistentImpl();
		}
		return _persistentImpl;
	}
	
	public static class AnnotationUpdater
	{
		/** Map of annotation classes to values in the current annotated object */
		private ObjectKeyHashMap<DBClassBase,DBAnnotationBase> existing;
		private ArrayList<DBAnnotationBase> after;
		boolean changed;
		private AnnotationCollection collection;
		
		private ArrayList<DBAnnotationBase> getAfter()
		{
			if ( after==null)
				after=new ArrayList<DBAnnotationBase>();
			return after;
		}
		
		public AnnotationUpdater( DBAnnotatable target)
		{
			after=null;
			changed=false;
			collection=target.getAnnotationCollection();
			Enumeration e=collection.getAnnotations();
			if ( e.hasMoreElements())
			{
				existing=new ObjectKeyHashMap<DBClassBase,DBAnnotationBase>();
				while ( e.hasMoreElements())
				{
					DBAnnotationBase annotation=(DBAnnotationBase)e.nextElement();
					existing.put(annotation.annotationClass, annotation);
				}
			}
		}
		
		public DBAnnotationBase getExisting(DBClassBase annotationclass)
		{
			DBAnnotationBase result = null;
			
			if ( existing!=null)
				result=existing.get(annotationclass);
			
			if ( result!=null)
			{
				existing.remove( annotationclass);
				getAfter().add( result);
			}
			
			return result;
		}
		
		/**
		 * Add a newly created annotation to the collection
		 * @param newannotation
		 */
		public void addNew(DBAnnotationBase newannotation)
		{
			changed=true;
			getAfter().add( newannotation);
		}
		
		public boolean cleanup(IndexObjectDB db)
		{
			if ( existing!=null && existing.size()>0 )
			{
				changed=true;
				for ( DBAnnotationBase i : existing.values())
				{
					db.deleteObject(i);
				}
			}
			if ( changed )
			{
				collection.annotations.clear();
				for ( DBAnnotationBase ann : after)
				{
					collection.annotations.add(new ObjectRef<DBAnnotationBase>(ann));
				}
			}
				
			return changed;
		}
	}
	
	/**
	 * @author Michael A. MacDonald
	 *
	 */
	public static class AnnotationClassKeyGenerator implements KeyGenerator {

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey(((DBAnnotationBase)o1).annotationClass);
		}
	}
}
