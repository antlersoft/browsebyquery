package com.antlersoft.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class EmptyEnum implements Enumeration {
    public boolean hasMoreElements() {
		return false;
    }
    public Object nextElement() {
		throw new NoSuchElementException();
    }

	public static EmptyEnum empty=new EmptyEnum();
}