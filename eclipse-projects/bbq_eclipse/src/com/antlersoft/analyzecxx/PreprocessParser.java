package com.antlersoft.analyzecxx;

import java.util.List;
import java.util.Properties;

import com.antlersoft.parser.*;

class PreprocessParser extends Parser
{
    PreprocessParser( Properties initial_defines)
    {
	    super( parseStates);
    }
static Symbol errorSymbol=_error_;
static ReservedWord lex_character_literal=ReservedWord.getReserved( "lex_character_literal");
static ReservedWord lex_include_header=ReservedWord.getReserved( "lex_include_header");
static ReservedWord lex_include_header_with_current=ReservedWord.getReserved( "lex_include_header_with_current");
static ReservedWord lex_identifier=ReservedWord.getReserved( "lex_identifier");
static ReservedWord lex_preprocessing_op_or_punc=ReservedWord.getReserved( "lex_preprocessing_op_or_punc");
static ReservedWord lex_string_literal=ReservedWord.getReserved( "lex_string_literal");
static ReservedWord lex_white_space=ReservedWord.getReserved( "lex_white_space");
static ReservedWord lex_new_line=ReservedWord.getReserved( "lex_new_line");
static ReservedWord lex_number=ReservedWord.getReserved( "lex_number");
static ReservedWord pp_comma=ReservedWord.getReserved( "pp_comma");
static ReservedWord pp_define=ReservedWord.getReserved( "pp_define");
static ReservedWord pp_defined=ReservedWord.getReserved( "pp_defined");
static ReservedWord pp_elif=ReservedWord.getReserved( "pp_elif");
static ReservedWord pp_else=ReservedWord.getReserved( "pp_else");
static ReservedWord pp_endif=ReservedWord.getReserved( "pp_endif");
static ReservedWord pp_error=ReservedWord.getReserved( "pp_error");
static ReservedWord pp_hash=ReservedWord.getReserved( "#");
static ReservedWord pp_if=ReservedWord.getReserved( "pp_if");
static ReservedWord pp_ifdef=ReservedWord.getReserved( "pp_ifdef");
static ReservedWord pp_ifndef=ReservedWord.getReserved( "pp_ifndef");
static ReservedWord pp_include=ReservedWord.getReserved( "pp_include");
static ReservedWord pp_left_paren=ReservedWord.getReserved( "pp_left_paren");
static ReservedWord pp_line=ReservedWord.getReserved( "pp_line");
static ReservedWord pp_lparen=ReservedWord.getReserved( "pp_lparen");
static ReservedWord pp_pragma=ReservedWord.getReserved( "pp_pragma");
static ReservedWord pp_rparen=ReservedWord.getReserved( "pp_rparen");
static ReservedWord pp_undef=ReservedWord.getReserved( "pp_undef");
static Symbol CONSTANT_EXPRESSION=Symbol.get( "CONSTANT_EXPRESSION");
static Symbol CONSTANT_TOKEN=Symbol.get( "CONSTANT_TOKEN");
static Symbol CONTROL_LINE=Symbol.get( "CONTROL_LINE");
static Symbol ELIF_COND=Symbol.get( "ELIF_COND");
static Symbol ELIF_GROUPS=Symbol.get( "ELIF_GROUPS");
static Symbol ELIF_GROUP=Symbol.get( "ELIF_GROUP");
static Symbol ELSE_COND=Symbol.get( "ELSE_COND");
static Symbol ELSE_GROUP=Symbol.get( "ELSE_GROUP");
static Symbol ENDIF_LINE=Symbol.get( "ENDIF_LINE");
static Symbol GROUP=Symbol.get( "GROUP");
static Symbol GROUP_PART=Symbol.get( "GROUP_PART");
static Symbol IDENTIFIER_LIST=Symbol.get( "IDENTIFIER_LIST");
static Symbol IF_COND=Symbol.get( "IF_COND");
static Symbol IF_GROUP=Symbol.get( "IF_GROUP");
static Symbol IF_SECTION=Symbol.get( "IF_SECTION");
static Symbol INCLUDE_HEADER=Symbol.get( "INCLUDE_HEADER");
static Symbol PP_FILE=Symbol.get( "PP_FILE");
static Symbol PP_TOKEN=Symbol.get( "PP_TOKEN");
static Symbol PP_TOKENS=Symbol.get( "PP_TOKENS");
static Symbol REPLACEMENT_LIST=Symbol.get( "REPLACEMENT_LIST");
static ParseState[] parseStates={
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( PP_FILE, 1),
 new GotoRule( GROUP, 2),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( PP_FILE, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( _end_, 17)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP_PART, 18),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( PP_FILE, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( GROUP, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( GROUP_PART, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 19),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 20)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKENS, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( GROUP_PART, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_elif, 24),
 new ShiftRule( pp_endif, 26),
 new ShiftRule( pp_else, 28),
 new ShiftRule( pp_hash, 30)
},
new GotoRule[] { new GotoRule( ELIF_GROUPS, 21),
 new GotoRule( ELIF_GROUP, 22),
 new GotoRule( ELIF_COND, 23),
 new GotoRule( ENDIF_LINE, 25),
 new GotoRule( ELSE_GROUP, 27),
 new GotoRule( ELSE_COND, 29)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP, 31),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( IF_GROUP, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_if, 32),
 new ShiftRule( pp_ifdef, 33),
 new ShiftRule( pp_ifndef, 34),
 new ShiftRule( pp_include, 35),
 new ShiftRule( pp_define, 36),
 new ShiftRule( pp_undef, 37),
 new ShiftRule( pp_line, 38),
 new ShiftRule( pp_error, 39),
 new ShiftRule( pp_pragma, 40),
 new ShiftRule( lex_new_line, 41)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( GROUP_PART, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( _complete, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( GROUP, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( GROUP_PART, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( PP_TOKENS, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_else, 28),
 new ShiftRule( pp_hash, 30),
 new ShiftRule( pp_elif, 24),
 new ShiftRule( pp_endif, 26)
},
new GotoRule[] { new GotoRule( ELSE_GROUP, 42),
 new GotoRule( ELSE_COND, 29),
 new GotoRule( ELIF_GROUP, 43),
 new GotoRule( ELIF_COND, 23),
 new GotoRule( ENDIF_LINE, 44)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELIF_GROUPS, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP, 45),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( ELIF_GROUP, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_defined, 48),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_EXPRESSION, 46),
 new GotoRule( CONSTANT_TOKEN, 47),
 new GotoRule( PP_TOKEN, 49)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 50)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( pp_endif, 26)
},
new GotoRule[] { new GotoRule( ENDIF_LINE, 51)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 52)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELSE_GROUP, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_else, 53)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP_PART, 18),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( IF_GROUP, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_defined, 48),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_EXPRESSION, 54),
 new GotoRule( CONSTANT_TOKEN, 47),
 new GotoRule( PP_TOKEN, 49)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 55)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 56)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_include_header, 58),
 new ShiftRule( lex_include_header_with_current, 59),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( INCLUDE_HEADER, 57),
 new GotoRule( PP_TOKENS, 60),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 61)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 62)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 63),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( lex_new_line, 65)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 64),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 66),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_endif, 26)
},
new GotoRule[] { new GotoRule( ENDIF_LINE, 67)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELIF_GROUPS, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP_PART, 18),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( ELIF_GROUP, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 68),
 new ShiftRule( pp_defined, 48),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_TOKEN, 69),
 new GotoRule( PP_TOKEN, 49)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_EXPRESSION, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 70),
 new ShiftRule( pp_left_paren, 71)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ENDIF_LINE, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP, 72),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 73)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 74),
 new ShiftRule( pp_defined, 48),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_TOKEN, 69),
 new GotoRule( PP_TOKEN, 49)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 75)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 76)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 77)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( INCLUDE_HEADER, 1, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( INCLUDE_HEADER, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 20)
},
new ReduceRule( INCLUDE_HEADER, 1, new RuleAction() { public Object ruleFire( Parser parser, List valueStack) throws RuleActionException { throw new RuleActionException( "Unimplemented: Getting include header from substitution tokens"); }})
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_lparen, 80)
},
new GotoRule[] { new GotoRule( REPLACEMENT_LIST, 78),
 new GotoRule( PP_TOKENS, 79),
 new GotoRule( PP_TOKEN, 6)
},
new ReduceRule( REPLACEMENT_LIST, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 81)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 82),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 20)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 83),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 20)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 84),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 20)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELIF_COND, 3, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_EXPRESSION, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_TOKEN, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 85)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 15)
},
new GotoRule[] { new GotoRule( GROUP_PART, 18),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( IF_COND, 14),
 new GotoRule( CONTROL_LINE, 16)
},
new ReduceRule( ELSE_GROUP, 3, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELSE_COND, 3, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_COND, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_COND, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_COND, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 86)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 20)
},
new ReduceRule( REPLACEMENT_LIST, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 88),
 new ShiftRule( pp_rparen, 89)
},
new GotoRule[] { new GotoRule( IDENTIFIER_LIST, 87)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_rparen, 90)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 5, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_rparen, 91)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IDENTIFIER_LIST, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( REPLACEMENT_LIST, 92),
 new GotoRule( PP_TOKENS, 79),
 new GotoRule( PP_TOKEN, 6)
},
new ReduceRule( REPLACEMENT_LIST, 0, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_TOKEN, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( REPLACEMENT_LIST, 93),
 new GotoRule( PP_TOKENS, 79),
 new GotoRule( PP_TOKEN, 6)
},
new ReduceRule( REPLACEMENT_LIST, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 94)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 95)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 7, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 8, null)
)
};}
