package com.antlersoft.analyzer.query;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import com.antlersoft.analyzer.*;
import com.antlersoft.parser.*;

import com.antlersoft.classwriter.ClassWriter;

class QueryParserBase extends Parser
{
    public QueryParserBase( ParseState[] parseStates)
    {
        super( parseStates);
        tokens=null;
        previousSet=null;
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
            errorOut=parse( tokens[currentIndex].symbol, tokens[currentIndex].value);
        }
        if ( errorOut)
        {
            ParseException pe=new ParseException( this);
            reset();
            throw pe;
        }
        return previousSet;
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

    // Package interface
    Token[] tokens;
    int currentIndex;
    SetExpression previousSet;
    HashMap storedValues; // String, SetExpression
    PropertyChangeSupport storedValuesSupport;
    TreeSet importedPackages;

    static Symbol literalString=Symbol.get( "_literalString");
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
                case ' ' :
                case '\r' :
                case '\n' :
                case '\t' :
                case '"' :
                    if ( currentString.length()>0)
                    {
                        String cs=currentString.toString().toLowerCase();
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
                    if ( c=='"')
                    {
                        inQuoted=true;
                    }
                    break;

                default :
                    currentString.append( c);
                }
            }
        }
        tokens.addElement( new Token( QueryParser.finalToken, ";"));
        tokens.addElement( new Token( Parser._end_, ""));

        Token[] retval=new Token[tokens.size()];
        tokens.copyInto( retval);
        return retval;
    }

    void pushCastToken( SetExpression se)
    {
        Token castToken;
        if ( se.getSetClass()==DBMethod.class)
            castToken=new Token( QueryParser.methodcast, "");
        else
            castToken=new Token( QueryParser.references, "");
/*
        else if ( se instanceof MethodSet)
            castToken=new Token( methodcast, "");
        else if ( se instanceof CallSet)
            castToken=new Token( callcast, "");
        else
            castToken=new Token( fieldcast, "");
*/
        ArrayList tokenList=new ArrayList( tokens.length+1);
        for ( int i=0; i<tokens.length; i++)
        {
            if ( i==currentIndex)
            {
                tokenList.add( castToken);
            }
            tokenList.add( tokens[i]);
        }
        tokens=new Token[tokenList.size()];
        tokens=(Token[])tokenList.toArray( tokens);
        currentIndex--;
        clearNextValue();
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
            super( DBClass.class);
            _className=className;
            _set=set;
        }

        public Enumeration execute( AnalyzerDB db)
            throws Exception
        {
            Vector tmp=new Vector( 1);
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
    }

    static class ClassesGet extends SetExpression
    {
        ClassesGet()
        {
            super( DBClass.class);
        }

        public Enumeration execute( AnalyzerDB db)
            throws Exception
        {
            return db.getAll( "com.antlersoft.analyzer.DBClass");
        }
    }

    static class BaseClasses extends TransformImpl
    {
        protected Hashtable ht;
        BaseClasses( )
        {
            super( DBClass.class, DBClass.class);
        }

        public void startEvaluation( AnalyzerDB db)
        {
            ht=new Hashtable();
        }

        public Enumeration transform( Object toTransform)
            throws Exception
        {
            DBClass c=(DBClass)toTransform;
            Enumeration superEnum=c.getSuperClasses();
            while ( superEnum.hasMoreElements())
            {
                DBClass superClass=(DBClass)superEnum.nextElement();
                ht.put( superClass, superClass);
            }
            return EmptyEnumeration.emptyEnumeration;
        }

        public Enumeration finishEvaluation()
        {
            Enumeration result= ht.keys();
            ht=null;
            return result;
        }
    }

    static class DerivedClasses extends BaseClasses
    {
        public Enumeration transform( Object toTransform)
            throws Exception
        {
            DBClass c=(DBClass)toTransform;
            Enumeration derivedEnum=c.getDerivedClasses();
            while ( derivedEnum.hasMoreElements())
            {
                DBClass derivedClass=(DBClass)derivedEnum.nextElement();
                ht.put( derivedClass, derivedClass);
            }
            return EmptyEnumeration.emptyEnumeration;
        }
    }

    static class RecursiveBaseClasses extends BaseClasses
    {
        private static void addSuperClasses( DBClass current, Hashtable ht)
        {
            Enumeration superEnum=current.getSuperClasses();
            while ( superEnum.hasMoreElements())
            {
                DBClass superClass=(DBClass)superEnum.nextElement();
                if ( ht.get( superClass)==null)
                {
                    ht.put( superClass, superClass);
                    addSuperClasses( superClass, ht);
                }
            }
        }

        public Enumeration transform( Object toTransform)
            throws Exception
        {
            addSuperClasses( (DBClass)toTransform, ht);
            return EmptyEnumeration.emptyEnumeration;
        }
    }

    static class RecursiveDerivedClasses extends BaseClasses
    {
        private static void addDerivedClasses( DBClass current, Hashtable ht)
        {
            Enumeration derivedEnum=current.getDerivedClasses();
            while ( derivedEnum.hasMoreElements())
            {
                DBClass derivedClass=(DBClass)derivedEnum.nextElement();
                if ( ht.get( derivedClass)==null)
                {
                    ht.put( derivedClass, derivedClass);
                    addDerivedClasses( derivedClass, ht);
                }
            }
        }

        public Enumeration transform( Object toTransform)
            throws Exception
        {
            addDerivedClasses( (DBClass)toTransform, ht);
            return EmptyEnumeration.emptyEnumeration;
        }
    }

    static class ClassOf extends UniqueTransform
    {
        ClassOf()
        {
            super( null, DBClass.class);
        }

        public Object uniqueTransform( Object toTransform)
            throws Exception
        {
            if ( appliesTo()==DBMethod.class)
                return ((DBMethod)toTransform).getDBClass();
            if ( appliesTo()==DBField.class)
                return ((DBField)toTransform).getDBClass();
            throw new Exception( "Class of incorrectly bound");
        }

        public void lateBindApplies( Class c)
            throws RuleActionException
        {
            if ( c!=DBMethod.class && c!=DBField.class)
            {
                throw new RuleActionException( "Operator only applies to methods and fields");
            }
            _applies=c;
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
            super( DBClass.class);
            _class=c;
        }

        public Enumeration execute( AnalyzerDB db)
        {
            return enumFromObject( _class);
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
            super( DBClass.class, DBMethod.class);
        }

        public Enumeration transform( Object toTransform)
            throws Exception
        {
            return ((DBClass)toTransform).getMethods();
        }
    }

    static class Polymorphic extends TransformImpl
    {
        private AnalyzerDB db;

        Polymorphic() {
            super( DBMethod.class, DBMethod.class);
        }

        public void startEvaluation( AnalyzerDB adb)
        {
            db=adb;
        }

        public Enumeration transform( Object toTransform)
            throws Exception
        {
            final DBMethod soughtMethod=(DBMethod)toTransform;
            DBClass c=soughtMethod.getDBClass();
            Enumeration fromBase=new FilterEnumeration(
                ( new TransformSet( new SingleClass( c),
                    new RecursiveBaseClasses())).execute( db)) {
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
            return new ComboEnumeration( enumFromObject(
                soughtMethod), new ComboEnumeration( fromBase,
                fromDerived));
        }
    }

    static class GoodBase extends BiEnumeration {
        AnalyzerDB _adb;
        DBMethod soughtMethod;
        GoodBase( DBClass fromHere, AnalyzerDB adb, DBMethod sm)
            throws Exception
        {
            super( ( new TransformSet(
                new SingleClass( fromHere), new DerivedClasses())).
                execute( adb));
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
                        (Enumeration)new ComboEnumeration(
                        enumFromObject( candidate),
                        new GoodBase( c, _adb, soughtMethod));
                }
                else
                {
                    return EmptyEnumeration.emptyEnumeration;
                }
            }
            catch ( Exception e)
            {
                throw new NoSuchElementException(
                    e.getMessage());
            }
        }
    }

/*
    static class MethodMatching implements MethodSet
    {
        MethodSet _toFilter;
        String _filterString;

        MethodMatching( MethodSet toFilter, String filterString)
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
                    if ( ((DBMethod)next).getName().indexOf(
                        _filterString)!= -1)
                        retVal=next;
                    return retVal;
                }
            };
        }
    }
*/

    static class UncalledMethod extends Filter
    {
        UncalledMethod()
        {
            super( DBMethod.class);
        }

        protected boolean include( Object toCheck)
            throws Exception
        {
            return ! ((DBMethod)toCheck).getCalledBy().hasMoreElements();
        }
    }

    static class UncalledPolymorphic extends UncalledMethod
    {
        AnalyzerDB db;
        public void startEvaluation( AnalyzerDB adb)
        {
            db=adb;
        }

        protected boolean include( Object toInclude)
            throws Exception
        {
            DBMethod method=(DBMethod)toInclude;
            boolean result=true;
            for ( Enumeration e=(new TransformSet( new SingleMethod( method), new Polymorphic())).execute( db);
                e.hasMoreElements() && result;)
            {
                if ( ((DBMethod)e.nextElement()).
                    getCalledBy().hasMoreElements())
                    result=false;
            }
            return result;
        }
    }

    static class SingleMethod extends SetExpression
    {
        DBMethod _method;

        SingleMethod( DBMethod c)
        {
            super( DBMethod.class);
            _method=c;
        }

        public Enumeration execute( AnalyzerDB db)
            throws Exception
        {
            return enumFromObject( _method);
        }
    }

    static public class ComboEnumeration extends BiEnumeration
    {
        private static Enumeration combined( Enumeration e1,
            Enumeration e2)
        {
            Vector tmp=new Vector( 2);
            tmp.addElement( e1);
            tmp.addElement( e2);
            return tmp.elements();
        }

        ComboEnumeration( Enumeration e1, Enumeration e2)
        {
            super( combined( e1, e2));
        }

        protected Enumeration getNextCurrent( Object base)
        {
            return (Enumeration)base;
        }
    }

    static public Enumeration enumFromObject( Object o1)
    {
if ( o1==null)
throw new IllegalStateException( "null passed to enumFromObject");
        Vector tmp=new Vector(1);
        tmp.addElement( o1);
        return tmp.elements();
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

    static public abstract class BiEnumeration implements Enumeration
    {
        private Enumeration _baseEnumeration;
        private Enumeration _currentEnumeration;

        public BiEnumeration( Enumeration base)
        {
            _baseEnumeration=base;
            _currentEnumeration=null;
        }

        private void setCurrentEnumeration()
        {
            while (  _currentEnumeration==null
                || ! _currentEnumeration.hasMoreElements())
            {
                if ( _baseEnumeration.hasMoreElements())
                    _currentEnumeration=getNextCurrent( _baseEnumeration.nextElement());
                else
                {
                    _currentEnumeration=null;
                    break;
                }
            }
        }

        protected abstract Enumeration getNextCurrent( Object base);

        public boolean hasMoreElements()
        {
            setCurrentEnumeration();
            return _currentEnumeration!=null;
        }

        public Object nextElement()
        {
            setCurrentEnumeration();
            if ( _currentEnumeration==null)
            {
                throw new NoSuchElementException();
            }
            return _currentEnumeration.nextElement();
        }
    }
}