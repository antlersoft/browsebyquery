package com.antlersoft.query;

public interface Bindable {
	public Class resultClass();
	public Class appliesClass();
	public void lateBindResult( Class new_result) throws BindException;
	public void lateBindApplies( Class new_applies) throws BindException;
}