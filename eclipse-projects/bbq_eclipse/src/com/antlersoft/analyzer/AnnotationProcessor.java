/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.ArrayList;

import com.antlersoft.bbq.db.DBAnnotatable;
import com.antlersoft.bbq.db.DBAnnotationBase;
import com.antlersoft.bbq.db.DBString;

import com.antlersoft.classwriter.*;

import com.antlersoft.odb.ObjectDB;

/**
 * Create or update database objects for annotation attributes in a classwriter
 * 
 * @author Michael A. MacDonald
 *
 */
class AnnotationProcessor<A extends DBAnnotatable & SourceObject> {
	ClassWriter cw;
	IndexAnalyzeDB db;
	AnnotationInitializerHolder initializer;
	A target;
	DBAnnotationBase.AnnotationUpdater updater;
	
	AnnotationProcessor( ClassWriter cw, IndexAnalyzeDB db, A annotated,
			AnnotationInitializerHolder initializer)
	{
		this.db=db;
		this.cw=cw;
		this.initializer=initializer;
		updater=null;
		target=annotated;
	}
	
	void addAnnotations( AnnotationInfo info, boolean hidden)
	throws Exception
	{
		ArrayList<String> strings=new ArrayList<String>();
		ArrayList<Annotation> annotations=new ArrayList<Annotation>();
		
		info.gatherAnnotationInfo(cw, annotations, strings);
		
		for ( Annotation a : annotations)
		{
			if ( updater==null )
				updater=new DBAnnotationBase.AnnotationUpdater(target);
			DBClass annotationClass=DBClass.getByInternalName( cw.getInternalClassName(a.getClassIndex()), db);
			DBAnnotation annotation=(DBAnnotation)updater.getExisting(annotationClass);
			if ( annotation!=null )
			{
				updater.addNew(new DBAnnotation<A>(annotationClass,target,hidden));
			}
			else
			{
				annotation.setIsHiddenAtRuntime(hidden);
			}
		}
		
		for ( String s : strings)
		{
			initializer.addString( s, target.getLineNumber());
		}
	}
	
	void done()
	{
		if ( updater!=null && updater.cleanup(db.getSession()))
		{
			ObjectDB.makeDirty(target);
		}
	}
	
	void processAttributeList( AttributeList attributes)
	throws Exception
	{
		AnnotationInfo attr=(AnnotationInfo)attributes.getAttributeByType( RuntimeVisibleAnnotationsAttribute.typeString);
		if ( attr!=null)
			addAnnotations( attr, false);
		attr=(AnnotationInfo)attributes.getAttributeByType( RuntimeInvisibleAnnotationsAttribute.typeString);
		if ( attr!=null)
			addAnnotations( attr, true);
		done();
	}
}
