/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.io.File;
import java.util.ArrayList;

import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectStoreException;
import com.antlersoft.odb.ObjectStreamCustomizer;
import com.antlersoft.odb.diralloc.CustomizerFactory;
import com.antlersoft.odb.diralloc.DirectoryAllocator;
import com.antlersoft.odb.diralloc.IndexStatistics;
import com.antlersoft.odb.schemastream.SchemaCustomizer;

import com.antlersoft.query.DataSource;

/**
 * The object database for IL analyzed objects
 * @author Michael A. MacDonald
 *
 */
public class ILDB extends IndexObjectDB implements DataSource {

	/**
	 * Create interface to ILDB stored in a directory
	 * @param db_directory Directory that contains the DirAlloc files for the database
	 */
	public ILDB(File db_directory) {
		super(new DirectoryAllocator(
				  db_directory, new CFactory()));
		// Create indices for the classes
		try
		{
			defineIndex( DBAssembly.ASSEMBLY_NAME_INDEX,
								   DBAssembly.class,
								   new StringKeyGenerator(),
								   false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBModule.MODULE_NAME_INDEX,
					DBModule.class,
					new DBModule.NameKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBNamespace.NAMESPACE_NAME_INDEX,
					DBNamespace.class,
					new StringKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBSourceFile.SOURCE_FILE_NAME_INDEX,
					DBSourceFile.class,
					new StringKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBClass.CLASS_KEY_INDEX,
					DBClass.class,
					new DBClass.ClassKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBClass.CLASS_BY_NAME_INDEX,
					DBClass.class,
					new StringKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBType.TYPE_KEY_INDEX,
					DBType.class,
					new DBType.TypeKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBType.TYPE_NAME_INDEX,
					DBType.class,
					new StringKeyGenerator(),
					false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBField.FIELD_TYPE_INDEX,
					DBField.class,
					new DBMember.MemberTypeGenerator(),
					false, false, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBMethod.METHOD_TYPE_INDEX,
					DBMethod.class,
					new DBMember.MemberTypeGenerator(),
					false, false, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBArgument.ARG_TYPE_INDEX,
					DBArgument.class,
					new DBArgument.ArgTypeGenerator(),
					false, false, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBString.STRING_INDEX,
								   DBString.class,
								   new StringKeyGenerator(),
								   false, true, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBStringReference.SRTARGET,
								   DBStringReference.class,
								   DBReference.ReferenceTargetGenerator.G,
								   false, false, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBFieldReference.FRTARGET,
								   DBFieldReference.class,
								   DBReference.ReferenceTargetGenerator.G,
								   false, false, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		try
		{
			defineIndex( DBCall.CALL_TARGET,
								   DBCall.class,
								   DBReference.ReferenceTargetGenerator.G,
								   false, false, null);
		}
		catch ( IndexExistsException iee)
		{
		}
		redefineIndex( DBBundle.BUNDLE_KEY_INDEX,
				DBBundle.class,
				new DBBundle.BundleKeyGenerator(),
				false, true, null);
		redefineIndex( DBStringResource.STRING_RESOURCE_VALUE_INDEX,
				DBStringResource.class,
				new DBStringResource.ValueKeyGenerator(),
				false, false, null);
		redefineIndex( DBStringResource.STRING_RESOURCE_NAME_INDEX,
				DBStringResource.class,
				new DBStringResource.NameKeyGenerator(),
				false, false, null);
	}
	
	public static ILDB clearDB( ILDB db, File dbFile )
	{
		try
		{
			db.close();
		}
		catch ( Exception e)
		{
			
		}
        if ( dbFile.exists())
        {
            File[] children=dbFile.listFiles();
            for ( int i=0; i<children.length; ++i)
            {
            	if ( children[i].isFile() && children[i].getName().indexOf('.')== -1)
            	{
            		if ( ! children[i].delete())
            			throw new ObjectStoreException( "Cleaning up failed to delete: "+children[i].getAbsolutePath());
            	}
            }
        }
        return new ILDB( dbFile);
	}
	
    /**
     * validates indices in this database
     * @param args The first argument should be the database directory
     */
    public static void validate( String[] args)
    throws Exception
    {
    	DirectoryAllocator store=new DirectoryAllocator( new File( args[0]),
                new CFactory());
    	store.validateIndex(null, true);
     }
    /**
     * Print the statistics for the indexes in this database
     * @param args The first argument should be the database directory
     */
    public static void main( String[] args)
    throws Exception
    {
    	DirectoryAllocator store=new DirectoryAllocator( new File( args[0]),
                new CFactory());
    	store.logAllStatistics();
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
			nameList.add( "com.antlersoft.odb.ObjectKeyHashMap");
			nameList.add( "com.antlersoft.odb.ObjectKeyHashSet");
			nameList.add( "com.antlersoft.odb.ObjectRef");
			nameList.add( "com.antlersoft.ilanalyze.db.DBSourceObject");
			nameList.add( "com.antlersoft.ilanalyze.db.DBMember");
			nameList.add( "com.antlersoft.ilanalyze.db.DBReference");
			nameList.add( toStore.getName());
			return new SchemaCustomizer( nameList);
		}
	}
}
