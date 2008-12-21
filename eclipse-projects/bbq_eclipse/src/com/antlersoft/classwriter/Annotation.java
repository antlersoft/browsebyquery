/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.classwriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * An annotation stored in the Java class file as described in JSR-175
 * @author Michael A. MacDonald
 *
 */
public class Annotation implements AnnotationInfo {
	private int type;
	private int numElementValues;
	private ElementValuePair[] elementValuePairs;
	
	Annotation( DataInputStream classStream)
	throws IOException
	{
		type=classStream.readUnsignedShort();
		numElementValues=classStream.readUnsignedShort();
		elementValuePairs=new ElementValuePair[numElementValues];
		for ( int i=0; i<numElementValues; ++i)
		{
			elementValuePairs[i]=new ElementValuePair(classStream);
		}
	}
	
	void write(DataOutputStream classStream) throws IOException
	{
		classStream.writeShort(type);
		classStream.writeShort(numElementValues);
		for ( int i=0; i<numElementValues; ++i)
		{
			elementValuePairs[i].write(classStream);
		}
	}
	
	public int getClassIndex()
	{
		return type;
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.classwriter.AnnotationInfo#gatherAnnotationInfo(com.antlersoft.classwriter.ClassWriter, java.util.Collection, java.util.Collection)
	 */
	public void gatherAnnotationInfo(ClassWriter container, Collection<Annotation> annotations, Collection<String> strings) {
		for ( ElementValuePair p : elementValuePairs)
			p.gatherAnnotationInfo( container, annotations, strings);
	}

	/**
	 * One element name-value pair in an annotation.
	 * @author Michael A. MacDonald
	 *
	 */
	public static class ElementValuePair implements AnnotationInfo
	{
		private int name;
		private ElementValue value;
		
		ElementValuePair( DataInputStream classStream)
		throws IOException
		{
			name=classStream.readUnsignedShort();
			value=new ElementValue( classStream);
		}
		
		void write( DataOutputStream classStream)
		throws IOException
		{
			classStream.writeShort( name);
			value.write(classStream);
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.classwriter.AnnotationInfo#gatherAnnotationInfo(com.antlersoft.classwriter.ClassWriter, java.util.Collection, java.util.Collection)
		 */
		public void gatherAnnotationInfo(ClassWriter container, Collection<Annotation> annotations, Collection<String> strings) {
			strings.add( container.getStringConstant(name));
			value.value.gatherAnnotationInfo(container, annotations, strings);
		}
	}
	
	/**
	 * Discriminated union of value types
	 * @author Michael A. MacDonald
	 *
	 */
	public static class ElementValue
	{
		private int type;
		private ValueBase value;
		
		ElementValue( DataInputStream classStream)
		throws IOException
		{
			type=classStream.readUnsignedByte();
			value=ValueBase.createValueBase( type, classStream);
		}
		
		void write( DataOutputStream classStream)
		throws IOException
		{
			classStream.write(type);
			value.write( classStream);
		}
	}
	
	/**
	 * Base class for value types within an ElementValue.
	 * @author Michael A. MacDonald
	 *
	 */
	public abstract static class ValueBase implements AnnotationInfo
	{
		static ValueBase createValueBase( int type, DataInputStream classStream)
		throws IOException
		{
			ValueBase result;
			switch ( type )
			{
			case '@' : result=new AnnotationValue(classStream); break;
			case 'c' : result=new ClassValue(classStream); break;
			case 'e' : result=new EnumValue(classStream); break;
			case '[' : result=new ArrayValue(classStream); break;
			default :
				result=new ConstPoolValue( classStream);
			}
			
			return result;
		}
		
		/**
		 * Write the value to the datastream as defined for the type
		 * @param classStream Class file output stream
		 * @throws IOException
		 */
		abstract void write( DataOutputStream classStream) throws IOException;

		/*
		 * @see com.antlersoft.classwriter.AnnotationInfo#gatherAnnotationInfo(com.antlersoft.classwriter.ClassWriter, java.util.Collection, java.util.Collection)
		 */
		public void gatherAnnotationInfo(ClassWriter container, Collection<Annotation> annotations, Collection<String> strings) {
			// Default implementation does nothing
		}
	}
	
	/**
	 * Annotation value is a constant in the constant pool
	 * @author Michael A. MacDonald
	 *
	 */
	public static class ConstPoolValue extends ValueBase
	{
		private int poolIndex;
		
		ConstPoolValue( DataInputStream classStream)
		throws IOException
		{
			poolIndex=classStream.readUnsignedShort();
		}
		
		@Override
		void write( DataOutputStream classStream) throws IOException
		{
			classStream.writeShort(poolIndex);
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.classwriter.AnnotationInfo#gatherAnnotationInfo(com.antlersoft.classwriter.ClassWriter, java.util.Collection, java.util.Collection)
		 */
		public void gatherAnnotationInfo(ClassWriter container, Collection<Annotation> annotations, Collection<String> strings) {
			String s=container.getIfStringConstant(poolIndex);
			if ( s!=null)
				strings.add(s);
		}
	}
	
	/**
	 * Annotation value is an enumeration value
	 * @author Michael A. MacDonald
	 *
	 */
	public static class EnumValue extends ValueBase
	{
		private int enumType;
		private int enumName;
		
		EnumValue( DataInputStream classStream)
		throws IOException
		{
			enumType=classStream.readUnsignedShort();
			enumName=classStream.readUnsignedShort();
		}
		
		/* (non-Javadoc)
		 * @see com.antlersoft.classwriter.Annotation.ValueBase#write(java.io.DataOutputStream)
		 */
		@Override
		void write(DataOutputStream classStream) throws IOException {
			classStream.writeShort(enumType);
			classStream.writeShort(enumName);
		}
	}
	/**
	 * Annotation value is string in pool representing a type
	 * @author Michael A. MacDonald
	 *
	 */
	public static class ClassValue extends ValueBase
	{
		private int poolIndex;
		
		ClassValue( DataInputStream classStream)
		throws IOException
		{
			poolIndex=classStream.readUnsignedShort();
		}
		
		@Override
		void write( DataOutputStream classStream) throws IOException
		{
			classStream.writeShort(poolIndex);
		}
	}
	/**
	 * Annotation value is an annotation
	 * @author Michael A. MacDonald
	 *
	 */
	public static class AnnotationValue extends ValueBase
	{
		private Annotation annotation;
		
		AnnotationValue( DataInputStream classStream)
		throws IOException
		{
			annotation=new Annotation(classStream);
		}
		
		@Override
		void write( DataOutputStream classStream) throws IOException
		{
			annotation.write(classStream);
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.classwriter.Annotation.ValueBase#gatherAnnotationInfo(com.antlersoft.classwriter.ClassWriter, java.util.Collection, java.util.Collection)
		 */
		@Override
		public void gatherAnnotationInfo(ClassWriter container, Collection<Annotation> annotations, Collection<String> strings) {
			annotations.add( annotation);
			annotation.gatherAnnotationInfo(container, annotations, strings);
		}
	}
	/**
	 * Annotation value is an array of values
	 * @author Michael A. MacDonald
	 *
	 */
	public static class ArrayValue extends ValueBase {
		private int numElements;
		private ElementValue[] elementValues;
		
		ArrayValue( DataInputStream classStream)
		throws IOException
		{
			numElements=classStream.readUnsignedShort();
			elementValues=new ElementValue[numElements];
			for ( int i=0; i<numElements; ++i)
			{
				elementValues[i]=new ElementValue(classStream);
			}
		}
		
		/* (non-Javadoc)
		 * @see com.antlersoft.classwriter.Annotation.ValueBase#write(java.io.DataOutputStream)
		 */
		@Override
		void write(DataOutputStream classStream) throws IOException {
			classStream.writeShort(numElements);
			for ( int i=0; i<numElements; ++i)
			{
				elementValues[i].write(classStream);
			}
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.classwriter.Annotation.ValueBase#gatherAnnotationInfo(com.antlersoft.classwriter.ClassWriter, java.util.Collection, java.util.Collection)
		 */
		@Override
		public void gatherAnnotationInfo(ClassWriter container, Collection<Annotation> annotations, Collection<String> strings) {
			for ( ElementValue ev : elementValues)
			{
				ev.value.gatherAnnotationInfo(container, annotations, strings);
			}
		}
	}
}
