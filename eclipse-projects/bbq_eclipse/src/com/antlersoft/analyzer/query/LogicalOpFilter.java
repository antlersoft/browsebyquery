package com.antlersoft.analyzer.query;

import com.antlersoft.parser.ReservedWord;
import com.antlersoft.parser.RuleActionException;

public class LogicalOpFilter extends Filter
{
    private QueryParserBase.ReservedWord _op;
    private Filter _f1;
    private Filter _f2;

    public LogicalOpFilter( QueryParserBase.ReservedWord op,
        Filter f1, Filter f2)
        throws RuleActionException
    {
		super( null);
        _op=op;
        _f1=f1;
        _f2=f2;
        setFilterClass();
    }

    public void lateBind( Class _newFilterClass)
        throws RuleActionException
    {
        _f1.lateBind( _newFilterClass);
        _f2.lateBind( _newFilterClass);
        setFilterClass();
    }

    protected boolean include( Object o1)
        throws Exception
    {
        if ( _op==QueryParser.or)
        {
            return _f1.isIncluded( o1) || _f2.isIncluded( o1);
        }
        return _f1.isIncluded( o1) && _f2.isIncluded( o1);
    }

    private void setFilterClass()
        throws RuleActionException
    {
        Class f1Class=_f1.getFilterClass();
        Class f2Class=_f2.getFilterClass();
        if ( f1Class==null)
        {
            if ( f2Class==null)
                 return;
            _f1.lateBind( f2Class);
            f1Class=_f1.getFilterClass();
        }
        if ( f2Class==null)
        {
            _f2.lateBind( f1Class);
            f2Class=_f2.getFilterClass();
        }
        _filterClass=TransformSet.commonSubType( f1Class, f2Class, false);
    }
}
