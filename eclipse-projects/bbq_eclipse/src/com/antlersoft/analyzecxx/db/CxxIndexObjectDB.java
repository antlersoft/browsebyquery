package com.antlersoft.analyzecxx.db;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;

import com.antlersoft.analyzecxx.DBDriver;

import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.IndexIterator;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.ObjectStreamCustomizer;
import com.antlersoft.odb.Persistent;

import com.antlersoft.odb.diralloc.DirectoryAllocator;
import com.antlersoft.odb.diralloc.CustomizerFactory;

import com.antlersoft.odb.schemastream.SchemaCustomizer;

public class CxxIndexObjectDB implements DBDriver {
	private IndexObjectDB m_session;
	private TranslationUnit m_unit;
	private SourceFile m_current_file;
	private ArrayList m_inprocess_entries;

	public CxxIndexObjectDB( String directory_name)
	{
		m_session=new IndexObjectDB( new DirectoryAllocator(
				  new File( directory_name), new CFactory()));
		try
		{
			m_session.defineIndex( TranslationUnit.TRANSLATION_UNIT_NAME_INDEX,
								   TranslationUnit.class,
								   new SourceFile.FileKeyGenerator(),
								   false, true);
		}
		catch ( IndexExistsException iee)
		{
			// Don't care if index exists
		}
		try
		{
			m_session.defineIndex( UnitEntry.ENTRY_INDEX_NAME,
								   UnitEntry.class,
								   new UnitEntry.EntryGenerator(),
								   false, false);
		}
		catch ( IndexExistsException iee)
		{
			// Don't care if index exists
		}
		try
		{
			m_session.defineIndex( UnitEntry.UNIT_INDEX_NAME,
								   UnitEntry.class,
								   new UnitEntry.UnitGenerator(),
								   false, false);
		}
		catch ( IndexExistsException iee)
		{
			// Don't care if index exists
		}
	}

    public void startTranslationUnit(String file_name) {
		m_unit=TranslationUnit.get( file_name, m_session);
		m_unit.translate();
		m_current_file=m_unit;
		ArrayList unit_entries=new ArrayList();
		m_inprocess_entries=new ArrayList();
		for ( IndexIterator ii=m_session.greaterThanOrEqualEntries( UnitEntry.UNIT_INDEX_NAME,
			new ObjectRefKey( m_unit));
			ii.isExactMatch() && ii.hasNext();)
	    {
			UnitEntry unit_entry=(UnitEntry)ii.next();
		    if ( unit_entry.getUnit()==m_unit)
			{
				unit_entries.add( unit_entry);
				m_inprocess_entries.add( unit_entry.getEntry());
			}
			else
				break;
		}
		for ( Iterator i=unit_entries.iterator(); i.hasNext();)
			m_session.deleteObject( i.next());
    }

    public void finishTranslationUnit() {
		for ( Iterator i=m_inprocess_entries.iterator(); i.hasNext();)
		{
			Persistent p=(Persistent)i.next();
			if ( ! m_session.greaterThanOrEqualEntries( UnitEntry.ENTRY_INDEX_NAME,
				new ObjectRefKey( p)).isExactMatch())
				m_session.deleteObject( p);
		}
		m_session.commit();
    }

    public void includeFile(String included, int from_line) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method includeFile() not yet implemented.");
    }
    public void setCurrentFile(String file) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method setCurrentFile() not yet implemented.");
    }
    public void defineMacro(String name, boolean is_function, int from_line) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method defineMacro() not yet implemented.");
    }
    public void undefineMacro(String name, int from_line) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method undefineMacro() not yet implemented.");
    }
    public void checkForMacroDefinition(String name, int from_line) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method checkForMacroDefinition() not yet implemented.");
    }
    public void expandMacro(String name, boolean is_function, int from_line) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method expandMacro() not yet implemented.");
    }

	void CreateUnitEntry( Persistent entry)
	{
		new UnitEntry( m_unit, entry);
	}

	static class CFactory extends CustomizerFactory
	{
		public ObjectStreamCustomizer getCustomizer( Class toStore)
		{
			ArrayList nameList=new ArrayList();
			nameList.add( "java.lang.Comparable");
			nameList.add( "[Ljava.lang.Comparable;");
			nameList.add( "[I");
			nameList.add( "com.antlersoft.odb.diralloc.DAKey");
			nameList.add( toStore.getName());
			return new SchemaCustomizer( nameList);
		}
	}
}