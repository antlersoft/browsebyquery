/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.odb.diralloc;

/**
 * @author Michael A. MacDonald
 *
 */
public class IndexStatistics {

	public class Counts
	{
		int m_pages;
		int m_totalKeys;
		int m_totalSize;
		
		public String toString()
		{ return "Pages: "+m_pages+" Keys: "+m_totalKeys+" Size: "+m_totalSize; }
	}
	
	int m_entriesPerPage;
	Counts m_regular;
	Counts m_overflow;
	
	public IndexStatistics()
	{
		m_regular=new Counts();
		m_overflow=new Counts();
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
     	sb.append( " Entries per page: ");
		sb.append( getEntriesPerPage());
		sb.append( "\r\nRegular--");
    	sb.append( getRegular().toString());
    	sb.append( "\r\nOverflow--");
    	sb.append(getOverflow().toString());
    	
    	return sb.toString();
	}
	
	public int getEntriesPerPage() { return m_entriesPerPage; }
	public final Counts getRegular() { return m_regular; }
	public final Counts getOverflow() { return m_overflow; }
}
