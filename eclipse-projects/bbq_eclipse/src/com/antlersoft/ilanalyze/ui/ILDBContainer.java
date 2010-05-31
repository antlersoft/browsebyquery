/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.ui;

import java.io.File;
import java.util.Date;
import java.util.TreeMap;

import com.antlersoft.bbq.query.IDBSource;
import com.antlersoft.ilanalyze.db.ILDB;
import com.antlersoft.ilanalyze.db.ILDBDriver;
import com.antlersoft.ilanalyze.parseildasm.IldasmReader;
import com.antlersoft.query.environment.ui.AbstractDBContainer;

/**
 * @author Michael A. MacDonald
 *
 */
public class ILDBContainer extends AbstractDBContainer {
	private ILDB analyzerDB;
	
	ILDBContainer()
	{
		super("ILDB020");
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.AbstractDBContainer#internalOpen(java.io.File)
	 */
	@Override
	protected IDBSource internalOpen(File f) throws Exception {
		analyzerDB=new ILDB(f, isUseMapped());
		return analyzerDB;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.ui.AbstractDBContainer#analyze(java.io.File[])
	 */
	@Override
	public void analyze(File[] selectedFiles) throws Exception {
	   	ILDBDriver driver=new ILDBDriver(analyzerDB);
	   	IldasmReader reader=new IldasmReader();
	   	Object last_scanned=analyzerDB.getRootObject("LAST_SCANNED");
	   	TreeMap<String,Date> oldest;
	   	if ( last_scanned==null || ! (last_scanned instanceof TreeMap))
	   		oldest=new TreeMap<String,Date>();
	   	else
	   		oldest=(TreeMap<String,Date>)last_scanned;
	   	reader.setOldestFileDate(oldest);

		for ( int i=0; i<selectedFiles.length; i++)
		{
		   reader.sendFileToDriver(selectedFiles[i], driver);
		}
       
		analyzerDB.makeRootObject("LAST_SCANNED", oldest);
		analyzerDB.commitAndRetain();
	}	
}
