package com.antlersoft.analyzecxx.db;

import com.antlersoft.odb.CompoundKey;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import java.util.Date;
import java.util.HashSet;

public class UnitEntry implements Persistent {
	private transient PersistentImpl _persistentImpl;
    public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
				_persistentImpl=new PersistentImpl();
		return _persistentImpl;
    }

	static final String UNIT_INDEX_NAME="UnitEntry.m_unit";
	static final String ENTRY_INDEX_NAME="UnitEntry.m_entry";
	static final String COMBINED_INDEX_NAME="UnitEntry.m_unit+m_entry";

	private ObjectRef m_unit, m_entry;
	private Date m_update_date;

	private UnitEntry(ObjectDB db, TranslationUnit unit, Persistent entry)
	{
		m_unit=new ObjectRef();
		m_entry=new ObjectRef();
		m_unit.setReferenced( unit);
		m_entry.setReferenced( entry);
		m_update_date=unit.getTranslationDate();
		db.makePersistent( this);
	}

	void clearIfObsolete(ObjectDB db, HashSet potentially_obsolete)
	{
		if ( ((TranslationUnit)m_unit.getReferenced()).getTranslationDate().
			compareTo( m_update_date)>0)
	    {
		    potentially_obsolete.add( m_entry);
		    db.deleteObject(this);
	    }
	}

	TranslationUnit getUnit()
	{
		return (TranslationUnit)m_unit.getReferenced();
	}

	Object getEntry()
	{
		return m_entry.getReferenced();
	}

	static void update( IndexObjectDB db, TranslationUnit unit, Persistent entry)
	{
		UnitEntry ue=(UnitEntry)db.findObject( COMBINED_INDEX_NAME, new
									CompoundKey( new ObjectRefKey( unit),
												 new ObjectRefKey( entry)));
		if ( ue==null)
			ue=new UnitEntry(db, unit, entry);
		else
		{
			ue.m_update_date=unit.getTranslationDate();
			db.makeDirty( ue);
		}
	}

	static class UnitGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object o)
		{
			return new ObjectRefKey( ((UnitEntry)o).m_unit);
		}
	}
	static class EntryGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object o)
		{
			return new ObjectRefKey( ((UnitEntry)o).m_entry);
		}
	}
	static class CombinedGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object o)
		{
			UnitEntry ue=(UnitEntry)o;
			return new CompoundKey( new ObjectRefKey( ue.m_unit),
									new ObjectRefKey( ue.m_entry));
		}
	}
}