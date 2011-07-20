/**
 * Copyright (c) 2006,2010-2011 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.TransformerException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.Token;

import com.antlersoft.query.BasicBase;
import com.antlersoft.query.ParserEnvironment;
import com.antlersoft.query.SelectionSetExpression;
import com.antlersoft.query.SetExpression;
import com.antlersoft.query.Transform;

import com.antlersoft.util.xml.ElementTransformReader;
import com.antlersoft.util.xml.HandlerStack;
import com.antlersoft.util.xml.IElement;

/**
 * A wrapper for the parser that handles the state of the query engine.
 * 
 * @author Michael A. MacDonald
 *
 */
public class QueryLanguageEnvironment implements ParserEnvironment {
	
	public static final String ELEMENT_TAG="query_language_environment";
	
	public QueryLanguageEnvironment( BasicBase parser)
	{
		m_parser=parser;
		m_lexer=new Lexer( parser);
		parser.setParserEnvironment( this);
		storedValues=new HashMap<String,TokenSequence>();
		storedValuesSupport=new PropertyChangeSupport(this);
		m_sequence_stack=new Stack<TokenSequence>();
		m_selection = new SelectionSetExpression();
	}
	
    public SetExpression getExpression()
    throws ParseException
	{
	    currentIndex=0;
	    m_sequence_stack.clear();
	    m_sequence_stack.add( new TokenSequence());
	    boolean errorOut=false;
	    for (; ! errorOut && currentIndex<tokens.length; currentIndex++)
	    {
	    	Token token=tokens[currentIndex];
	    	m_sequence_stack.peek().addToken( token.symbol, token.value);
	    	m_parser.massageToken( token);
	        errorOut=m_parser.parse( token.symbol, token.value);
	    }
	    if ( errorOut)
	    {
	        ParseException pe=new ParseException( this);
	        m_parser.reset();
	        throw pe;
	    }
	    m_sequence_stack.pop();
	    return getLastParsedExpression();
	}
	
	public void setLine( String toParse)
	{
	    tokens=m_lexer.tokenize( toParse);
	}
	
	// Bean type interface for accessing stored value names
	public String[] getStoredValues()
	{
	    return (String[])storedValues.keySet().toArray( new String[0]);
	}
	
	public TokenSequence getStoredSequence( String name)
	{
		return (TokenSequence)storedValues.get( name);
	}
	
	public void addStoredValuesListener( PropertyChangeListener l)
	{
	    storedValuesSupport.addPropertyChangeListener( "storedValues", l);
	}
	
	public void removeStoredValuesListener( PropertyChangeListener l)
	{
	    storedValuesSupport.removePropertyChangeListener( l);
	}
	
	public void unsaveName( String name)
	{
		storedValues.remove( name);
		storedValuesSupport.firePropertyChange( "storedValues", null, this);
	}
	
	public SavingName startSavingName( String name, int token_count)
	{
		m_sequence_stack.push( new TokenSequence());
		return new SavingName(name, token_count);
	}
	
	public void finishSavingName( SavingName saved, Object value)
	{
		assert( m_sequence_stack.size()>1);
		TokenSequence seq=(TokenSequence)m_sequence_stack.pop();
		seq.setResult( value);
		seq.throwAwayClosingToken( m_parser.getReservedScope());
		storedValues.put( saved.m_name, seq);
		storedValuesSupport.firePropertyChange( "storedValues", null, this);
		// A saved transform converts, not to itself, but to an empty set expression,
		// represented by a single "list" token here
		if ( value instanceof Transform)
		{
			seq=new TokenSequence();
			seq.addToken( m_parser.getReservedScope().getReserved( "list"), "list");
		}
		((TokenSequence)m_sequence_stack.peek()).replaceTokensBySequence( saved.m_token_count, seq);
	}
	
	public Object getStoredValue( String name)
	{
		TokenSequence sequence=(TokenSequence)storedValues.get( name);
		if ( sequence==null)
			return null;
		return sequence.getResult();
	}
	
	public Object replaceWithStoredValue( String name, int token_count) {
		TokenSequence sequence=(TokenSequence)storedValues.get( name);
		if ( sequence==null)
			return null;
		((TokenSequence)m_sequence_stack.peek()).replaceTokensBySequence( token_count, sequence);
		return sequence.getResult();
	}
	
	/**
	 * Replace the existing collection of automatic query templates (if any)
	 * with a new collection
	 * @param topNode Top node in the tree of AutoQueryNodes representing
	 * hierarchical
	 */
	public void setAutoQueryNode(AutoQueryNode topNode)
	{
		m_topNode = topNode;
	}
	
	/**
	 * Get the top node of the tree of automatic query templates (if any)
	 * @return Top note in the tree of AutoQueryNodes
	 */
	public AutoQueryNode getAutoQueryNode()
	{
		return m_topNode;
	}
	
	/**
	 * Determine the automated queries for the type of the current selection, based
	 * on the current AutoQueryNode tree.
	 * @param type Key for the appropriate start node
	 * @return List of appropriate automated queries; list may be empty
	 * but won't be null
	 */
	public List<String> getAutoQueryList()
	{
		if (m_topNode == null || getCurrentSelection().getResultClass() == null)
			return new ArrayList<String>();
		List<String> result = m_topNode.getTemplatesForType(getCurrentSelection().getResultClass().toString());
		if (result == null)
			result = new ArrayList<String>();
		return result;
	}
	
	/**
	 * Read the XML corresponding to a QueryLanguageEnvironment environment from the specified Reader
	 * @param is Read over characters in XML file
	 * @throws IOException
	 * @throws QueryException
	 */
	public void readEnvironment( Reader is)
	throws IOException, QueryException
	{
		boolean successful=false;
		try
		{
			try
			{
				m_previous_sequence=null;
				SAXParser p=SAXParserFactory.newInstance().newSAXParser();
				HandlerStack stack=new HandlerStack( p.getXMLReader());
				DefaultHandler top=getElement().readFromXML( stack);
				stack.pushHandlerStack( top);
				p.parse( new InputSource( is), top);
			}
			catch ( ParserConfigurationException pce)
			{
				throw new QueryException( "Error in JRE XML Configuration", pce);
			}
			catch ( SAXException se)
			{
				throw new QueryException( "Format error reading environment", se);
			}
			try
			{
				if ( m_previous_sequence != null)
					restoreTokenSequenceResult( null, m_previous_sequence);
				for ( Iterator<Entry<String,TokenSequence>> i=storedValues.entrySet().iterator(); i.hasNext();)
				{
					Map.Entry<String,TokenSequence> entry=i.next();
					restoreTokenSequenceResult( entry.getKey(), entry.getValue());
				}
			}
			catch ( ClassNotFoundException cnfe)
			{
				throw new QueryException( "Class not found while restoring environment", cnfe);
			}
			successful=true;
		}
		finally
		{
			// Make sure everything is cleared on an exception
			if ( ! successful)
			{
				m_previous_sequence=null;
				storedValues.clear();
			}
			storedValuesSupport.firePropertyChange( "storedValues", null, this);
		}
	}
	
	public void writeEnvironment( Writer is)
	throws IOException, QueryException
	{
		try
		{
			ElementTransformReader.writeElement( getElement(), is);
		}
		catch ( SAXException se)
		{
			throw new QueryException( "Error writing environment", se);
		}
		catch ( TransformerException te)
		{
			throw new QueryException( "Error in JRE XML configuration", te);
		}
	}
	
	// Protected interface
	
	/**
	 * This will be called when reading or writing the environment to XML.
	 * Sub-classes should override to return an element that supports the sub-class
	 * data as well as the parents data.
	 * @return IElement object supporting reading or writing the environment to XML.
	 */
	protected IElement getElement()
	{
		return new EnvironmentElement( this);
	}
	
	/**
	 * Gives access to the collection of selected objects that can be accessed
	 * from the query parser as "selection"
	 * @return SetExpression over collection
	 */
	public SelectionSetExpression getCurrentSelection()
	{
		return m_selection;
	}

	// Package interface
	Token[] tokens;
	int currentIndex;
	HashMap<String,TokenSequence> storedValues; // String, TokenSequence
	PropertyChangeSupport storedValuesSupport;
	TokenSequence m_previous_sequence;
	
	Parser getParser()
	{
		return m_parser;
	}
	
	private BasicBase m_parser;
	private Stack<TokenSequence> m_sequence_stack;
	private Lexer m_lexer;
	private SelectionSetExpression m_selection;
	private AutoQueryNode  m_topNode;
	
	
	/**
	 * When a token sequence is stored in persistent storage, its result is not attached (that's
	 * the point of storing token sequences and not results).
	 * This method will parse the tokens in the sequence appropriately to restore it's result.
	 * @param name Name under which the sequence is to be stored (may be null)
	 * @param to_restore Token sequence to restore-- the result will contain the name of the type
	 * that is to be restored as the result.
	 */
	private void restoreTokenSequenceResult( String name, TokenSequence to_restore)
	throws ClassNotFoundException, ParseException
	{
		ArrayList<Token> restore_tokens=new ArrayList<Token>();
		// If the token sequence represents a transform, use special code to restore it
		String class_name=(String)to_restore.getResult();
		boolean is_transform=false;
		if ( Transform.class.isAssignableFrom( Class.forName( class_name)))
		{
			is_transform=true;
			restore_tokens.add( new Token( m_parser.getReservedScope().getReserved( "set"), "set"));
			restore_tokens.add( new Token( BasicBase.nameSymbol, name));
			restore_tokens.add( new Token( m_parser.getReservedScope().getReserved( "to"), "to"));
		}
		to_restore.collectTokens( m_parser.getReservedScope(), restore_tokens);
	    restore_tokens.add( new Token( Parser._end_, ""));
		m_parser.reset();
		tokens=restore_tokens.toArray( new Token[restore_tokens.size()]);
		SetExpression exp=getExpression();
		if ( is_transform)
		{
			to_restore.setResult( storedValues.get( name).getResult());
		}
		else
			to_restore.setResult( exp);
	}
	
	// Implementation of ParserEnvironment
	
	public SetExpression getLastParsedExpression() {
		SetExpression result=null;
		
		if ( m_previous_sequence!=null)
		{
			result=(SetExpression)m_previous_sequence.getResult();
			if ( m_sequence_stack.size()>0)
				m_sequence_stack.peek().replaceTokensBySequence( 1, m_previous_sequence);
		}
		return result;
	}

	public void setLastParsedExpression(SetExpression expr) {
		m_previous_sequence=m_sequence_stack.pop();
		m_previous_sequence.throwAwayClosingToken( m_parser.getReservedScope());
		m_previous_sequence.setResult( expr);
		m_sequence_stack.push( new TokenSequence());
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ParserEnvironment#getSelection()
	 */
	@Override
	public SetExpression getSelection() {
		return m_selection;
	}
}
