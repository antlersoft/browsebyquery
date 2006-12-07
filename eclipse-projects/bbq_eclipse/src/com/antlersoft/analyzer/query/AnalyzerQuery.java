/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import java.util.TreeSet;

import com.antlersoft.query.environment.QueryLanguageEnvironment;

/**
 * @author Michael A. MacDonald
 *
 */
public class AnalyzerQuery extends QueryLanguageEnvironment {
	public AnalyzerQuery()
	{
		super( new QueryParser());
        importedPackages=new TreeSet();
	}

    // Package interface
    TreeSet importedPackages;
    
}
