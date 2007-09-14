/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author Michael A. MacDonald
 *
 */
public class RegExpSubstitute implements ValueExpression {
	
	public RegExpSubstitute( ValueExpression match_string, ValueExpression pattern)
	throws BindException
	{
		m_to_match=match_string;
		m_pattern_value=pattern;
		m_context=new ResolvePairContext(match_string, pattern);
		m_binding=new ValueRegExpMatch.ResolveAppliesBinding(match_string, pattern);
	}

	public RegExpSubstitute( ValueExpression match_string, ValueExpression pattern, ValueExpression replacement)
	throws BindException
	{
		m_to_match=match_string;
		m_pattern_value=pattern;
		m_replacement_string=replacement;
		m_expression_pair=new ValuePair( pattern, replacement);
		m_context=new ResolvePairContext(match_string, m_expression_pair);
		m_binding=new ValueRegExpMatch.ResolveAppliesBinding(match_string, m_expression_pair);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueExpression#getValue()
	 */
	public Object getValue() {
		String next_pattern=m_pattern_value.getValue().toString();
		if ( m_pattern==null || ! m_pattern.pattern().equals( next_pattern))
		{
			m_pattern=Pattern.compile( next_pattern);
		}
		String result;
		Matcher matcher=m_pattern.matcher( m_to_match.getValue().toString());
		if ( m_replacement_string!=null)
			result=matcher.replaceAll(m_replacement_string.getValue().toString());
		else if ( matcher.find())
		{
			result=matcher.group();
		}
		else
			result="";
		return result;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getContext()
	 */
	public ValueContext getContext() {
		return m_context;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getValueCollection()
	 */
	public List getValueCollection() {
		return m_context.getValueCollection();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	public Class appliesClass() {
		return m_binding.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
	public void lateBindApplies(Class new_applies) throws BindException {
		m_binding.lateBindApplies( new_applies);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindResult(java.lang.Class)
	 */
	public void lateBindResult(Class new_result) throws BindException {
		if ( ! new_result.isAssignableFrom(String.class))
			throw new BindException( "Result of substitution is a string value");
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#resultClass()
	 */
	public Class resultClass() {
		return String.class;
	}
	
	private ValueExpression m_replacement_string;
	private ResolvePairContext m_context;
	private ValueRegExpMatch.ResolveAppliesBinding m_binding;
	private ValueExpression m_to_match, m_pattern_value;
	private Pattern m_pattern;
	private ValuePair m_expression_pair;
}
