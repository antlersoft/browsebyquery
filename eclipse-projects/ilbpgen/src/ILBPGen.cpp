#include <fstream>
#include <map>
#include "ILBPLex.h"
#include "com/antlersoft/parser/BuildParse.h"
#include "com/antlersoft/parser/SeqStr.h"
#include "com/antlersoft/tokenize.h"

using namespace com::antlersoft::parser;
using namespace std;

map<string,int> matched_strings;
map<string,int> matched_symbols;
map<string,Sequence> defined_symbols;

const char* remap[]={
	".", "period",
	"...", "ellipsis",
	"(", "left_paren",
	"[", "left_square",
	"<", "left_angle",
	"{", "left_brace",
	"+", "plus",
	"-", "minus",
	"&", "ampersand",
	"*", "asterisk",
	"/", "slash",
	"!", "exclamation",
	",", "comma",
	":", "colon",
	"::", "member_op",
	"}", "right_brace",
	"]", "right_square",
	">", "right_angle",
	")", "right_paren",
	"=", "equals"
};

string nameFromString( string in)
{
	if ( in.length()>1 && in.at(0)=='.' && in.at(1)>='a' && in.at(1)<='z')
	{
		return in.substr( 1)+"Directive";
	}
	for ( size_t i=0; i<sizeof(remap)/sizeof(const char*); i+=2)
	{
		if ( in==remap[i])
			return remap[i+1];
	}
	if ( in.length()<100 && in.find( '.')!=string::npos)
	{
		char buf[100];
		for ( int i=0; i<in.length(); ++i)
		{
			char c=in.at(i);
			if ( c=='.')
				c='_';
			buf[i]=c;
		}
		buf[in.length()]=0;
		in=buf;
	}
	return in;
}

class MatchStringRuleAction : public RuleAction
{
	public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		matched_strings[*dynamic_cast<const SeqString*>(value_stack.last())]=1;
		return value_stack.last()->copy();
	}
};

class MatchNameRuleAction : public RuleAction
{
	public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		matched_symbols[*dynamic_cast<const SeqString*>(value_stack.last())]=1;
		return value_stack.last()->copy();
	}
};

class EmptyDefinitionRuleAction : public RuleAction
{
	public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		return new Sequence();
	}
};

/**
 * A definition_parts exists on the value_stack as a sequence of sequences, with each
 * sequence a sequence of matches.
 * 
 */ 
class DefinitionPartsCreateRuleAction : public RuleAction
{
	public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		return new Sequence( *value_stack.last());
	}	
};

/**
 * Add a match to a definition part
 */
class ExpandDefinitionPartRuleAction : public RuleAction
{
	public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		return (*dynamic_cast<const Sequence*>(value_stack.butLast().last())|*value_stack.last()).copy();
	}	
};

/**
 * Expand a definition_parts on the value stack
 */
class DefinitionPartsExpandRuleAction : public RuleAction
{
	public :
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		return (*dynamic_cast<const Sequence*>(value_stack.butLast().butLast().last())|*value_stack.last()).copy();
	}
};

/**
 * Define a symbol with all it's parts
 */
class DefineSymbolRuleAction : public RuleAction
{
	public:
	SeqData* ruleFire( void*& parse_data, Sequence value_stack) throw(RuleActionException)
	{
		string name= *
			dynamic_cast<const SeqString*>(
				value_stack.butLast().butLast().butLast().last()
			);
		cout<<"Defining symbol name = "<<name<<" with ";
		if ( defined_symbols.find(name)!=defined_symbols.end())
		{
			defined_symbols[name]=defined_symbols[name]|dynamic_cast<const Sequence&>( *value_stack.butLast().last());
		}
		else
		{
			defined_symbols[name]=dynamic_cast<const Sequence&>( *value_stack.butLast().last());
		}
		return new Sequence();
	}
};

map<string,string> id_type_map;

Sequence parseOpCode( string op_code)
{
	//list<string> codes;
	//tokenize<string, list<string> >( codes, op_code, '.');
	//Sequence result;
	//for ( list<string>::iterator i=codes.begin(); i!=codes.end(); ++i)
	//{
	//	if ( i!=codes.begin())
	//	{
	//		result=result|SeqString(".");
	//	}
	//	matched_strings[*i]=1;
	//	result=result|SeqString(*i);
	//}
	matched_strings[op_code]=1;
	return Sequence( SeqString(op_code));
}


const char* op_code_type_map[] = {
	"InlineBrTarget", "INSTR_BRTARGET",
	"ShortInlineBrTarget", "INSTR_BRTARGET",
	"InlineField", "INSTR_FIELD",
	"InlineI", "INSTR_I",
	"ShortInlineI", "INSTR_I",
	"InlineI8", "INSTR_I8",
	"InlineMethod", "INSTR_METHOD",
	"InlineNone", "INSTR_NONE",
	"InlinePhi", "INSTR_PHI",
	"InlineR", "INSTR_R",
	"ShortInlineR", "INSTR_R",
	"InlineRVA", "INSTR_RVA",
	"InlineSig", "INSTR_SIG",
	"InlineString", "INSTR_STRING",
	"InlineSwitch", "INSTR_SWITCH",
	"InlineTok", "INSTR_TOK",
	"InlineType", "INSTR_TYPE",
	"InlineVar", "INSTR_VAR",
	"ShortInlineVar", "INSTR_VAR"
};

void OpCodeDef( const char* op_code_id, const char* op_code_string, const char* op_code_type)
{
	if ( op_code_string=="unused")
		return;
	string upper_type(op_code_type);
	for ( int i=0; i<sizeof(op_code_type_map)/sizeof(const char*); i+=2)
	{
		if ( upper_type==op_code_type_map[i])
		{
			upper_type=op_code_type_map[i+1];
			break;
		}
	}
	id_type_map[op_code_id]=upper_type;
	defined_symbols[upper_type]=defined_symbols[upper_type]|Sequence(*parseOpCode(op_code_string).copy());
}

void OpCodeAlias( const char* op_code_id, const char* op_code_string)
{
	defined_symbols[id_type_map[op_code_id]]=defined_symbols[id_type_map[op_code_id]]|Sequence(*parseOpCode(op_code_string).copy());
}

#define OPDEF( op_code_id, op_code_string, a3, a4, op_code_type, a6, a7, a8, a9, a10) OpCodeDef( #op_code_id, op_code_string, #op_code_type);
#define OPALIAS(op_code_new_id, op_code_string, op_code_real_id) OpCodeAlias( #op_code_real_id, op_code_string);

void runOpCode()
{
#include "opcode.h"
}

void gen( istream& in_s, ostream& out_s)
{
	BuildParser ild_build;
	
	NonTerminalSymbol definitions("definitions");
	NonTerminalSymbol definition("definition");
	NonTerminalSymbol definition_part( "definition_part");
	NonTerminalSymbol definition_parts( "definitions_parts");
	NonTerminalSymbol match("match");
	
	TerminalSymbol colon(":");
	TerminalSymbol bar("|");
	TerminalSymbol semicolon(";");
	TerminalSymbol delimited_comment_start("/*");
	TerminalSymbol delimited_comment_end("*/");
	
	MatchStringRuleAction msra;
	MatchNameRuleAction mnra;
	
	ild_build.addRule( Rule( definition_part, CompareSequence(), *new EmptyDefinitionRuleAction()));
	ild_build.addRule( Rule( match, CompareSequence(ILBPLex::_name), mnra));
	ild_build.addRule( Rule( match, CompareSequence(ILBPLex::_str_const), msra));
	ild_build.addRule( Rule( definition_part, CompareSequence(definition_part)|match, *new ExpandDefinitionPartRuleAction()));
	ild_build.addRule( Rule( definition_parts, CompareSequence(definition_part), *new DefinitionPartsCreateRuleAction()));
	ild_build.addRule( Rule( definition_parts, CompareSequence(definition_parts)|bar|definition_part, *new DefinitionPartsExpandRuleAction()));
	ild_build.addRule( Rule( definition, CompareSequence(ILBPLex::_name)|colon|definition_parts|semicolon, *new DefineSymbolRuleAction()));
	ild_build.addRule( Rule( definitions, CompareSequence(definition)));
	ild_build.addRule( Rule( definitions, CompareSequence(definitions)|definition));
	
	ild_build.setGoal(definitions);
	
	Parser* parser=ild_build.build( cout);
	
	ILBPLex lex_scan(CompareSequence(),CompareSequence(colon)|bar|semicolon|delimited_comment_start|delimited_comment_end);
	
	lex_scan.setDelimComment( delimited_comment_start, delimited_comment_end);
	
	try
	{
		while ( in_s.good())
		{
			char buf[1000];
			in_s.getline( buf, 1000);
			std::string nl( buf);
			if ( ! in_s.eof())
			{
				lex_scan.addText( nl);
				lex_scan.addText( "\n");
				for( ;;)
				{
					std::string val;
					TerminalSymbol ts=lex_scan.nextToken( val);
					if ( ts==ILBPLex::_need_more)
						break;
					if ( parser->parse( static_cast<void*>(0), ts, SeqString( val)))
					{
						cout<<"Error on token ["<<val<<"]"<<endl;
						parser->reset();
					}
				}
			}
		}
		lex_scan.endOfInput();
		TerminalSymbol ts( ILBPLex::_need_more);
		do
		{                           
			std::string val;
			ts=lex_scan.nextToken( val);
			parser->parse( static_cast<void*>(0), ts, SeqString( val));
		}
		while ( ! ( ts==Parser::_end_));
	}
	catch ( exception& e)
	{
		cerr<<"Error parsing input file: "<<e.what()<<endl;
	}
	
	runOpCode();
	
	// Output the results
	for ( map<string,Sequence>::iterator i=defined_symbols.begin(); i!=defined_symbols.end(); ++i)
	{
		out_s<<"symbol n_"<<i->first<<endl;
	}
	for ( map<string,int>::iterator i=matched_strings.begin(); i!=matched_strings.end(); ++i)
	{
		out_s<<"reserved t_"<<nameFromString(i->first)<<" \""<<i->first<<"\"\n";
	}
	for ( map<string,int>::iterator i=matched_symbols.begin(); i!=matched_symbols.end(); ++i)
	{
		if ( defined_symbols.find(i->first)==defined_symbols.end())
		{
			out_s<<"reserved t_"<<i->first<<" \""<<i->first<<"\"\n";
		}
	}
	
	for ( map<string,Sequence>::iterator i=defined_symbols.begin(); i!=defined_symbols.end(); ++i)
	{
		for ( Sequence parts=i->second; parts.count()>0; parts=parts.butFirst())
		{
			out_s<<"n_"<<i->first<<" : ";
			for ( Sequence part(dynamic_cast<const Sequence&>(*parts.first())); part.count()>0; part=part.butFirst())
			{
				string sym=dynamic_cast<const SeqString&>( *part.first());
				if ( defined_symbols.find( sym)!=defined_symbols.end())
					out_s<<"n_"<<sym;
				else
					out_s<<"t_"<<nameFromString( sym);
				out_s<<" ";
			}
			out_s<<".\n";
		}
	}	
}

int main( int argc, char* argv[])
{
	ifstream in_s("src/ild.bpi");
	ofstream out_s("IldasmParser.bp");
	
	gen( in_s, out_s);
	
	return 0;
}

