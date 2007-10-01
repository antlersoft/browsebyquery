#ifndef ILBPLEX_H_
#define ILBPLEX_H_

#pragma once

#include <deque>
#include <string>

#include <com/antlersoft/parser/ParseSym.h>
#include <com/antlersoft/parser/CmpSeq.h>

typedef std::deque<std::string> StringList;

class ILBPLex
	{                     
	private :
		StringList input;
		int end_of_input;
		enum { ls_initial,
			ls_in_name,
			ls_in_string,
			ls_in_punc,
			ls_start_number,
		    ls_period,
		    ls_end_number,
		    ls_line_comment,
		    ls_delim_comment,
			ls_quoted_char_in_string
		    } state;
		std::string next_token;
		com::antlersoft::parser::TerminalSymbol start_line_comment;
		com::antlersoft::parser::TerminalSymbol start_delim_comment;
		com::antlersoft::parser::TerminalSymbol end_delim_comment;
		unsigned int input_pos; 
		unsigned int current_position;
		unsigned int current_line;
		int next_char;  
		com::antlersoft::parser::CompareSequence name_tokens;
		com::antlersoft::parser::CompareSequence punc_tokens;
		
		int getNextChar();

		// Not defined; prevent attempt to copy, etc.
		ILBPLex( const ILBPLex&);

	public :
		static com::antlersoft::parser::TerminalSymbol _name;
		static com::antlersoft::parser::TerminalSymbol _str_const;
		static com::antlersoft::parser::TerminalSymbol _num_const;
		static com::antlersoft::parser::TerminalSymbol _need_more;
		
		ILBPLex(
			com::antlersoft::parser::CompareSequence n_name_tokens,
			com::antlersoft::parser::CompareSequence n_punc_tokens)
			: name_tokens( n_name_tokens),
			punc_tokens( n_punc_tokens),
			state( ls_initial),
            next_token(),
			start_line_comment( _need_more),
			start_delim_comment( _need_more),
			end_delim_comment( _need_more),
			end_of_input(0),
			input_pos(0),   
			current_line(1),
			current_position(1),
			next_char( -1)
			{};

		unsigned currentLine() { return current_line; };
		unsigned currentPosition() { return current_position; };			
		void addText( std::string to_add)
			{ input.push_back( to_add); };
		com::antlersoft::parser::TerminalSymbol nextToken( std::string& val,
			com::antlersoft::parser::CompareSequence allowable=com::antlersoft::parser::CompareSequence());
		void setLineComment( const com::antlersoft::parser::TerminalSymbol& ts)
			{ start_line_comment=ts; };
		void setDelimComment( const com::antlersoft::parser::TerminalSymbol& ts1,
			const com::antlersoft::parser::TerminalSymbol& ts2)
			{ start_delim_comment=ts1; end_delim_comment=ts2; }
		void reset();
		void endOfInput()
			{ end_of_input=1; };
	};

#endif /*ILBPLEX_H_*/
