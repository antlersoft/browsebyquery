/*
 * Copyright (c) 1999-2007  Michael A. MacDonald
 * ----- - - -- - - --
 *     This file is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * 
 *     This file is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with the file (see COPYING); if not, see www.gnu.org
 * ----- - - -- - - --
 */
#include "com/antlersoft/parser/BuildParse.h"
#include "com/antlersoft/parser/LexScan.h"
#include "com/antlersoft/parser/SeqStr.h"
#include <cstdlib>
#include <string>

using namespace com::antlersoft::parser;
using namespace std;

static SymbolScope non_terminal_scope;
static SymbolScope terminal_scope;
static int java_goal_set=0;

// Placeholder action for generated rules
class RuleReduceAction : public RuleAction
{
private:
	string java_action;

public:
	RuleReduceAction( const string& ja) : java_action( ja) {};

	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		return value_stack.copy();
	}

	const string& getJavaAction()
	{
		return java_action;
	}
};

// Output code to initialize Java parse state
void writeParseState( ParseState& out_state)
{
	cout<<"new ParseState( new ShiftRule[] {";
	unsigned int count=0;
	for ( ShiftRuleIter sri( out_state.shift_rules.begin()); sri!=out_state.shift_rules.end(); sri++)
	{
		ShiftRule& sr= *sri;
		cout<<" new ShiftRule( "<<sr.looked_for.name()<<", "<<sr.state_index<<")";
		if ( count<out_state.shift_rules.size()-1)
			cout<<",";
		cout<<endl;
		count++;
	}
	cout<<"},\n";
	cout<<"new GotoRule[] {";
	count=0;
	for ( GotoRuleIter gri( out_state.goto_rules.begin()); gri!=out_state.goto_rules.end(); gri++)
	{
		GotoRule& sr= *gri;
		cout<<" new GotoRule( "<<sr.looked_for.name()<<", "<<sr.state_index<<")";
		if ( count<out_state.goto_rules.size()-1)
			cout<<",";
		cout<<endl;
		count++;
	}
	cout<<"},\n";
	if ( out_state.reduce_rule)
	{
		cout<<"new ReduceRule( "<<out_state.reduce_rule->result.name()<<", "<<out_state.reduce_rule->states_to_pop<<", ";
		if ( out_state.reduce_rule->reduce_action)
		{
			string java_action=static_cast<RuleReduceAction*>( out_state.reduce_rule->reduce_action)->getJavaAction();
			if ( java_action.c_str()[0]=='{')
				cout<<"new RuleAction() { public >>Object ruleFire( Parser parser, List valueStack) throws RuleActionException "<<static_cast<RuleReduceAction*>( out_state.reduce_rule->reduce_action)->getJavaAction()<<"}";
			else
				cout<<java_action;
		}
		else
			cout<<"null";
		cout<<")\n";
	}
	else
		cout<<"null";
	cout<<")";
}

int bigParserOutput( Parser* out_parser)
{
	ParseState* out_states=const_cast<ParseState*>(out_parser->getParseStates());
	size_t max_index=out_parser->getStateCount();
	for ( size_t index=0; index<max_index; index++)
	{
		cout<<"static ParseState get_ParseState"<<index<<"() { return\n";
		writeParseState( out_states[index]);
		cout<<";\n}\n";
	}
	cout<<"static ParseState[] parseStates={\n";
	for ( size_t index=0; index<max_index; ++index)
	{
		cout<<"get_ParseState"<<index<<"()";
		if ( index<max_index-1)
			cout<<",";
		cout<<"\n";
	}
	cout<<"};\n";
	return 0;
}

class ReservedWordDefAction : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		string word= *static_cast<const SeqString*>( value_stack.last());
		cout<<"static Symbol "<<word<<"=reservedScope.getReserved( \""<<word<<"\");\n";
		Symbol scope_symbol( terminal_scope, word);
		return new TerminalSymbol( word);
	}
};

class ReservedWordDefWithString : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		string text= *static_cast<const SeqString*>( value_stack.last());
		value_stack=value_stack.butLast();
		string word= *static_cast<const SeqString*>( value_stack.last());
		cout<<"static Symbol "<<word<<"=reservedScope.getReserved( \""<<text<<"\");\n";
		Symbol scope_symbol( terminal_scope, word);
		return new TerminalSymbol( word);
	}
};

class SymbolDefAction : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		string word= *static_cast<const SeqString*>( value_stack.last());
		Symbol scope_symbol( non_terminal_scope, word);
		cout<<"static Symbol "<<word<<"=Symbol.get( \""<<word<<"\");\n";
		return new NonTerminalSymbol( word);
	}
};

class AssociativitySetAction : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		Sequence associativity_seq=*static_cast<const Sequence*>( value_stack.last());
		AssocType assoc_type=assoc_none;
		string associativity_symbol=*static_cast<const SeqString*>( associativity_seq.first());
		if ( associativity_symbol==string( "left"))
			assoc_type=assoc_left;
		else if ( associativity_symbol==string( "right"))
			assoc_type=assoc_right;
		else if ( associativity_symbol==string( "not"))
			assoc_type=assoc_not;
		value_stack=value_stack.butLast();
		unsigned int precedence=(unsigned int)atoi( *static_cast<const SeqString*>( value_stack.last()));
		value_stack=value_stack.butLast();
		TerminalSymbol symbol=*static_cast<const TerminalSymbol*>( value_stack.last());
		symbol.setPrecedence( precedence, assoc_type);
		cout<<"// precedence "<<precedence<<" and associativity "<<associativity_symbol<<endl;
		return symbol.copy();
	}
};

class AddKnownSymbol : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		return new TerminalSymbol( *static_cast<const SeqString*>(value_stack.last()));
	}
};

class AddNamedSymbol : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		string symbol_name= *static_cast<const SeqString*>(value_stack.last());
		if ( non_terminal_scope.find( symbol_name)!=non_terminal_scope.end())
			return new NonTerminalSymbol( symbol_name);
		if ( terminal_scope.find( symbol_name)==terminal_scope.end())
		{
			cout<<"//Undefined symbol in rule: "<<symbol_name<<endl;
			Symbol( terminal_scope, symbol_name);
		}
		return new TerminalSymbol( symbol_name);
	}
};

class RuleDefAction : public RuleAction
{
private :
	BuildParser& build_parser;

public :
	RuleDefAction( BuildParser& bp)
		: build_parser( bp)
	{
	}
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		// Throw away rule_ender
		unsigned int precedence=static_cast<unsigned int>( atoi( *static_cast<const SeqString*>( value_stack.last())));
		value_stack=value_stack.butLast();
		CompareSequence partial_sequence= *static_cast<const CompareSequence*>( value_stack.last());
		const NonTerminalSymbol& target= *static_cast<const NonTerminalSymbol*>( partial_sequence.first());
		partial_sequence=partial_sequence.butFirst();
		Rule new_rule=Rule( target, partial_sequence);
		if ( precedence)
			new_rule.setPrecedence( precedence);
		build_parser.addRule( new_rule);
		if ( ! java_goal_set)
		{
			build_parser.setGoal( target);
			java_goal_set=1;
		}
		return new Sequence();		
	}
};

class RuleDefActionWithString : public RuleAction
{
private :
	BuildParser& build_parser;

public :
	RuleDefActionWithString( BuildParser& bp)
		: build_parser( bp)
	{
	}
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		// Throw away rule_ender
		unsigned int precedence=static_cast<unsigned int>( atoi( *static_cast<const SeqString*>( value_stack.last())));
		value_stack=value_stack.butLast();
		string java_action= *static_cast<const SeqString*>( value_stack.last());
		value_stack=value_stack.butLast();
		CompareSequence partial_sequence= *static_cast<const CompareSequence*>( value_stack.last());
		const NonTerminalSymbol& target= *static_cast<const NonTerminalSymbol*>( partial_sequence.first());
		partial_sequence=partial_sequence.butFirst();
		Rule new_rule=Rule( target, partial_sequence, *new RuleReduceAction( java_action));
		if ( precedence)
			new_rule.setPrecedence( precedence);
		build_parser.addRule( new_rule);
		if ( ! java_goal_set)
		{
			build_parser.setGoal( target);
			java_goal_set=1;
		}
		return new Sequence();		
	}
};

class RuleStartAction : public RuleAction
{
public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		value_stack=value_stack.butLast();
		return new CompareSequence( NonTerminalSymbol( *static_cast<const SeqString*>( value_stack.last())));
	}
};

class ExtendPartialAction : public RuleAction
{
public:
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		const ParseSymbol& added_symbol= *static_cast<const ParseSymbol*>( value_stack.last());
		value_stack=value_stack.butLast();
		CompareSequence partial_sequence= *static_cast<const CompareSequence*>( value_stack.last());
		return ( partial_sequence|added_symbol).copy();
	}
};

class SetPrecedenceAction : public RuleAction
{
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		value_stack=value_stack.butLast();
		SeqData* result=value_stack.last()->copy();
		value_stack=value_stack.butLast();
		return result;
	}
};

class NoPrecedenceAction : public RuleAction
{
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		value_stack=value_stack.butLast();
		return new SeqString( "0");
	}
};

int main( int argc, char* argv[])
	{
	BuildParser x;
	BuildParser java_parser;
	x.setDebug( 0);
	java_parser.setDebug( 0);

	ReservedWordDefAction rwda;
	ReservedWordDefWithString rwdws;
	AssociativitySetAction asa;
	SymbolDefAction sda;
	RuleStartAction rsa;
	AddKnownSymbol aks;
	AddNamedSymbol ans;
	SetPrecedenceAction set_precedence;
	NoPrecedenceAction no_precedence;
	ExtendPartialAction epa;
	RuleDefAction rda( java_parser);
	RuleDefActionWithString rdaws( java_parser);

	NonTerminalSymbol ReservedWordDef( "ReservedWordDef");
	NonTerminalSymbol SymbolDef( "SymbolDef");
	NonTerminalSymbol RuleDef( "RuleDef");
	NonTerminalSymbol Associativity( "Associativity");
	NonTerminalSymbol RulePrecedence( "RulePrecedence");
	NonTerminalSymbol Rules( "Rules");
	NonTerminalSymbol Defs( "Defs");
	NonTerminalSymbol CompleteParser( "CompleteParser");
	NonTerminalSymbol AnySymbol( "AnySymbol");
	NonTerminalSymbol PartialRule( "PartialRule");
	TerminalSymbol reserved( "reserved");
	TerminalSymbol symbol( "symbol");
	TerminalSymbol left( "left");
	TerminalSymbol right( "right");
	TerminalSymbol none( "none");
	TerminalSymbol Not( "not");
	TerminalSymbol colon( ":");
	TerminalSymbol rule_ender( ".");
	TerminalSymbol statement_divider( ";");
	TerminalSymbol name( "nameSymbol");
	TerminalSymbol string( "literalString");
	TerminalSymbol number( "number");
	TerminalSymbol errorSymbol( "errorSymbol");
	TerminalSymbol line_comment( "#");
	x.addRule( Rule( ReservedWordDef, CompareSequence( reserved)|LexScan::_name, rwda));
	x.addRule( Rule( ReservedWordDef, CompareSequence( reserved)|LexScan::_str_const, rwda));
	x.addRule( Rule( ReservedWordDef, CompareSequence( reserved)|LexScan::_name|LexScan::_str_const, rwdws));
	x.addRule( Rule( ReservedWordDef, CompareSequence( ReservedWordDef)|LexScan::_num_const|Associativity, asa));
	x.addRule( Rule( Associativity, CompareSequence(left)));
	x.addRule( Rule( Associativity, CompareSequence(right)));
	x.addRule( Rule( Associativity, CompareSequence(none)));
	x.addRule( Rule( Associativity, CompareSequence(Not)));
	x.addRule( Rule( RulePrecedence, CompareSequence( rule_ender), no_precedence));
	x.addRule( Rule( RulePrecedence, CompareSequence( LexScan::_num_const)|rule_ender, set_precedence));
	x.addRule( Rule( SymbolDef, CompareSequence( symbol)|LexScan::_name, sda));
	x.addRule( Rule( AnySymbol, CompareSequence( name), aks));
	x.addRule( Rule( AnySymbol, CompareSequence( number), aks));
	x.addRule( Rule( AnySymbol, CompareSequence( string), aks));
	x.addRule( Rule( AnySymbol, CompareSequence( errorSymbol), aks));
	x.addRule( Rule( AnySymbol, CompareSequence( LexScan::_name), ans));
	x.addRule( Rule( PartialRule, CompareSequence( LexScan::_name)|colon, rsa));
	x.addRule( Rule( PartialRule, CompareSequence( PartialRule)|AnySymbol, epa));
	x.addRule( Rule( RuleDef, CompareSequence( PartialRule)|LexScan::_str_const|RulePrecedence, rdaws));
	x.addRule( Rule( RuleDef, CompareSequence( PartialRule)|RulePrecedence, rda));
	x.addRule( Rule( Rules, CompareSequence( RuleDef)));
	x.addRule( Rule( Rules, CompareSequence( Rules)|RuleDef));
	x.addRule( Rule( Defs, CompareSequence()));
	x.addRule( Rule( Defs, CompareSequence( Defs)|ReservedWordDef));
	x.addRule( Rule( Defs, CompareSequence( Defs)|SymbolDef));
	x.addRule( Rule( CompleteParser, CompareSequence( Defs)|Rules));
	x.addRule( Rule( CompleteParser, CompareSequence( CompleteParser)|Defs|Rules));
	x.setGoal( CompleteParser);
	Parser* parser=x.build(cerr);
	LexScan lex_scan(
		// Reserved words for lexical scanning
		CompareSequence( reserved)|symbol|left|right|none|Not|name|string|number|errorSymbol,      
		// Tokens composed of punctuation
		CompareSequence( colon)|statement_divider|rule_ender|line_comment);
	lex_scan.setLineComment( line_comment);
	cout.flush();

	cout<<"static Symbol errorSymbol=_error_;\n";
	try
	{
		while ( cin.good())
			{
			char buf[1000];
			cin.getline( buf, 1000);
			std::string nl( buf);
			if ( ! cin.eof())
				{
				lex_scan.addText( nl);
				lex_scan.addText( "\n");
				for( ;;)
					{
					std::string val;
					TerminalSymbol ts=lex_scan.nextToken( val);
					if ( ts==LexScan::_need_more)
						break;
					if ( ts==statement_divider)
						ts=Parser::_end_;
					if ( parser->parse( static_cast<void*>(0), ts, SeqString( val)))
						{
						cout<<"Error on token ["<<val<<"]"<<endl;
						parser->reset();
						}
					}
				}
			}
		lex_scan.endOfInput();
	}
	catch ( exception& e)
	{
		cerr<<"Error parsing input file: "<<e.what()<<endl;
		return -1;
	}

	try
	{
		TerminalSymbol ts( LexScan::_need_more);
		do
			{                           
			std::string val;
			ts=lex_scan.nextToken( val);
			parser->parse( static_cast<void*>(0), ts, SeqString( val));
			}
		while ( ! ( ts==Parser::_end_));
		Parser* out_parser=java_parser.build( cerr);
		if ( out_parser->getStateCount()>300)
		{
			return bigParserOutput( out_parser);
		}
		cout<<"static ParseState[] parseStates={\n";
		ParseState* out_states=const_cast<ParseState*>(out_parser->getParseStates());
		for ( int index=0, max_index=out_parser->getStateCount(); index<max_index; index++)
		{
			ParseState& out_state=out_states[index];
			writeParseState( out_state);

			if ( index<max_index-1)
				cout<<",";

			cout<<endl;
		}
	}
	catch ( exception& e2)
	{
		cerr<<"Error building parser from input: "<<e2.what()<<endl;
	}
	cout<<"};";

	return 0;
	}
