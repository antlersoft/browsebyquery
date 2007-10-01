/*---------------------------------------------------------------------------
 * Copyright 1995-2001 Michael A. MacDonald
 * This source code is licensed under the terms of the
 * Lesser Gnu Public License (see license.txt) and carries
 * NO WARRANTY
 *-------------------------------------------------------------------------*/
#include <cctype>
#include "ILBPLex.h"
#include "com/antlersoft/parser/Parser.h"

using namespace std;
using namespace com::antlersoft::parser;
    
TerminalSymbol ILBPLex::_name( "_name");
TerminalSymbol ILBPLex::_num_const( "_num_const");
TerminalSymbol ILBPLex::_str_const( "_str_const");
TerminalSymbol ILBPLex::_need_more( "_need_more");

void ILBPLex::reset()
	{
	while ( input.size())
		input.pop_front();
	end_of_input=0;
	next_char= -1;
	next_token=string();
	input_pos=0;
	state=ls_initial;
	current_line=1;
	current_position=1;
	}

int ILBPLex::getNextChar()
	{
	int next=0;
	while ( ! next)
		{
		if ( ! input.size())
			{
			if ( end_of_input)
				next= -2;
			else
				next= -1;
			}
		else
			{
			if ( input_pos>=input.front().size())
				{
				input_pos=0;
				input.pop_front();
				}
		    else	
				next= (input.front())[input_pos++];
			}
		}        
	if ( next=='\n')
		{
		++current_line;
		current_position=1;
		}
	else
		if ( next>=0)
			++current_position;
	return next;
	}

TerminalSymbol ILBPLex::nextToken( string& val,
	CompareSequence allowable)
	{                  
	val=string();   
	for ( ;;)
		{
		if ( next_char<0)
			{
			next_char=getNextChar();
			if ( next_char== -1)
				return _need_more;
			if ( next_char== -2)
				{
				if ( next_token==string())
					return Parser::_end_;
				else
					next_char= EOF;
				}
			}
		switch ( state)
			{
		case ls_initial :
			if ( next_char==EOF)
				return Parser::_end_;
			if ( isspace( next_char))
				{
				next_char= -1;
				continue;     
				}
			if ( isalpha( next_char) || next_char=='_')
				{
				next_token=string( 1, (char)next_char);
				next_char= -1;
				state=ls_in_name;
				continue;
				}
			if ( isdigit( next_char))
				{
				next_token=string( 1, (char)next_char);
				next_char= -1;
				state=ls_start_number;
				continue;
				}
			if ( next_char=='\'')
				{
				next_char= -1;
				state=ls_in_string;
				continue;
				}           
			if ( next_char=='.')
				{
				next_char= -1;
				state=ls_period;
				next_token=".";
				continue;
				}                     
			next_token=string( 1, (char)next_char);
			next_char= -1;
			state=ls_in_punc;
			continue;
		case ls_in_name :
			if ( isalnum( next_char) || next_char=='_')
				{
				next_token=next_token+string( 1, (char)next_char);
				next_char= -1;
				continue;
				}
			else
				{
				state=ls_initial;
				val=next_token;
				next_token=string();
				for ( CompareSequence s=name_tokens; s.count();
					s=s.butFirst())
					{
					if ( ((TerminalSymbol*)s.first())->name()==
						val)
						{
						if ( start_delim_comment== *((TerminalSymbol*)s.first()))
							{
							state=ls_delim_comment;
							continue;
							}
						if ( start_line_comment== *((TerminalSymbol*)s.first()))
							{
							state=ls_line_comment;
							continue;
							}
						return *((TerminalSymbol*)s.first());
						}
					}
				return _name;
				}
		case ls_in_string :
			if ( next_char==EOF)
				{
				val=next_token;
				state=ls_initial;
				next_token=string();
				return Parser::_error_;       
				}
			if ( next_char=='\'')
				{             
				val=next_token;
				next_char= -1;
				next_token=string();
				state=ls_initial;
				return _str_const;
				}
			if ( next_char=='\\')
				{
				state=ls_quoted_char_in_string;
				next_char= -1;
				continue;
				}
			else                  
				{
				next_token=next_token+string( 1, (char)next_char);
				next_char= -1;
				continue;
				}

		case ls_quoted_char_in_string :
			if ( next_char==EOF)
				{
				val=next_token;
				state=ls_initial;
				next_token=string();
				return Parser::_error_;       
				}
			else
				{
				next_token=next_token+string( 1, (char)next_char);
				next_char= -1;
				state=ls_in_string;
				continue;
				}

		case ls_start_number : 
			if ( isdigit( next_char))
				{
				next_token=next_token+string( 1, (char)next_char);
				next_char= -1;
				continue;
				}
			if ( next_char=='.')
				{
				next_token=next_token+string( '.', 1);
				next_char= -1;
				state=ls_end_number;
				continue;
				}
			state=ls_end_number;
			continue;
			
		case ls_end_number :
			if ( isdigit( next_char))
				{
				next_token=next_token+string( 1, (char)next_char);
				next_char= -1;
				continue;
				}
			val=next_token;
			next_token=string();
			state=ls_initial;
			return _num_const;
			
		case ls_period :
			if ( isdigit( next_char))
				{
				next_token=next_token+string( 1, (char)next_char);
				state=ls_end_number;
				next_char= -1;
				continue;
				}
			else
				{
				state=ls_in_punc;
				continue;
				}
				
		case ls_line_comment :
			if ( next_char=='\n' || next_char==EOF)
				{
				next_token=string();
				state=ls_initial;
				}                
			next_char= -1;
			continue;
			
		case ls_delim_comment :
			{
			int j=end_delim_comment.name().length();
			if ( next_token.length()>=j)
				{
				if ( next_token.substr( 0, j)==end_delim_comment.name())
					{
					next_token.erase( 0, j);
					if ( next_token.length())
						state=ls_in_punc;
					else
						state=ls_initial;
					}
				else
					next_token.erase( 0, 1);
				}
			else
				{
				if ( next_char==EOF)
					state=ls_initial;
				else
					{
					next_token=next_token+string( 1, (char)next_char);
					next_char= -1;
					}
				}                                                
			continue;
			}
				
		case ls_in_punc :
			if ( isalnum( next_char) || next_char=='\'' ||
				next_char==EOF || isspace( next_char))
				{	// End of punctuation token or tokens
				int tok_len=next_token.length();
				bool special_state=false;
				for ( CompareSequence s( punc_tokens); s.count();
					s=s.butFirst())
					{
					string tok=((TerminalSymbol*)s.first())->name();
					int len=tok.length();
					if ( len<=tok_len && tok==next_token.substr( 0, len))
						{
						val=tok;
						if ( len==tok_len)
							{
							next_token=string();
							state=ls_initial;
							}
						else
							next_token.erase( 0, len);
						if ( start_delim_comment== *((TerminalSymbol*)s.first()))
							{
							state=ls_delim_comment;
							special_state=true;
							break;
							}
						if ( start_line_comment== *((TerminalSymbol*)s.first()))
							{
							state=ls_line_comment;
							special_state=true;
							break;
							}
						return *(TerminalSymbol*)s.first();
						}
					}        
				if ( special_state)
					continue;
				val=next_token;
				state=ls_initial;
				next_token=string();
				return Parser::_error_;
				}             
			else
				{
				next_token=next_token+string( 1, (char)next_char);
				next_char= -1;
				continue;
				}
			}	/* End case */
		}
	}  



