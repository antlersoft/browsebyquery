package com.antlersoft.analyzecxx.db;

import com.antlersoft.odb.CompoundKey;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;

public class FileInclusion extends SourceObject {
	private ObjectRef m_included_file;
	static final String UNIQUE_KEY="FileInclusion.uk";

	private FileInclusion( IncludeFile included, SourceFile from, int line)
	{
		super( from, line);
		m_included_file=new ObjectRef( included);
		ObjectDB.makePersistent( this);
	}

	public IncludeFile getIncluded()
	{
		return (IncludeFile)m_included_file.getReferenced();
	}

	public static FileInclusion get( IndexObjectDB db, IncludeFile included,
									 SourceFile from, int line)
	{
		FileInclusion fi=(FileInclusion)db.findObject( UNIQUE_KEY,
			new CompoundKey( new ObjectRefKey( included),
							 new ObjectRefKey( from)));
		if ( fi==null)
			fi=new FileInclusion( included, from, line);
		else
		{
			if ( fi.m_line!=line)
			{
				fi.m_line = line;
				ObjectDB.makeDirty(fi);
			}
		}
		return fi;
	}

	static class InclusionKeyGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object o)
		{
			FileInclusion fi=(FileInclusion)o;
			return new CompoundKey(
									new ObjectRefKey( fi.m_included_file),
									new ObjectRefKey( fi.m_file));
		}
	}
}