/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.Enumeration;

/**
 * Combine, using the given set operator,
 * the result of applying the given transform to each element
 * of the input set
 * @author Michael A. MacDonald
 *
 */
public class CombineTransform extends Transform {

	private Transform _transform;
	private int _operatorType;
	/**
	 * Holds the result of the combined expressions processed so far
	 */
	private SetExpression _currentExpression;
	
	/**
	 * 
	 * @param operatorType Identifies set operator
	 * @param transform
	 */
	public CombineTransform(int operatorType, Transform transform) {
		_operatorType = operatorType;
		_transform = transform;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Transform#finishEvaluation(com.antlersoft.query.DataSource)
	 */
	@Override
	public Enumeration<?> finishEvaluation(DataSource source) {
		if (_currentExpression == null)
			return EmptyEnum.empty;
		return _currentExpression.evaluate(source);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Transform#startEvaluation(com.antlersoft.query.DataSource)
	 */
	@Override
	public void startEvaluation(DataSource source) {
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Transform#transformObject(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	@Override
	public Enumeration<?> transformObject(DataSource source, Object toTransform) {
		SelectionSetExpression sse = new SelectionSetExpression(resultClass());
		
		_transform.startEvaluation(source);
		Enumeration<?> e = _transform.transformObject(source, toTransform);
		while (e.hasMoreElements())
			sse.add(e.nextElement());
		e = _transform.finishEvaluation(source);
		while (e.hasMoreElements())
			sse.add(e.nextElement());
		if (_currentExpression == null)
			_currentExpression = sse;
		else
			try {
				_currentExpression = new SetOperatorExpression(_operatorType, _currentExpression, sse);
			} catch (BindException e1) {
			}
		
		return EmptyEnum.empty;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	@Override
	public Class appliesClass() {
		return _transform.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
	@Override
	public void lateBindApplies(Class newApplies) throws BindException {
		_transform.lateBindApplies(newApplies);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindResult(java.lang.Class)
	 */
	@Override
	public void lateBindResult(Class newResult) throws BindException {
		_transform.lateBindResult(newResult);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#resultClass()
	 */
	@Override
	public Class resultClass() {
		return _transform.resultClass();
	}

}
