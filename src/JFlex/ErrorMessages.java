/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.3.5                                                             *
 * Copyright (C) 1998-2001  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package JFlex;


/**
 * Central class for all kinds of JFlex messages.
 * 
 * [Is not yet used exclusively, but should]
 * 
 * TODO: this is silly, use resources instead
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
public interface ErrorMessages {

  String messages [] = {
    "Unterminated string at end of line.",
    "<<EOF>> must be followed by an action.",
    "<<EOF>> can only be used as a single rule.",
    "Unkown %-option",
    "Unexpected character ",
    "Unexpected character <newline>",
    "Lexical state has not been declared.",
    "State identifier expected.",
    "Illegal repeat statement. At least one operand must be > 0",
    "Illegal repeat statement."+Out.NL+"First operand must not be greater than second one.",
    "Regular expression expected.",
    "Macro has not been declared.",
    "Character set is too small for this class.",
    "Character set is too small for a charactor of this string.",
    "Character set is too small for this character.",
    "Macros in character classes are not supported.",
    "Syntax error.",
    "Syntax error.",
    "%-option is not at the beginning of the line.",
    "Didn't find matching bracket",
    "Unexpected end of file in action code",
    "Unexpected end of file in comment",
    "Unexpected end of file in string",
    "Unexpected end of file in macros",
    "Unexpected end of file in state list",
    "Unexpected end of file in regular expression",
    "Unexpected end of file (maybe missing \"*/\", \")\", \"]\" or \"}\")",
    "This seems not to be a lexical specification (first %% is missing)",
    "Last action in the specification must not be \"|\"",
    "End of expression matches beginning of trailing context",
    "No directory for -d option",
    "No file provided for -skel option",
    "Incorrect skeleton file (wrong number of sections)",
    "JFlex has run out of memory."+Out.NL+"Please try increasing the maximum JVM heap size",
    "%initthrow needs a list of (qualified) identifiers",
    "%eofthrow needs a list of (qualified) identifiers",
    "%yylexthrow needs a list of (qualified) identifiers",
    "Your scanner has zero states. Very funny.",
    "%buffer expects the size of the scanner buffer as decimal number",
    "Could not open file.",
    "Cyclic file inclusion.",
    "Error writing to file",
    "%scannerror needs a (qualified) identifier",
    "Rule can never be matched:",
    "%throws needs a list of (qualified) identifiers",
    "Unexpected newline in character class (closing \"]\" is missing)",
    "%cupsym needs a (qualified) identifier",
    "%cupsym should be used before %cup",
    "The generator is already running"
  };


  int UNTERMINATED_STR = 0;
  int EOF_WO_ACTION    = 1;
  int EOF_SINGLERULE   = 2;
  int UNKNOWN_OPTION   = 3;
  int UNEXPECTED_CHAR  = 4;
  int UNEXPECTED_NL    = 5;
  int LEXSTATE_UNDECL  = 6;
  int STATE_IDENT_EXP  = 7;
  int REPEAT_ZERO      = 8;
  int REPEAT_GREATER   = 9;
  int REGEXP_EXPECTED  = 10;
  int MACRO_UNDECL     = 11;
  int CHARSET_2_SMALL  = 12;
  int CS2SMALL_STRING  = 13;
  int CS2SMALL_CHAR    = 14;
  int CHARCLASS_MACRO  = 15;
  int UNKNOWN_SYNTAX   = 16;
  int SYNTAX_ERROR     = 17;
  int NOT_AT_BOL       = 18;
  int NO_MATCHING_BR   = 19;
  int EOF_IN_ACTION    = 20;
  int EOF_IN_COMMENT   = 21;
  int EOF_IN_STRING    = 22;
  int EOF_IN_MACROS    = 23;
  int EOF_IN_STATES    = 24;
  int EOF_IN_REGEXP    = 25;
  int UNEXPECTED_EOF   = 26;
  int NO_LEX_SPEC      = 27;
  int NO_LAST_ACTION   = 28;
  int LOOKAHEAD_ERROR  = 29;
  int NO_DIRECTORY     = 30;
  int NO_SKEL_FILE     = 31;
  int WRONG_SKELETON   = 32;
  int OUT_OF_MEMORY    = 33;
  int QUIL_INITTHROW   = 34;
  int QUIL_EOFTHROW    = 35;
  int QUIL_YYLEXTHROW  = 36;
  int ZERO_STATES      = 37;
  int NO_BUFFER_SIZE   = 38;
  int NOT_READABLE     = 39;
  int FILE_CYCLE       = 40;
  int FILE_WRITE       = 41;
  int QUIL_SCANERROR   = 42;
  int NEVER_MATCH      = 43;
  int QUIL_THROW       = 44;
  int EOL_IN_CHARCLASS = 45;
  int QUIL_CUPSYM      = 46;
  int CUPSYM_AFTER_CUP = 47;
  int ALREADY_RUNNING  = 48;
}
