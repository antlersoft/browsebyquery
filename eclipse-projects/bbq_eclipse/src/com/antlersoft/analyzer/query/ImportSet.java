package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import java.util.TreeSet;

import com.antlersoft.analyzer.AnalyzerDB;

import com.antlersoft.util.IteratorEnumeration;

class ImportSet extends SetExpression {
    private TreeSet m_set;
    ImportSet( TreeSet set)
    {
        super( String.class);
        m_set=set;
    }
    public Enumeration execute(AnalyzerDB db) throws java.lang.Exception {
        return new IteratorEnumeration( m_set.iterator());
    }
}