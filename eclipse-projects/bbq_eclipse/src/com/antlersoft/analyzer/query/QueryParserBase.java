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
package com.antlersoft.analyzer.query;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import com.antlersoft.analyzer.*;
import com.antlersoft.parser.*;
import com.antlersoft.query.*;


class QueryParserBase extends BasicBase
{
    public QueryParserBase( ParseState[] parseStates)
    {
        super( parseStates);
        tokens=null;
        storedValues=new HashMap();
        storedValuesSupport=new PropertyChangeSupport( this);
        importedPackages=new TreeSet();
    }

    public SetExpression getExpression()
        throws ParseException
    {
        currentIndex=0;
        boolean errorOut=false;
        for (; ! errorOut && currentIndex<tokens.length; currentIndex++)
        {
        	Token token=tokens[currentIndex];
        	massageToken( token);
            errorOut=parse( token.symbol, token.value);
        }
        if ( errorOut)
        {
            ParseException pe=new ParseException( this);
            reset();
            throw pe;
        }
        return getLastParsedExpression();
    }

    public void setLine( String toParse)
    {
        tokens=tokenize( toParse);
    }

    // Bean type interface for accessing stored value names
    public String[] getStoredValues()
    {
        return (String[])storedValues.keySet().toArray( new String[0]);
    }

    public void addStoredValuesListener( PropertyChangeListener l)
    {
        storedValuesSupport.addPropertyChangeListener( "storedValues", l);
    }

    public void removeStoredValuesListener( PropertyChangeListener l)
    {
        storedValuesSupport.removePropertyChangeListener( l);
    }

    // Protected interface
    
    /**
     * Anything a sub-class might want to do with a token
     */
    protected void massageToken( Token token)
    {
    
    }
    
    protected final boolean nameSymbolExpected()
    {
    	for ( Enumeration e=getExpectedSymbols(); e.hasMoreElements();)
    	{
    		if ( e.nextElement()==nameSymbol)
    			return true;
    	}
    	return false;
    }

    // Package interface
    Token[] tokens;
    int currentIndex;
    HashMap storedValues; // String, SetExpression
    PropertyChangeSupport storedValuesSupport;
    TreeSet importedPackages;

    static Symbol nameSymbol=Symbol.get( "_nameSymbol");

    static class LiteralToken extends Token
    {
        LiteralToken( String val)
        {
            super( literalString, val);
        }

        public String toString()
        {
            return "\""+value+"\"";
        }
    }
    
    private static void addCurrentString( StringBuffer currentString, Vector tokens)
    {
        if ( currentString.length()>0)
        {
            String cs=currentString.toString();
            currentString.setLength(0);
            ReservedWord rw=(ReservedWord)ReservedWord.wordList.get( cs);
            if ( rw==null)
            {
                tokens.addElement( new Token( nameSymbol, cs));
            }
            else
            {
                tokens.addElement( new Token( rw, cs));
            }
        }  	
    }
    
    static Token[] tokenize( String toTokenize)
    {
        char[] chars=toTokenize.toCharArray();
        Vector tokens=new Vector();
        StringBuffer currentString=new StringBuffer();
        int i=0;
        boolean inQuoted=false;
        for ( ; i<=chars.length; i++)
        {
            char c;
            if ( i==chars.length)
            {
                if ( inQuoted)
                    c='"';
                else
                    c='\n';
            }
            else
            {
                c=chars[i];
            }
            if ( inQuoted)
            {
                if ( c=='"')
                {
                    tokens.addElement( new LiteralToken( currentString.toString()));
                    currentString.setLength(0);
                    inQuoted=false;
                }
                else
                    currentString.append( c);
            }
            else
            {
                switch ( c)
                {
                // Whitespace cases
                case ' ' :
                case '\r' :
                case '\n' :
                case '\t' :
                	addCurrentString( currentString, tokens);
                	break;
                	
                // Initial quote
                case '"' :
                	addCurrentString( currentString, tokens);
                    if ( c=='"')
                    {
                        inQuoted=true;
                    }
                    break;
                    
                // Non-quote Punctuation-- only supports single char punctuation tokens
                case ',' :
                case '(' :
                case ')' :
                	addCurrentString( currentString, tokens);
                	currentString.append( c);
                	addCurrentString( currentString, tokens);
                	break;

                default :
	                currentString.append( c);
                }
            }
        }
        tokens.addElement( new Token( Parser._end_, ""));

        Token[] retval=new Token[tokens.size()];
        tokens.copyInto( retval);
        return retval;
    }

    static class ReservedWord extends Symbol
    {
        static Hashtable wordList=new Hashtable();
        private ReservedWord( String w)
            throws DuplicateSymbolException
        {
            super( w);
            wordList.put( w, this);
        }

        static ReservedWord getReserved( String w)
        {
            ReservedWord result;
            try
            {
                result=new ReservedWord( w);
            }
            catch ( DuplicateSymbolException dse)
            {
                result=(ReservedWord)dse.duplicate;
            }
            return result;
        }
    }

    /*
     ClassSet : Class "x"
              | All Classes
              | Base Classes of ClassSet
              | Recursive Base Classes of ClassSet
              | Derived From ClassSet
              | Recursive Derived From ClassSet
              | Class of MethodSet
              | ClassSet matching "x"
     */
    static class ClassGet extends SetExpression
    {
        private String _className;
        private TreeSet _set;

        ClassGet( String className, TreeSet set)
        {
            _className=className;
            _set=set;
        }
        
        public Class getResultClass()
        {
        	return DBClass.class;
        }

        public Enumeration evaluate( DataSource source)
        {
            Vector tmp=new Vector( 1);
            AnalyzerDB db=(AnalyzerDB)source;
            try
            {
	            Object q=db.findWithKey( "com.antlersoft.analyzer.DBClass", _className);
	            if ( q!=null)
	                tmp.addElement( q);
	            for ( Iterator i=_set.iterator(); i.hasNext() && q==null;)
	            {
	                q=db.findWithKey( "com.antlersoft.analyzer.DBClass", ((String)i.next())+"."+_className);
	                if ( q!=null)
	                    tmp.addElement( q);
	            }
	            return tmp.elements();
            }
            catch ( Exception e)
            {
            	throw new RuntimeException( e);
            }
        }
    }

    static class ClassesGet extends SetExpression
    {
    	public Class getResultClass()
    	{
    		return DBClass.class;
    	}
    	
        public Enumeration evaluate( DataSource db)
        {
        	try
        	{
        		return ((AnalyzerDB)db).getAll( "com.antlersoft.analyzer.DBClass");
        	}
        	catch ( Exception e)
        	{
        		throw new RuntimeException( e);
        	}
        }
    }

    static abstract class ClassTransform extends UniqueTransformImpl
    {
        ClassTransform( )
        {
            super( DBClass.class, DBClass.class);
        }
    }

    static class BaseClasses extends ClassTransform
    {
        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            DBClass c=(DBClass)toTransform;
            return c.getSuperClasses();
        }
    }

    static class DerivedClasses extends ClassTransform
    {
        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            DBClass c=(DBClass)toTransform;
            return c.getDerivedClasses();
        }
    }

    static class RecursiveBaseClasses extends ClassTransform
    {
        private void addSuperClasses( DBClass current)
        {
            Enumeration superEnum=current.getSuperClasses();
            while ( superEnum.hasMoreElements())
            {
                DBClass superClass=(DBClass)superEnum.nextElement();
                if ( ! m_set.contains( superClass))
                {
                    m_set.add( superClass);
                    addSuperClasses( superClass);
                }
            }
        }

        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            addSuperClasses( (DBClass)toTransform);
            return EmptyEnum.empty;
        }
    }

    static class RecursiveDerivedClasses extends ClassTransform
    {
        private void addDerivedClasses( DBClass current)
        {
            Enumeration derivedEnum=current.getDerivedClasses();
            while ( derivedEnum.hasMoreElements())
            {
                DBClass derivedClass=(DBClass)derivedEnum.nextElement();
                if ( ! m_set.contains( derivedClass))
                {
                    m_set.add( derivedClass);
                    addDerivedClasses( derivedClass);
                }
            }
        }

        public Object uniqueTransform( DataSource source, Object toTransform)
        {
            addDerivedClasses( (DBClass)toTransform);
            return EmptyEnum.empty;
        }
    }

    static class ClassContaining extends CountPreservingValueExpression
    {
        ClassContaining()
        {
            super( DBClass.class, null);
        }

        public Object transformSingleObject( DataSource source, Object toTransform)
        {
            if ( appliesClass()==DBMethod.class)
                return ((DBMethod)toTransform).getDBClass();
            if ( appliesClass()==DBField.class)
                return ((DBField)toTransform).getDBClass();
            throw new RuntimeException( "Class of incorrectly bound");
        }

        public void lateBindApplies( Class c)
            throws BindException
        {
            if ( c!=DBMethod.class && c!=DBField.class)
            {
                throw new BindException( "Operator only applies to methods and fields");
            }
            super.lateBindApplies( c);
        }
    }

/*
    static class ClassMatching implements ClassSet
    {
        ClassSet _toFilter;
        String _filterString;

        ClassMatching( ClassSet toFilter, String filterString)
        {
            _toFilter=toFilter;
            _filterString=filterString;
        }

        public Enumeration execute( AnalyzerDB db)
            throws Exception
        {
            return new FilterEnumeration( _toFilter.execute( db)) {
                protected Object filterObject( Object next)
                {
                    Object retVal=null;
                    if ( ((DBClass)next).getName().indexOf(
                        _filterString)!= -1)
                        retVal=next;
                    return retVal;
                }
            };
        }
    }
*/

    static class SingleClass extends SetExpression
    {
        DBClass _class;

        SingleClass( DBClass c)
        {
            _class=c;
        }
        
        public Class getResultClass()
        {
        	return _class.getClass();
        }

        public Enumeration evaluate( DataSource source)
        {
            return new SingleEnum( _class);
        }
    }

    /*
      MethodSet : methods in ClassSet
                | polymorphic MethodSet
                | matching "x" MethodSet
                | uncalled MethodSet
                | uncalled polymorphic MethodSet
     */
    static class MethodsIn extends TransformImpl
    {
        MethodsIn()
        {
            super( DBMethod.class, DBClass.class);
        }

        public Enumeration transformObject( DataSource source, Object toTransform)
        {
            return ((DBClass)toTransform).getMethods();
        }
    }

    static public abstract class FilterEnumeration implements Enumeration
    {
        private Enumeration _baseEnumeration;
        private Object next;
        // Kludge because of compiler bug
        private boolean initialized;

        public FilterEnumeration( Enumeration base)
        {
            _baseEnumeration=base;
            // Kludge because of compiler bug
            initialized=false;
        }

        abstract protected Object filterObject( Object next);

        private void getNextObject()
        {
            for ( next=null; next==null &&
                _baseEnumeration.hasMoreElements();)
            {
                next=filterObject( _baseEnumeration.nextElement());
            }
        }

        public boolean hasMoreElements()
        {
            // Kludge because of compiler bug
            if ( ! initialized)
            {
                getNextObject();
                initialized=true;
            }
            return next!=null;
        }

        public Object nextElement()
        {
            // Kludge because of compiler bug
            if ( ! initialized)
            {
                getNextObject();
                initialized=true;
            }
            if ( next==null)
                throw new NoSuchElementException();
            Object retVal=next;
            getNextObject();
            return retVal;
        }
    }

    static class Polymorphic extends TransformImpl
    {
        private AnalyzerDB db;

        Polymorphic() {
            super( DBMethod.class, DBMethod.class);
        }

        public void startEvaluation( DataSource adb)
        {
            db=(AnalyzerDB)adb;
        }

        public Enumeration transformObject( DataSource source, Object toTransform)
        {
        	try
        	{
	            final DBMethod soughtMethod=(DBMethod)toTransform;
	            DBClass c=soughtMethod.getDBClass();
	            Enumeration fromBase=new FilterEnumeration(
	                ( new TransformSet( new RecursiveBaseClasses(),
	                		new SingleClass( c))).evaluate( db)) {
	                protected Object filterObject( Object base)
	                {
	                    DBClass filterc=(DBClass)base;
	                    try
	                    {
	                    return db.findWithKey( "com.antlersoft.analyzer.DBMethod",
	                        DBMethod.makeKey( filterc.getName(),
	                        soughtMethod.getName(),
	                        soughtMethod.getSignature()));
	                    }
	                    catch ( Exception e)
	                    {
	                        throw new NoSuchElementException(
	                            e.getMessage());
	                    }
	                }
	            };
	            Enumeration fromDerived=new GoodBase( c, db, soughtMethod);
	            return new CombineEnum( new SingleEnum(
	                soughtMethod), new CombineEnum( fromBase,
	                fromDerived));
        	}
        	catch ( BindException be)
        	{
        		throw new RuntimeException( be);
        	}
        }
    }

    static class GoodBase extends MultiEnum {
        AnalyzerDB _adb;
        DBMethod soughtMethod;
        GoodBase( DBClass fromHere, AnalyzerDB adb, DBMethod sm)
        throws BindException
        {
            super( ( new TransformSet(
                new DerivedClasses(), new SingleClass( fromHere))).
                evaluate( adb));
            _adb=adb;
            soughtMethod=sm;
        }

        protected Enumeration getNextCurrent(
            Object base)
        {
            DBClass c=(DBClass)base;
            try
            {
                DBMethod candidate=(DBMethod)
                    _adb.findWithKey( "com.antlersoft.analyzer.DBMethod",
                    DBMethod.makeKey( c.getName(),
                    soughtMethod.getName(),
                    soughtMethod.getSignature()));
                if ( candidate==null || !
                    ( candidate.methodStatus()==
                    DBMethod.REAL))
                {
                    return candidate==null ?
                        (Enumeration)new GoodBase( c, _adb, soughtMethod) :
                        (Enumeration)new CombineEnum(
                        new SingleEnum( candidate),
                        new GoodBase( c, _adb, soughtMethod));
                }
                else
                {
                    return EmptyEnum.empty;
                }
            }
            catch ( Exception e)
            {
                throw new NoSuchElementException(
                    e.getMessage());
            }
        }
    }

    static class UncalledMethod extends CountPreservingFilter
    {
        UncalledMethod()
        {
        	m_bind=new BindImpl( BOOLEAN_CLASS, DBMethod.class);
        }

        protected boolean getCountPreservingFilterValue( DataSource source, Object toCheck)
        {
            return ! ((DBMethod)toCheck).getCalledBy().hasMoreElements();
        }
        
        private BindImpl m_bind;

		/* (non-Javadoc)
		 * @see com.antlersoft.query.BindImpl#appliesClass()
		 */
		public Class appliesClass() {
			return m_bind.appliesClass();
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.query.BindImpl#lateBindApplies(java.lang.Class)
		 */
		public void lateBindApplies(Class new_applies) throws BindException {
			m_bind.lateBindApplies(new_applies);
		}
    }

    static class UncalledPolymorphic extends UncalledMethod
    {
        protected boolean getCountPreservingFilterValue( DataSource source, Object toInclude)
        {
        	try
        	{
	            DBMethod method=(DBMethod)toInclude;
	            boolean result=true;
	            for ( Enumeration e=(new TransformSet( new Polymorphic(), new SingleMethod( method))).evaluate( source);
	                e.hasMoreElements() && result;)
	            {
	                if ( ((DBMethod)e.nextElement()).
	                    getCalledBy().hasMoreElements())
	                    result=false;
	            }
                return result;
        	}
        	catch (BindException be)
        	{
        		throw new RuntimeException( be);
        	}
        }
    }

    static class SingleMethod extends SetExpression
    {
        DBMethod _method;

        SingleMethod( DBMethod c)
        {
            _method=c;
        }
        
        public Class getResultClass()
        {
        	return _method.getClass();
        }

        public Enumeration evaluate( DataSource db)
        {
            return new SingleEnum( _method);
        }
    }
}