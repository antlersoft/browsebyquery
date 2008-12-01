/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.query;

import com.antlersoft.odb.IndexObjectDB;

import com.antlersoft.query.DataSource;

/**
 * A DataSource bases on an IndexObjectDB.
 * <p>
 * This interface provides a method for accessing the underlying IndexObjectDB.
 * @author Michael A. MacDonald
 *
 */
public interface IDBSource extends DataSource {
	public IndexObjectDB getSession();
}
