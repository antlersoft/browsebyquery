		return _persistentImpl;
    }

	static final String UNIT_INDEX_NAME="UnitEntry.m_unit";
	static final String ENTRY_INDEX_NAME="UnitEntry.m_entry";
	static final String COMBINED_INDEX_NAME="UnitEntry.m_unit+m_entry";

	private ObjectRef m_unit, m_entry;
	private Date m_update_date;

	private UnitEntry( TranslationUnit unit, Persistent entry)
	{
		m_unit=new ObjectRef();
		m_entry=new ObjectRef();
		m_unit.setReferenced( unit);
		m_entry.setReferenced( entry);
		m_update_date=unit.getTranslationDate();
		ObjectDB.makePersistent( this);
	}

	void clearIfObsolete( HashSet potentially_obsolete)
	{
		if ( ((TranslationUnit)m_unit.getReferenced()).getTranslationDate().
			compareTo( m_update_date)>0)
	    {
		    potentially_obsolete.add( m_entry);
		    ObjectDB.getObjectDB().deleteObject(this);
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
			ue=new UnitEntry( unit, entry);
		else
		{
			ue.m_update_date=unit.getTranslationDate();
			ObjectDB.makeDirty( ue);
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