package com.antlersoft.analyzecxx;

import com.antlersoft.parser.*;

class PreprocessParser extends Parser
{
PreprocessParser()
{
	super( parseStates);
}static Symbol errorSymbol=_error_;
static ReservedWord lex_character_literal=ReservedWord.getReserved( "lex_character_literal");
static ReservedWord lex_include_header=ReservedWord.getReserved( "lex_include_header");
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
static Symbol ELIF_GROUPS=Symbol.get( "ELIF_GROUPS");
static Symbol ELIF_GROUP=Symbol.get( "ELIF_GROUP");
static Symbol ELSE_GROUP=Symbol.get( "ELSE_GROUP");
static Symbol ENDIF_LINE=Symbol.get( "ENDIF_LINE");
static Symbol GROUP=Symbol.get( "GROUP");
static Symbol GROUP_PART=Symbol.get( "GROUP_PART");
static Symbol IDENTIFIER_LIST=Symbol.get( "IDENTIFIER_LIST");
static Symbol IF_GROUP=Symbol.get( "IF_GROUP");
static Symbol IF_SECTION=Symbol.get( "IF_SECTION");
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
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( PP_FILE, 1),
 new GotoRule( GROUP, 2),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( PP_FILE, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( _end_, 16)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP_PART, 17),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
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
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 18),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 19)
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
new ParseState( new ShiftRule[] { new ShiftRule( pp_hash, 22)
},
new GotoRule[] { new GotoRule( ELIF_GROUPS, 20),
 new GotoRule( ELIF_GROUP, 21),
 new GotoRule( ENDIF_LINE, 23),
 new GotoRule( ELSE_GROUP, 24)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( pp_if, 25),
 new ShiftRule( pp_ifdef, 26),
 new ShiftRule( pp_ifndef, 27),
 new ShiftRule( pp_include, 28),
 new ShiftRule( pp_define, 29),
 new ShiftRule( pp_undef, 30),
 new ShiftRule( pp_line, 31),
 new ShiftRule( pp_error, 32),
 new ShiftRule( pp_pragma, 33),
 new ShiftRule( lex_new_line, 34)
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
new ParseState( new ShiftRule[] { new ShiftRule( pp_hash, 36)
},
new GotoRule[] { new GotoRule( ELSE_GROUP, 35),
 new GotoRule( ELIF_GROUP, 37),
 new GotoRule( ENDIF_LINE, 38)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELIF_GROUPS, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_elif, 39),
 new ShiftRule( pp_endif, 40),
 new ShiftRule( pp_else, 41)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_hash, 43)
},
new GotoRule[] { new GotoRule( ENDIF_LINE, 42)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( pp_defined, 46),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_EXPRESSION, 44),
 new GotoRule( CONSTANT_TOKEN, 45),
 new GotoRule( PP_TOKEN, 47)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 48)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 49)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_include_header, 50),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 51),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 52)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 53)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 54),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( lex_new_line, 56)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 55),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKENS, 57),
 new GotoRule( PP_TOKEN, 6)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_hash, 43)
},
new GotoRule[] { new GotoRule( ENDIF_LINE, 58)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( pp_else, 41),
 new ShiftRule( pp_elif, 39),
 new ShiftRule( pp_endif, 40)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ELIF_GROUPS, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_defined, 46),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_EXPRESSION, 59),
 new GotoRule( CONSTANT_TOKEN, 45),
 new GotoRule( PP_TOKEN, 47)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 60)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 61)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_endif, 40)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 62),
 new ShiftRule( pp_defined, 46),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_TOKEN, 63),
 new GotoRule( PP_TOKEN, 47)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_EXPRESSION, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 64),
 new ShiftRule( pp_left_paren, 65)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_TOKEN, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 66)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 67)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 68)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 69),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 19)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_lparen, 72)
},
new GotoRule[] { new GotoRule( REPLACEMENT_LIST, 70),
 new GotoRule( PP_TOKENS, 71),
 new GotoRule( PP_TOKEN, 6)
},
new ReduceRule( REPLACEMENT_LIST, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 73)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 74),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 19)
},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 75),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 19)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 76),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 19)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( IF_SECTION, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 77),
 new ShiftRule( pp_defined, 46),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( CONSTANT_TOKEN, 63),
 new GotoRule( PP_TOKEN, 47)
},
null),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( ENDIF_LINE, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP, 78),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( ELSE_GROUP, 3, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP, 79),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( IF_GROUP, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_EXPRESSION, 2, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONSTANT_TOKEN, 2, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 80)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP, 81),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( IF_GROUP, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP, 82),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( IF_GROUP, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 83)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11)
},
new GotoRule[] { new GotoRule( PP_TOKEN, 19)
},
new ReduceRule( REPLACEMENT_LIST, 1, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_identifier, 85),
 new ShiftRule( pp_rparen, 86)
},
new GotoRule[] { new GotoRule( IDENTIFIER_LIST, 84)
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
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP, 87),
 new GotoRule( GROUP_PART, 3),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( ELIF_GROUP, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP_PART, 17),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( ELSE_GROUP, 4, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP_PART, 17),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( IF_GROUP, 5, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_rparen, 88)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP_PART, 17),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( IF_GROUP, 5, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP_PART, 17),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( IF_GROUP, 5, null)
),
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( CONTROL_LINE, 5, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( pp_rparen, 89)
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
new GotoRule[] { new GotoRule( REPLACEMENT_LIST, 90),
 new GotoRule( PP_TOKENS, 71),
 new GotoRule( PP_TOKEN, 6)
},
new ReduceRule( REPLACEMENT_LIST, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 4),
 new ShiftRule( lex_identifier, 7),
 new ShiftRule( lex_number, 8),
 new ShiftRule( lex_character_literal, 9),
 new ShiftRule( lex_string_literal, 10),
 new ShiftRule( lex_preprocessing_op_or_punc, 11),
 new ShiftRule( pp_hash, 14)
},
new GotoRule[] { new GotoRule( GROUP_PART, 17),
 new GotoRule( PP_TOKENS, 5),
 new GotoRule( PP_TOKEN, 6),
 new GotoRule( IF_SECTION, 12),
 new GotoRule( IF_GROUP, 13),
 new GotoRule( CONTROL_LINE, 15)
},
new ReduceRule( ELIF_GROUP, 5, null)
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
new GotoRule[] { new GotoRule( REPLACEMENT_LIST, 91),
 new GotoRule( PP_TOKENS, 71),
 new GotoRule( PP_TOKEN, 6)
},
new ReduceRule( REPLACEMENT_LIST, 0, null)
),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 92)
},
new GotoRule[] {},
null),
new ParseState( new ShiftRule[] { new ShiftRule( lex_new_line, 93)
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
