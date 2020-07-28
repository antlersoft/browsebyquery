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

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Enumeration;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.antlersoft.bbq.db.DBString;

import com.antlersoft.classwriter.*;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;
import com.antlersoft.odb.FromRefIteratorEnumeration;

import com.antlersoft.query.EmptyEnum;

public class DBMethod extends DBMember
{
    public static final int UNRESOLVED=1;
    public static final int VIRTUAL=2;
    public static final int REAL=3;
    static Logger logger=Logger.getLogger( "com.antlersoft.analyzer.DBMethod");
    static final String RETURN_TYPE_INDEX="MethodType";
    
    String signature;
    private int lineNumber;
    ArrayList<ObjectRef<DBCall>> calls;
    ArrayList<ObjectRef<DBFieldReference>> fieldReferences;
    ArrayList<ObjectRef<DBStringReference>> stringReferences;
    ArrayList<ObjectRef<DBArgument>> arguments;
    ArrayList<ObjectRef<DBCatch>> catches;
    private boolean resolved;

    DBMethod( IndexAnalyzeDB db, DBClass containing, String name, String signature)
    throws Exception
    {
    	super(name,containing,getReturnTypeFromSignature( db, signature));
		this.signature=signature;
		resolved=false;
		createArguments( db);
		db.getSession().makePersistent( this);
    }

    public String toString()
    {
    	StringBuilder signature_builder=new StringBuilder();
    	signature_builder.append('(');
  		char[] array=signature.toCharArray();
  		int argument_count=0;
  		int start_offset=1;
  		int current_offset=1;
  		String return_type="";
  		try
  		{
	    	try
	     	{
		    	if ( array.length<3 || array[0]!='(')
		     	{
					throw new CodeCheckException( "Bad method signature "+signature);
		   		}
		    	for ( char ch; ( ch=array[current_offset])!=')'; ++current_offset)
		    	{
		    		switch ( ch)
		    		{
		    		case 'L' :
		    			for ( ++current_offset; array[current_offset]!=';'; ++current_offset);
		    			break;
		    		case '[' :
		    			continue;
		    		default :
		    			break;
		    		}
		    		if ( argument_count++ >0)
		    			signature_builder.append(", ");
		    		signature_builder.append(DBType.TypeStringMap.descriptorToUser(signature.subSequence(start_offset,current_offset+1)));
		    		start_offset=current_offset+1;
		    	}
		    	if ( name.indexOf('<')==-1)
		    		return_type=" "+DBType.TypeStringMap.descriptorToUser(signature.subSequence(current_offset+1,array.length));
	         }
	         catch ( ArrayIndexOutOfBoundsException bounds)
	         {
	             throw new CodeCheckException( "MethodType truncated");
	         }
  		}
  		catch ( CodeCheckException cce)
  		{
  			signature_builder.append( cce.getMessage());
  		}
  		signature_builder.append( ')');
		return getDBClass().name+":"+name+signature_builder.toString()+return_type;
    }

    public int methodStatus()
    {
		if ( ! getDBClass().isResolved())
		    return UNRESOLVED;
		else
		    return resolved ? REAL : VIRTUAL;
    }

    public String getName()
    {
		return name;
    }

    public String getSignature()
    {
		return signature;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public Enumeration<DBCall> getCalls()
    {
		if ( calls!=null)
		    return new FromRefIteratorEnumeration<DBCall>( calls.iterator());
		return EmptyEnum.empty;
    }
    
    public Enumeration<DBArgument> getArguments()
    {
    	if ( arguments!=null)
    		return new FromRefIteratorEnumeration<DBArgument>( arguments.iterator());
    	return EmptyEnum.empty;
    }

    public Enumeration<DBFieldReference> getReferences()
    {
		if ( fieldReferences!=null)
		{
		    return new FromRefIteratorEnumeration<DBFieldReference>( fieldReferences.iterator());
		}
		return EmptyEnum.empty;
    }

    public Enumeration<DBStringReference> getStringReferences()
    {
		if ( stringReferences!=null)
		{
		    return new FromRefIteratorEnumeration<DBStringReference>( stringReferences.iterator());
		}
		return EmptyEnum.empty;
    }

    public Enumeration<DBCall> getCalledBy( IndexAnalyzeDB db)
    {
    	return (Enumeration<DBCall>)db.retrieveByIndex( DBCall.CALL_TARGET, new ObjectRefKey(this));
    }
    
    public Enumeration<DBCatch> getCatches()
    {
    	if ( catches==null )
    	{
    		return EmptyEnum.empty;
    	}
		return new FromRefIteratorEnumeration<DBCatch>( catches.iterator());
    }
    
    private boolean isSpecialOrStatic()
    {
    	return (accessFlags & ClassWriter.ACC_STATIC)!=0 || name.startsWith("<");
    }
    
    static class ReferenceUpdater<E extends DBReference>
    {
    	ArrayList<ObjectRef<E>> beforeList;
    	ArrayList<ObjectRef<E>> afterList;
    	boolean updated;
    	ObjectDB m_db;
    	
    	ReferenceUpdater(ObjectDB db, ArrayList<ObjectRef<E>> original)
    	{
    		m_db = db;
    		if ( original!=null)
    			beforeList=(ArrayList<ObjectRef<E>>)original.clone();
    		else
    			beforeList=new ArrayList<ObjectRef<E>>();
    		afterList=new ArrayList<ObjectRef<E>>(beforeList.size());
    		updated=false;
    	}
    	
    	boolean existsReference( Persistent t, int lineNumber)
    	{
    		boolean result=false;
    		for ( Iterator<ObjectRef<E>> i=beforeList.iterator(); i.hasNext();)
    		{
    			ObjectRef<E> targetRef=i.next();
    			E dbref=targetRef.getReferenced();
    			if ( t.equals(dbref.target.getReferenced()))
    			{
    				i.remove();
    				afterList.add( targetRef);
    				dbref.setLineNumber( m_db, lineNumber);
    				result=true;
    				break;
    			}
    		}
    		return result;
    	}
    	
    	void addReference( E toAdd)
    	{
    		afterList.add( new ObjectRef<E>( toAdd));
    		updated=true;
    	}
    	
    	boolean cleanup( IndexAnalyzeDB db)
    	{
    		for ( ObjectRef<E> ref : beforeList)
    		{
    			Object o=ref.getReferenced();
    			ref.setReferenced( null);
    			db.deleteObject( o);
    			updated=true;
    		}
    		return updated;
    	}
    }
    
    static class FieldReferenceUpdater extends ReferenceUpdater<DBFieldReference>
    {
     	FieldReferenceUpdater(ObjectDB db, ArrayList<ObjectRef<DBFieldReference>> orig)
    	{
    		super(db, orig);
    	}
    	boolean existsReference( DBField t, int lineNumber, boolean write)
    	{
    		boolean result=false;
    		for ( Iterator<ObjectRef<DBFieldReference>> i=beforeList.iterator(); i.hasNext();)
    		{
    			ObjectRef<DBFieldReference> targetRef=i.next();
    			DBFieldReference dbref=targetRef.getReferenced();
    			if ( t.equals(dbref.target.getReferenced()) && write==dbref.writeReference)
    			{
    				i.remove();
    				afterList.add( targetRef);
    				dbref.setLineNumber(m_db, lineNumber);
    				result=true;
    				break;
    			}
    		}
    		return result;
    	}	
    }

    public void setFromClassWriter( final ClassWriter ac, MethodInfo mi,
		final IndexAnalyzeDB db)
		throws CodeCheckException
    {
    	ObjectDB session = db.getSession();
    	session.makeDirty( this);
        accessFlags=mi.getFlags();
        setDeprecated( mi.isDeprecated());
		CodeAttribute codeAttribute=mi.getCodeAttribute();
		if ( codeAttribute==null)
		{
			return;
		}
		createArguments( db);
		LocalVariableTableAttribute locals=codeAttribute.getLocalVariableTable();
		if ( locals!=null)
		{
			int variableOffset=isSpecialOrStatic() ? 0 : 1;
			for ( Iterator i=arguments.iterator(); i.hasNext();)
			{
				DBArgument arg=(DBArgument)((ObjectRef)i.next()).getReferenced();
				String entry=locals.getLocalVariable( ac, 1, arg.getOrdinal()+variableOffset);
				if ( entry!=null)
				{
					arg.setName( session, entry.substring( 0, entry.indexOf( '(')));
				}
			}
		}
		ReferenceUpdater<DBCall> callUpdater = new ReferenceUpdater<DBCall>(session, calls);
		FieldReferenceUpdater fieldRefUpdater=new FieldReferenceUpdater(session, fieldReferences);
		ReferenceUpdater<DBStringReference> stringRefUpdater=new ReferenceUpdater<DBStringReference>(session, stringReferences);
		ReferenceUpdater<DBCatch> catchUpdater=new ReferenceUpdater<DBCatch>( session, catches);
		lineNumber=codeAttribute.getLineNumber( 0);
		// Class linenumber = minimum linenumber of any method in the class
		if ( lineNumber>0)
		{
			DBClass c=getDBClass();
			if ( c.lineNumber== -1 || c.lineNumber>lineNumber)
				c.lineNumber=lineNumber;
		}
		for ( Iterator<Instruction> i=codeAttribute.getInstructions().iterator();
			i.hasNext();)
		{
			Instruction instruction=i.next();
			OpCode opcode=instruction.getOpCode();
			if ( opcode instanceof InvokeOpCode)
			{
				try
				{
					ClassWriter.CPTypeRef methodRef=instruction.getSymbolicReference( ac);
					DBMethod target=DBClass.getByInternalName(ac.getInternalClassName(methodRef.getClassIndex()), db).
						getOrCreateMethod( db, methodRef.getSymbolName(), methodRef.getSymbolType());
					int lineNumber=codeAttribute.getLineNumber( instruction.getInstructionStart());
					if ( ! callUpdater.existsReference( target, lineNumber))
					{
						DBCall call=new DBCall( session,DBMethod.this, target, lineNumber);
						callUpdater.addReference( call);
					}
					if ( opcode.getMnemonic().equals("invokestatic"))
					{
						if ( ( target.accessFlags & ClassWriter.ACC_STATIC)==0)
						{
							target.accessFlags|=ClassWriter.ACC_STATIC;
							session.makeDirty(target);
						}
					}
				}
				catch ( Exception e)
				{
					logger.log( Level.WARNING,"Problem processing method call opcode",e);
				}
			}
			else if ( opcode instanceof GetOpCode)
			{
				try
				{
					ClassWriter.CPTypeRef fieldRef=instruction.getSymbolicReference( ac);
					DBField target=DBClass.getByInternalName(ac.getInternalClassName(fieldRef.getClassIndex()), db).getField(db, fieldRef.getSymbolName(), fieldRef.getSymbolType());
					int lineNumber=codeAttribute.getLineNumber( instruction.getInstructionStart());
					boolean write=opcode.getMnemonic().startsWith("put");
					if ( ! fieldRefUpdater.existsReference(target,lineNumber,write))
					{
						DBFieldReference ref=new DBFieldReference( session,this,target,lineNumber,write);
						fieldRefUpdater.addReference(ref);
					}
				}
				catch ( Exception e)
				{
					logger.log( Level.WARNING,"Problem processing field reference opcode",e);
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
							int lineNumber=codeAttribute.getLineNumber( instruction.getInstructionStart());
							DBString target=DBString.get(db.getSession(),constant);
							if ( ! stringRefUpdater.existsReference(target, lineNumber))
							{
								stringRefUpdater.addReference( new DBStringReference(session, DBMethod.this, target, lineNumber));
							}
						}
						catch ( Exception e)
						{
							logger.log( Level.WARNING,"Problem processing string reference opcode",e);
						}
					}
				}
			}
		}
		for ( CodeAttribute.CodeException i : codeAttribute.getExceptions())
		{
			try
			{
				if ( i.getCatchType()!=0)
				{
					DBClass caught=DBClass.getByInternalName(ac.getInternalClassName(i.getCatchType()), db);
					int lineNumber=codeAttribute.getLineNumber( i.getEndIndex());
					if ( ! catchUpdater.existsReference( caught, lineNumber))
					{
						DBCatch c=new DBCatch( session,this, caught, lineNumber);
						catchUpdater.addReference( c);
					}
				}
			}
			catch ( Exception e)
			{
				logger.log( Level.WARNING, "Failed to find caught class: "+ac.getInternalClassName(i.getCatchType()), e);
			}
		}
		
		if ( callUpdater.cleanup(db))
		{
			calls=callUpdater.afterList;
			session.makeDirty(this);
		}
		if ( fieldRefUpdater.cleanup(db))
		{
			fieldReferences=fieldRefUpdater.afterList;
			session.makeDirty(this);
		}
		if ( stringRefUpdater.cleanup(db))
		{
			stringReferences=stringRefUpdater.afterList;
			session.makeDirty(this);
		}
		if ( catchUpdater.cleanup(db))
		{
			catches=catchUpdater.afterList;
			session.makeDirty(this);
		}
    }
    
    private void createArguments( IndexAnalyzeDB db)
    {
    	if ( arguments==null)
    	{
    		try
    		{
	      		char[] array=signature.toCharArray();
	      		int argument_count=0;
	      		int start_offset=1;
	      		int current_offset=1;
	      		arguments=new ArrayList<ObjectRef<DBArgument>>();
	        	try
	         	{
	    	    	if ( array.length<3 || array[0]!='(')
	    	     	{
	    				throw new CodeCheckException( "Bad method signature "+signature);
	    	   		}
	    	    	for ( char ch; ( ch=array[current_offset])!=')'; ++current_offset)
	    	    	{
	    	    		switch ( ch)
	    	    		{
	    	    		case 'L' :
	    	    			for ( ++current_offset; array[current_offset]!=';'; ++current_offset);
	    	    			break;
	    	    		case '[' :
	    	    			continue;
	    	    		default :
	    	    			break;
	    	    		}
	    	    		arguments.add( new ObjectRef<DBArgument>( new DBArgument( db.getSession(),this, argument_count++,
	    	    				DBType.getWithTypeKey(
	    	    						signature.substring( start_offset, current_offset+1), db))));
	    	    		start_offset=current_offset+1;
	    	    	}
	             }
	             catch ( ArrayIndexOutOfBoundsException bounds)
	             {
	                 throw new CodeCheckException( "Create arguments: MethodType truncated: "+signature);
	             }
    		}
    		catch( Exception e)
    		{
    			logger.log( Level.WARNING, "Failed to process method signature "+signature, e);
    		}
    	}
    }
    
    static DBType getReturnTypeFromSignature( IndexAnalyzeDB db, String sig)
    throws Exception
    {
    	int parenIndex=sig.lastIndexOf( ')');
    	if ( parenIndex<0 || parenIndex>sig.length()-2)
    		throw new CodeCheckException( "Method type truncated: "+sig);
    	return DBType.getWithTypeKey( sig.substring(parenIndex+1), db);
    }
}
