/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.HashSet;

import com.antlersoft.bbq.db.DBString;

import com.antlersoft.odb.ObjectDB;

/**
 * A class containing annotations has an annotation initializer, a fake method
 * to hold string references referenced within the annotations.
 * 
 * An object of this class is created during annotation processing of a class, and is used
 * to create and update the fake method only as needed.
 * 
 * @author Michael A. MacDonald
 *
 */
class AnnotationInitializerHolder {
	DBClass containing;
	DBMethod initializer;
	HashSet<DBMethod> foundMethods;
	DBMethod.ReferenceUpdater<DBStringReference> stringList;
	IndexAnalyzeDB db;
	
	AnnotationInitializerHolder( IndexAnalyzeDB db, DBClass containing, HashSet<DBMethod> foundMethods)
	{
		this.db=db;
		this.containing=containing;
		this.foundMethods=foundMethods;
	}
	
	DBMethod.ReferenceUpdater<DBStringReference> getStringList()
		throws Exception
	{
		if ( initializer==null)
		{
			initializer=containing.getOrCreateMethod(db, "<attrinit?>", "()V");
			foundMethods.add(initializer);
		}
		if ( stringList==null)
			stringList=new DBMethod.ReferenceUpdater<DBStringReference>(initializer.stringReferences);
		return stringList;
	}
	
	void addString( String s, int lineNumber)
	throws Exception
	{
		DBString dbs=DBString.get(db.getSession(), s);
		if ( ! getStringList().existsReference( dbs, lineNumber))
		{
			getStringList().addReference( new DBStringReference(initializer,dbs,lineNumber));
		}
	}
	
	/**
	 * Call when all annotations are processed
	 *
	 */
	void finish()
	{
		if ( stringList!=null )
		{
			if ( stringList.cleanup(db))
			{
				initializer.stringReferences=stringList.afterList;
				ObjectDB.makeDirty(initializer);
			}
		}
	}
}
