/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.3.5                                                             *
 * Copyright (C) 1998-2001  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software); you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY); without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program); if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package JFlex;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Central class for all kinds of JFlex messages.
 * 
 * [Is not yet used exclusively, but should]
 * 
 * @author Gerwin Klein
 * @version JFlex 1.4, $Revision$, $Date$
 */
public class ErrorMessages {
  
  private int key;
  
  private ErrorMessages(int key) {
    this.key = key;
  }

  public static String get(ErrorMessages msg) {
    return messages[msg.key]; 
  }

  // TODO: use resource bundle  
  private static final String BUNDLE_NAME = "JFlex.Message"; 

  private static final ResourceBundle RESOURCE_BUNDLE =
    ResourceBundle.getBundle(BUNDLE_NAME);

  private static String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  private static String messages [] = {
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
  
  // typesafe enumeration
  public static ErrorMessages UNTERMINATED_STR = new ErrorMessages(0);
  public static ErrorMessages EOF_WO_ACTION    = new ErrorMessages(1);
  public static ErrorMessages EOF_SINGLERULE   = new ErrorMessages(2);
  public static ErrorMessages UNKNOWN_OPTION   = new ErrorMessages(3);
  public static ErrorMessages UNEXPECTED_CHAR  = new ErrorMessages(4);
  public static ErrorMessages UNEXPECTED_NL    = new ErrorMessages(5);
  public static ErrorMessages LEXSTATE_UNDECL  = new ErrorMessages(6);
  public static ErrorMessages STATE_IDENT_EXP  = new ErrorMessages(7);
  public static ErrorMessages REPEAT_ZERO      = new ErrorMessages(8);
  public static ErrorMessages REPEAT_GREATER   = new ErrorMessages(9);
  public static ErrorMessages REGEXP_EXPECTED  = new ErrorMessages(10);
  public static ErrorMessages MACRO_UNDECL     = new ErrorMessages(11);
  public static ErrorMessages CHARSET_2_SMALL  = new ErrorMessages(12);
  public static ErrorMessages CS2SMALL_STRING  = new ErrorMessages(13);
  public static ErrorMessages CS2SMALL_CHAR    = new ErrorMessages(14);
  public static ErrorMessages CHARCLASS_MACRO  = new ErrorMessages(15);
  public static ErrorMessages UNKNOWN_SYNTAX   = new ErrorMessages(16);
  public static ErrorMessages SYNTAX_ERROR     = new ErrorMessages(17);
  public static ErrorMessages NOT_AT_BOL       = new ErrorMessages(18);
  public static ErrorMessages NO_MATCHING_BR   = new ErrorMessages(19);
  public static ErrorMessages EOF_IN_ACTION    = new ErrorMessages(20);
  public static ErrorMessages EOF_IN_COMMENT   = new ErrorMessages(21);
  public static ErrorMessages EOF_IN_STRING    = new ErrorMessages(22);
  public static ErrorMessages EOF_IN_MACROS    = new ErrorMessages(23);
  public static ErrorMessages EOF_IN_STATES    = new ErrorMessages(24);
  public static ErrorMessages EOF_IN_REGEXP    = new ErrorMessages(25);
  public static ErrorMessages UNEXPECTED_EOF   = new ErrorMessages(26);
  public static ErrorMessages NO_LEX_SPEC      = new ErrorMessages(27);
  public static ErrorMessages NO_LAST_ACTION   = new ErrorMessages(28);
  public static ErrorMessages LOOKAHEAD_ERROR  = new ErrorMessages(29);
  public static ErrorMessages NO_DIRECTORY     = new ErrorMessages(30);
  public static ErrorMessages NO_SKEL_FILE     = new ErrorMessages(31);
  public static ErrorMessages WRONG_SKELETON   = new ErrorMessages(32);
  public static ErrorMessages OUT_OF_MEMORY    = new ErrorMessages(33);
  public static ErrorMessages QUIL_INITTHROW   = new ErrorMessages(34);
  public static ErrorMessages QUIL_EOFTHROW    = new ErrorMessages(35);
  public static ErrorMessages QUIL_YYLEXTHROW  = new ErrorMessages(36);
  public static ErrorMessages ZERO_STATES      = new ErrorMessages(37);
  public static ErrorMessages NO_BUFFER_SIZE   = new ErrorMessages(38);
  public static ErrorMessages NOT_READABLE     = new ErrorMessages(39);
  public static ErrorMessages FILE_CYCLE       = new ErrorMessages(40);
  public static ErrorMessages FILE_WRITE       = new ErrorMessages(41);
  public static ErrorMessages QUIL_SCANERROR   = new ErrorMessages(42);
  public static ErrorMessages NEVER_MATCH      = new ErrorMessages(43);
  public static ErrorMessages QUIL_THROW       = new ErrorMessages(44);
  public static ErrorMessages EOL_IN_CHARCLASS = new ErrorMessages(45);
  public static ErrorMessages QUIL_CUPSYM      = new ErrorMessages(46);
  public static ErrorMessages CUPSYM_AFTER_CUP = new ErrorMessages(47);
  public static ErrorMessages ALREADY_RUNNING  = new ErrorMessages(48);
  
}
