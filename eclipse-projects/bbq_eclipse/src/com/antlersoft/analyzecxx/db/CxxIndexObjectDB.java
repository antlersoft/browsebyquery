package com.antlersoft.analyzecxx.db;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.antlersoft.analyzecxx.DBDriver;

import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.IndexIterator;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.ObjectStreamCustomizer;
import com.antlersoft.odb.Persistent;

import com.antlersoft.odb.diralloc.DirectoryAllocator;
import com.antlersoft.odb.diralloc.CustomizerFactory;

import com.antlersoft.odb.schemastream.SchemaCustomizer;

public class CxxIndexObjectDB implements DBDriver {
	IndexObjectDB m_session;
	private TranslationUnit m_unit;
	private SourceFile m_current_file;
	private int m_line;

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
			m_session.defineIndex( IncludeFile.INCLUDE_FILE_INDEX,
								   IncludeFile.class,
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
		try
		{
			m_session.defineIndex(UnitEntry.COMBINED_INDEX_NAME,
								  UnitEntry.class,
								  new UnitEntry.CombinedGenerator(),
								  false, true);
		}
		catch (IndexExistsException iee) {
			// Don't care if index exists
		}
	}

    public void startTranslationUnit(String file_name) {
		m_unit=TranslationUnit.get( file_name, m_session);
		m_unit.translate();
		m_current_file=m_unit;
		m_line=1;
    }

    public void finishTranslationUnit() {
		ArrayList entries=new ArrayList();
		for ( IndexIterator ii=m_session.greaterThanOrEqualEntries(
				  UnitEntry.UNIT_INDEX_NAME,
				  new ObjectRefKey( m_unit));
			  ii.hasNext();)
			entries.add( ii.next());
		HashSet possible_clear=new HashSet();
		for ( Iterator i=entries.iterator(); i.hasNext();)
			((UnitEntry)i.next()).clearIfObsolete( possible_clear);
		entries=null;
		for ( Iterator i=possible_clear.iterator(); i.hasNext();)
		{
			Persistent p=(Persistent)((ObjectRef)i.next()).getReferenced();
			if ( ! m_session.greaterThanOrEqualEntries( UnitEntry.ENTRY_INDEX_NAME,
				new ObjectRefKey(p)).isExactMatch())
				m_session.deleteObject( p);
		}
		m_session.commit();
    }

    public void includeFile(String included) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method includeFile() not yet implemented.");
    }
    public void setCurrentFile(String file) {
		if ( file.equals( m_unit.getName()))
		{
			m_current_file=m_unit;
		}
		else
		{
			m_current_file=IncludeFile.get( m_session, file);
			UnitEntry.update( m_session, m_unit, m_current_file);
		}
    }
	public String getCurrentFile() {
		if ( m_current_file!=null)
			return m_current_file.getName();
		return "";
	}
	public void setCurrentLine( int line) { m_line=line; }
	public int getCurrentLine() { return m_line; }
    public void defineMacro(String name, boolean is_function) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method defineMacro() not yet implemented.");
    }
    public void undefineMacro(String name) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method undefineMacro() not yet implemented.");
    }
    public void checkForMacroDefinition(String name) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method checkForMacroDefinition() not yet implemented.");
    }
    public void expandMacro(String name, boolean is_function) {
	/**@todo Implement this com.antlersoft.analyzecxx.DBDriver method*/
	throw new java.lang.UnsupportedOperationException("Method expandMacro() not yet implemented.");
    }

	public void preprocessorConditional( boolean success) {

	}

	void updateUnitEntry( Persistent entry)
	{
		UnitEntry.update( m_session, m_unit, entry);
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
			nameList.add( "com.antlersoft.odb.diralloc.UniqueKey");
			nameList.add( "com.antlersoft.analyzecxx.db.SourceFile");
			nameList.add( "com.antlersoft.analyzecxx.db.SourceObject");
			nameList.add( toStore.getName());
			return new SchemaCustomizer( nameList);
		}
	}
}