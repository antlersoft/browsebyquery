/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.analyzer;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Enumeration;

import com.antlersoft.classwriter.*;

import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

import com.antlersoft.util.NetByte;

public class DBMethod implements Persistent, Cloneable, SourceObject, AccessFlags
{
    public static final int UNRESOLVED=1;
    public static final int VIRTUAL=2;
    public static final int REAL=3;
    private static final long serialVersionUID = 7199971582900640229L;
    
    ObjectRef dbclass;
    ObjectRef returnType;
    String name;
    String signature;
    Vector calls;
    Vector calledBy;
    Vector fieldReferences;
    Vector stringReferences;
    Vector arguments;
    int accessFlags;

    private boolean resolved;
    private int lineNumber;

    static public Vector emptyVector=new Vector();

    private transient PersistentImpl _persistentImpl;

    public DBMethod( String key, AnalyzerDB db)
		throws Exception
    {
		StringTokenizer st=new StringTokenizer( key, "\t");
		dbclass=new ObjectRef( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", (String)st.nextElement()));
		name=(String)st.nextElement();
		signature=(String)st.nextElement();
		resolved=false;
    lineNumber=0;
		_persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
    ((DBClass)dbclass.getReferenced()).addMethod( this);
    }

    public PersistentImpl _getPersistentImpl()
    {
		if ( _persistentImpl==null)
				_persistentImpl=new PersistentImpl();
		return _persistentImpl;
    }

    public String toString()
    {
		return ((DBClass)dbclass.getReferenced()).name+":"+name+signature;
    }

    public int methodStatus()
    {
		if ( ! ((DBClass)dbclass.getReferenced()).isResolved())
		    return UNRESOLVED;
		else
		    return resolved ? REAL : VIRTUAL;
    }

    public static String makeKey( String className, String methodName, String descriptor)
    {
		StringBuffer sb=new StringBuffer();
		sb.append( className);
		sb.append( "\t");
		sb.append( methodName);
		sb.append( "\t");
		sb.append( descriptor);
		return sb.toString();
    }

    public String getName()
    {
		return name;
    }

    public String getSignature()
    {
		return signature;
    }

    public DBClass getDBClass()
    {
		return (DBClass)dbclass.getReferenced();
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public Enumeration getCalls()
    {
		if ( calls!=null)
		    return calls.elements();
		return emptyVector.elements();
    }

    public Enumeration getReferences()
    {
		if ( fieldReferences!=null)
		{
		    return fieldReferences.elements();
		}
		return emptyVector.elements();
    }

    public Enumeration getStringReferences()
    {
		if ( stringReferences!=null)
		{
		    return stringReferences.elements();
		}
		return emptyVector.elements();
    }

    public Enumeration getCalledBy()
    {
		if ( calledBy!=null)
		    return calledBy.elements();
		return emptyVector.elements();
    }

    public void addCalledBy( DBMethod caller, Vector callsFromCaller)
    {
		ObjectDB.makeDirty( this);
		if ( calledBy==null)
    {
		    if ( ! callsFromCaller.isEmpty())
            calledBy=callsFromCaller;
    }
		else
		    /* Remove calls from same method and append calls to list */
		{
		    int i;
		    for ( i=0; i<calledBy.size(); i++)
		    {
				if ( ((DBCall)calledBy.elementAt( i)).getSource()==caller)
				{
				    calledBy.removeElementAt( i);
				    i--;
				}
		    }
		    for ( i=0; i<callsFromCaller.size(); i++)
		    {
				calledBy.addElement( callsFromCaller.elementAt( i));
		    }
		}
    }

    public int getAccessFlags()
    {
        return accessFlags;
    }

    public void setFromClassWriter( final ClassWriter ac, MethodInfo mi,
		final AnalyzerDB db)
		throws CodeCheckException
    {
        accessFlags=mi.getFlags();
		CodeAttribute codeAttribute=mi.getCodeAttribute();
		if ( codeAttribute==null)
		{
			return;
		}
		HashMap calledTable=new HashMap();
		HashMap referencedTable=new HashMap();
		HashMap stringReferenceTable=new HashMap();
		if ( calls==null)
			calls=new Vector();
		else
		{
			for ( Iterator it=calls.iterator(); it.hasNext();)
			{
				DBMethod target=((DBCall)it.next()).getTarget();
				Vector called=(Vector)calledTable.get( target);
				if ( called!=null)
					calledTable.put( target, new Vector());
			}
			calls.removeAllElements();
		}
		if ( fieldReferences==null)
			fieldReferences=new Vector();
		else
		{
			for ( Iterator it=fieldReferences.iterator(); it.hasNext();)
			{
				DBField target=((DBFieldReference)it.next()).getTarget();
				Vector called=(Vector)referencedTable.get( target);
				if ( called!=null)
					referencedTable.put( target, new Vector());
			}
			fieldReferences.removeAllElements();
		}
		if ( stringReferences==null)
			stringReferences=new Vector();
		else
		{
			for ( Iterator it=stringReferences.iterator(); it.hasNext();)
			{
				DBStringConstant target=((DBStringReference)it.next()).getTarget();
				Vector called=(Vector)stringReferenceTable.get( target);
				if ( called!=null)
					stringReferenceTable.put( target, new Vector());
			}
			stringReferences.removeAllElements();
		}
		lineNumber=codeAttribute.getLineNumber( 0);
		for ( Iterator i=codeAttribute.getInstructions().iterator();
			i.hasNext();)
		{
			Instruction instruction=(Instruction)i.next();
			OpCode opcode=instruction.getOpCode();
			if ( opcode instanceof InvokeOpCode)
			{
				try
				{
					ClassWriter.CPTypeRef methodRef=instruction.getSymbolicReference( ac);
					calls.addElement( new DBCall(
						DBMethod.this,
						(DBMethod)db.getWithKey( "com.antlersoft.analyzer.DBMethod",
						DBMethod.makeKey(
						ac.getClassName( methodRef.getClassIndex()),
						methodRef.getSymbolName(),
						methodRef.getSymbolType()
						)), codeAttribute.getLineNumber( instruction.getInstructionStart())));
				}
				catch ( Exception e)
				{
					e.printStackTrace();
				}
			}
			else if ( opcode instanceof GetOpCode)
			{
				try
				{
					ClassWriter.CPTypeRef methodRef=instruction.getSymbolicReference( ac);
					fieldReferences.addElement( new DBFieldReference(
						DBMethod.this,
						(DBField)db.getWithKey( "com.antlersoft.analyzer.DBField",
						DBField.makeKey(
						ac.getClassName( methodRef.getClassIndex()),
						methodRef.getSymbolName()
						)), codeAttribute.getLineNumber( instruction.getInstructionStart()), opcode.getMnemonic().substring( 0, 3).equals( "put")));
				}
				catch ( Exception e)
				{
					e.printStackTrace();
				}
			}
			else if ( opcode.getMnemonic().equals( "ldc") || opcode.getMnemonic().equals( "ldc_w"))
			{
				int constantIndex=instruction.operandsAsInt();
				String constant=ac.getIfStringConstant( constantIndex);
				if ( constant != null )
				{
					// Throw out boring references to empty
					// string
					if ( constant.length()>0)
					{
						try
						{
							stringReferences.add( new DBStringReference(
								DBMethod.this, (DBStringConstant)db.getWithKey(
							"com.antlersoft.analyzer.DBStringConstant",
								constant),
								codeAttribute.getLineNumber( instruction.getInstructionStart())));
						}
						catch ( Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		Enumeration e;
		Iterator it;
		for ( e=calls.elements(); e.hasMoreElements(); )
		{
			DBCall call=(DBCall)e.nextElement();
			Vector calledByMethod=(Vector)calledTable.get( call.getTarget());
			if ( calledByMethod==null)
			{
					calledByMethod=new Vector();
					calledTable.put( call.getTarget(), calledByMethod);
			}
			calledByMethod.addElement( call);
		}
		for ( it=calledTable.keySet().iterator(); it.hasNext(); )
		{
			DBMethod called=(DBMethod)it.next();
			called.addCalledBy( this, (Vector)calledTable.get( called));
		}
		calledTable.clear();
		for ( e=fieldReferences.elements(); e.hasMoreElements(); )
		{
			DBFieldReference reference=(DBFieldReference)e.nextElement();
			Vector calledByMethod=(Vector)referencedTable.get(
		reference.getTarget());
			if ( calledByMethod==null)
			{
					calledByMethod=new Vector();
					referencedTable.put( reference.getTarget(), calledByMethod);
			}
			calledByMethod.addElement( reference);
		}
		for ( it=referencedTable.keySet().iterator(); it.hasNext(); )
		{
			DBField called=(DBField)it.next();
			called.addReferencedBy( this, (Vector)referencedTable.
			get( called));
		}
		referencedTable.clear();
		for ( e=stringReferences.elements(); e.hasMoreElements(); )
		{
			DBStringReference reference=(DBStringReference)e.nextElement();
			Vector calledByMethod=(Vector)stringReferenceTable.get(
			reference.getTarget());
			if ( calledByMethod==null)
			{
				calledByMethod=new Vector();
				stringReferenceTable.put( reference.getTarget(), calledByMethod);
			}
			calledByMethod.addElement( reference);
		}
		for ( it=stringReferenceTable.keySet().iterator(); it.hasNext(); )
		{
			DBStringConstant called=(DBStringConstant)it.next();
			called.addReferencedBy( this, (Vector)stringReferenceTable.
			get( called));
		}
		stringReferenceTable.clear();
    }
}
