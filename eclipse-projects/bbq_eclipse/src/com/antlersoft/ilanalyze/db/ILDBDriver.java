/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.antlersoft.bbq.db.DBAnnotatable;
import com.antlersoft.bbq.db.DBAnnotationBase;
import com.antlersoft.bbq.db.DBBundleBase;
import com.antlersoft.bbq.db.DBPackage;
import com.antlersoft.bbq.db.DBString;

import com.antlersoft.ilanalyze.BuiltinType;
import com.antlersoft.ilanalyze.CustomAttributeSetting;
import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.LoggingDBDriver;
import com.antlersoft.ilanalyze.ReadType;
import com.antlersoft.ilanalyze.Signature;
import com.antlersoft.ilanalyze.ReadArg;

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.ObjectRef;

/**
 * Adds data read from assembly files to the database
 * @author Michael A. MacDonald
 *
 */
public class ILDBDriver implements DBDriver {
	
	private ILDB m_db;
	private DBAssembly m_current_assembly;
	private DBModule m_current_module;
	private DBSourceFile m_current_source_file;
	private int m_current_line;
	/** List representing current stack of DBNamespace objects */
	private ArrayList<DBPackage> m_namespace_stack;
	/** List representing current stack of DBClass objects */
	private ArrayList<ClassUpdater> m_class_stack;
	/** Class that manages updating the contents of a method */
	private DBMethod.MethodUpdater m_method_updater;
	/** How many classes processed since last commit */
	private int m_class_count;
	
	/** Updates the current bundle */
	private DBBundle.BundleUpdater m_bundle_updater;
	
	/** Custom attributes applicable to the next declaration */
	private ArrayList<CustomAttributeSetting> m_custom_attributes;
	
	private final static String ATTRIBUTE_INITIALIZER=".attributeinitializer";
	
	public ILDBDriver( ILDB db)
	{
		m_db=db;
		m_namespace_stack=new ArrayList<DBPackage>();
		m_class_stack=new ArrayList<ClassUpdater>();
		m_class_count=0;
		m_custom_attributes = new ArrayList<CustomAttributeSetting>();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addFieldReference(com.antlersoft.ilanalyze.ReadType, com.antlersoft.ilanalyze.ReadType, java.lang.String, boolean)
	 */
	public void addFieldReference(ReadType containing_type,
			ReadType field_type, String name, boolean is_write) {
		if ( m_method_updater!=null)
		{
			try
			{
				m_method_updater.addFieldReference(getCurrentClass(containing_type).getField(name, DBType.get(m_db, field_type)), is_write, m_current_source_file, m_current_line);
			}
			catch ( ITypeInterpreter.TIException ti)
			{
				LoggingDBDriver.logger.info(ti.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addMethodCall(com.antlersoft.ilanalyze.ReadType, java.lang.String, java.util.List, com.antlersoft.ilanalyze.Signature)
	 */
	public void addMethodCall(ReadType containing_type, String method_name,
			List genericArgs, Signature sig) {
		if ( m_method_updater!=null)
		{
			try
			{
				DBMethod called=getCurrentClass(containing_type).getMethod(method_name, DBType.get(m_db, sig.getReturnType()), getSignatureKey( sig));
				if ( called.updateArguments(m_db, sig))
					ObjectDB.makeDirty( called);
				m_method_updater.addCall(called, m_current_source_file, m_current_line);
			}
			catch ( ITypeInterpreter.TIException ti)
			{
				LoggingDBDriver.logger.info(ti.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addStringReference(java.lang.String)
	 */
	public void addStringReference(String referenced) {
		if ( m_method_updater!=null)
		{
			m_method_updater.addStringReference(DBString.get(m_db, referenced), m_current_source_file, m_current_line);
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addCatch(com.antlersoft.ilanalyze.ReadType)
	 */
	public void addCatch(ReadType caught) {
		if ( m_method_updater!=null)
		{
			try
			{
				m_method_updater.addCatch( getCurrentClass(caught), m_current_source_file, m_current_line);
			}
			catch ( ITypeInterpreter.TIException tie)
			{
				LoggingDBDriver.logger.info(tie.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endClass()
	 */
	public void endClass() {
		ClassUpdater updater=(ClassUpdater)m_class_stack.get(m_class_stack.size()-1);
		if ( updater.m_updater!=null)
		{
			updater.m_updater.methodDone();
		}
		m_class_stack.remove( m_class_stack.size()-1);
		if ( ++m_class_count>1000)
		{
			m_db.commitAndRetain();
			m_class_count=0;
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endMethod()
	 */
	public void endMethod() {
		if ( m_method_updater!=null)
		{
			m_method_updater.methodDone();
			m_method_updater=null;
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endNamespace()
	 */
	public void endNamespace() {
		m_namespace_stack.remove( m_namespace_stack.size()-1);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#setFileAndLine(java.lang.String, int)
	 */
	public void setFileAndLine(String name, int line) {
		if ( name!=null && name.length()>0)
		{
			m_current_source_file=DBSourceFile.get(m_db, name);
		}
		m_current_line=line;
		if ( m_class_stack.size()>0)
		{
			((ClassUpdater)m_class_stack.get(m_class_stack.size()-1)).m_class.setFileAndLine(m_current_source_file, line);
		}
		if ( m_method_updater!=null)
			m_method_updater.m_method.setFileAndLine(m_current_source_file, line);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAssembly(java.lang.String)
	 */
	public void startAssembly(String name) {
		m_current_assembly=DBAssembly.get( m_db, name);
		m_current_source_file=null;
		m_current_line=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startClass(java.lang.String, java.util.List, int, com.antlersoft.ilanalyze.ReadType, java.util.List)
	 */
	public void startClass(String className, List genericParams,
			int properties, ReadType extendsType, List implementsList) {
		DBClass read_class=DBClass.get(m_db, className);
		read_class.setProperties(properties);
		read_class.setVisited( true);
		applyCurrentAttributes(read_class);
		ObjectKeyHashSet base=new ObjectKeyHashSet();
		try
		{
			base.add( new ObjectRef( DBClass.get( m_db, ITypeInterpreter.Factory.getClassKey(extendsType))));
			for ( Iterator i=implementsList.iterator(); i.hasNext();)
			{
				base.add( new ObjectRef( DBClass.get( m_db, ITypeInterpreter.Factory.getClassKey((ReadType)i.next()))));
			}
		}
		catch ( ITypeInterpreter.TIException ti)
		{
			LoggingDBDriver.logger.info( ti.getMessage());
		}
		read_class.updateBaseClasses(base);
		read_class.setAssembly(m_current_assembly);
		read_class.setModule(m_current_module);
		m_class_stack.add( new ClassUpdater( read_class));
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startMethod(java.lang.String, java.util.List, com.antlersoft.ilanalyze.Signature, int)
	 */
	public void startMethod(String name, List genericParams,
			Signature signature, int properties) {
		assert( m_method_updater==null);
		try
		{
			m_method_updater=new DBMethod.MethodUpdater( m_db, getCurrentClass( null).getMethod( name, DBType.get( m_db, signature.getReturnType()), getSignatureKey( signature)));
			m_method_updater.updateArguments(signature);
			m_method_updater.m_method.setProperties(properties);
			applyCurrentAttributes(m_method_updater.m_method);
		}
		catch ( ITypeInterpreter.TIException ti)
		{
			LoggingDBDriver.logger.info( ti.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startModule(java.lang.String)
	 */
	public void startModule(String name) {
		m_current_module=DBModule.get(m_db, name);
		m_current_module.setAssembly( m_current_assembly);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startNamespace(java.lang.String)
	 */
	public void startNamespace(String name) {
		DBPackage namespace=DBPackage.get( name, m_db);
		m_namespace_stack.add( namespace);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addField(java.lang.String, com.antlersoft.ilanalyze.ReadType, int)
	 */
	public void addField(String name, ReadType type, int properties) {
		try
		{
			DBField field=getCurrentClass(null).getField( name, DBType.get( m_db, type));
			field.setProperties( properties);
			field.setFileAndLine(m_current_source_file, m_current_line);
			applyCurrentAttributes(field);
		}
		catch ( ITypeInterpreter.TIException ti)
		{
			LoggingDBDriver.logger.info(ti.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endAnalyzedFile()
	 */
	public void endAnalyzedFile() {
		m_db.commitAndRetain();
		m_class_count=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAnalyzedFile(java.lang.String)
	 */
	public void startAnalyzedFile(String file) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addCustomAttribute(com.antlersoft.ilanalyze.ReadType, com.antlersoft.ilanalyze.Signature, byte[])
	 */
	public void addCustomAttribute(CustomAttributeSetting custom) {
		m_custom_attributes.add(custom);
	}
	
	private void applyCurrentAttributes(DBAnnotatable annotatable)
	{
		DBAnnotationBase.AnnotationUpdater annotation_updater = new DBAnnotationBase.AnnotationUpdater(annotatable);
		for (CustomAttributeSetting custom : m_custom_attributes)
		{
			try
			{
				DBClass annotation_class = getCurrentClass(custom.getContainingType());
				DBAnnotation annotation = (DBAnnotation)annotation_updater.getExisting(annotation_class);
				if (annotation==null)
				{
					annotation = new DBAnnotation(annotation_class, annotatable);
				}
				annotation.setFileAndLine(((DBSourceObject)annotatable).getSourceFile(), ((DBSourceObject)annotatable).getLineNumber());
				if ( m_class_stack.size()>0)
				{
					ReadType void_read_type=new BuiltinType("void");
					DBType void_type=DBType.get( m_db, void_read_type);
					DBMethod.MethodUpdater attrUpdater=m_method_updater;
					if ( attrUpdater==null)
					{
						ClassUpdater u=(ClassUpdater)m_class_stack.get( m_class_stack.size()-1);
						if ( u.m_updater==null)
						{
							u.m_updater=new DBMethod.MethodUpdater( m_db, u.m_class.getMethod(ATTRIBUTE_INITIALIZER, void_type, getSignatureKey( new Signature())));
						}
						attrUpdater=u.m_updater;
					}
					DBMethod called=getCurrentClass(custom.getContainingType()).getMethod(".ctor", DBType.get(m_db, custom.getSignature().getReturnType()), getSignatureKey( custom.getSignature()));
					if ( called.updateArguments(m_db, custom.getSignature()))
						ObjectDB.makeDirty( called);
					attrUpdater.addCall( called, m_current_source_file, m_current_line);
					for ( Iterator i=custom.getStringArguments().iterator(); i.hasNext();)
					{
						attrUpdater.addStringReference( DBString.get( m_db, (String)i.next()), m_current_source_file, m_current_line);
					}
					for ( Iterator<CustomAttributeSetting.NamedArgument> i=custom.getNamedArguments().iterator(); i.hasNext();)
					{
						CustomAttributeSetting.NamedArgument named=i.next();
						if ( named.isProperty())
						{
							Signature s=new Signature();
							s.addArgument( new ReadArg(named.getType()));
							s.setReturnType( void_read_type);
							DBMethod propertySetter=getCurrentClass(custom.getContainingType()).getMethod("set_"+named.getName(), void_type, getSignatureKey( s));
							if ( propertySetter.updateArguments(m_db, s))
								ObjectDB.makeDirty( propertySetter);
							attrUpdater.addCall(propertySetter, m_current_source_file, m_current_line);
						}
						else
						{
							attrUpdater.addFieldReference( getCurrentClass(custom.getContainingType()).getField(named.getName(), DBType.get( m_db, named.getType())), true, m_current_source_file, m_current_line);
						}
						for ( Iterator<String> si=named.getStringArguments().iterator(); si.hasNext();)
						{
							attrUpdater.addStringReference( DBString.get(m_db, si.next()), m_current_source_file, m_current_line);
						}
					}
				}
			}
			catch ( ITypeInterpreter.TIException ti)
			{
				LoggingDBDriver.logger.info(ti.getMessage());				
			}
		}
		m_custom_attributes.clear();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addResource(java.lang.String, java.lang.String)
	 */
	public void addResource(String name, String value) {
		m_bundle_updater.addNameValuePair(m_db, name, value);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endBundle()
	 */
	public void endBundle() {
		m_bundle_updater.finishBundle( m_db);
		m_bundle_updater=null;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startBundle(java.lang.String)
	 */
	public void startBundle(String name) {
		m_bundle_updater=new DBBundleBase.BundleUpdater( DBBundle.get(m_db, m_current_assembly, name));
	}

	private static String getSignatureKey( Signature sig) throws ITypeInterpreter.TIException
	{
		StringBuilder sb=new StringBuilder();
		for ( Iterator i=sig.getArguments().iterator(); i.hasNext();)
		{
			sb.append(',');
			ReadType t=((ReadArg)i.next()).getType();
			if ( t==null)
				sb.append("...");
			else
				sb.append( ITypeInterpreter.Factory.getTypeKey(t));
		}
		
		return sb.toString();
	}

	/**
	 * Arbitrary string (but guaranteed not to match any valid class) representing
	 * the class that holds methods not defined in any class.
	 */
	static private String NAMELESS_CLASS="`NAMELESS_CLASS`";
	
	/**
	 * Interpret a type and a name in the context of the current class
	 * and namespace stack to get a DBClass and the name within it.
	 * @author Michael A. MacDonald
	 *
	 */
	private DBClass getCurrentClass( ReadType type ) throws ITypeInterpreter.TIException
	{
		if ( type!=null)
		{
			return DBClass.get( m_db, ITypeInterpreter.Factory.getClassKey(type));
		}
		if ( m_class_stack.size()>0)
		{
			return m_class_stack.get( m_class_stack.size()-1).m_class;
		}
		String namespace="";
		if ( m_namespace_stack.size()>0)
		{
			namespace=m_namespace_stack.get( m_namespace_stack.size()-1).toString();
		}
		return DBClass.get( m_db, namespace+"."+NAMELESS_CLASS);
	}
	
	/**
	 * Entry on class stack that holds DBClass and optionally the class's custom attribute initialization method updater
	 * @author Michael A. MacDonald
	 *
	 */
	static class ClassUpdater
	{
		DBClass m_class;
		DBMethod.MethodUpdater m_updater;
		
		ClassUpdater( DBClass cl)
		{
			m_class=cl;
		}
	}
}
