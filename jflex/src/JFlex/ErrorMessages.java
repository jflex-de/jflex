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

  private String key;

  private static final ResourceBundle RESOURCE_BUNDLE =
    ResourceBundle.getBundle("JFlex.Messages");

  private ErrorMessages(String key) {
    this.key = key;
  }

  public static String get(ErrorMessages msg) {
    try {
      return RESOURCE_BUNDLE.getString(msg.key);
    } catch (MissingResourceException e) {
      return '!' + msg.key + '!';
    }
  }

  // typesafe enumeration  
  public final static ErrorMessages UNTERMINATED_STR = new ErrorMessages("UNTERMINATED_STR");
  public final static ErrorMessages EOF_WO_ACTION = new ErrorMessages("EOF_WO_ACTION");
  public final static ErrorMessages EOF_SINGLERULE = new ErrorMessages("EOF_SINGLERULE");
  public final static ErrorMessages UNKNOWN_OPTION = new ErrorMessages("UNKNOWN_OPTION");
  public final static ErrorMessages UNEXPECTED_CHAR = new ErrorMessages("UNEXPECTED_CHAR");
  public final static ErrorMessages UNEXPECTED_NL = new ErrorMessages("UNEXPECTED_NL");
  public final static ErrorMessages LEXSTATE_UNDECL = new ErrorMessages("LEXSTATE_UNDECL");
  public final static ErrorMessages STATE_IDENT_EXP = new ErrorMessages("STATE_IDENT_EXP");
  public final static ErrorMessages REPEAT_ZERO = new ErrorMessages("REPEAT_ZERO");
  public final static ErrorMessages REPEAT_GREATER = new ErrorMessages("REPEAT_GREATER");
  public final static ErrorMessages REGEXP_EXPECTED = new ErrorMessages("REGEXP_EXPECTED");
  public final static ErrorMessages MACRO_UNDECL = new ErrorMessages("MACRO_UNDECL");
  public final static ErrorMessages CHARSET_2_SMALL = new ErrorMessages("CHARSET_2_SMALL");
  public final static ErrorMessages CS2SMALL_STRING = new ErrorMessages("CS2SMALL_STRING");
  public final static ErrorMessages CS2SMALL_CHAR = new ErrorMessages("CS2SMALL_CHAR");
  public final static ErrorMessages CHARCLASS_MACRO = new ErrorMessages("CHARCLASS_MACRO");
  public final static ErrorMessages UNKNOWN_SYNTAX = new ErrorMessages("UNKNOWN_SYNTAX");
  public final static ErrorMessages SYNTAX_ERROR = new ErrorMessages("SYNTAX_ERROR");
  public final static ErrorMessages NOT_AT_BOL = new ErrorMessages("NOT_AT_BOL");
  public final static ErrorMessages NO_MATCHING_BR = new ErrorMessages("NO_MATCHING_BR");
  public final static ErrorMessages EOF_IN_ACTION = new ErrorMessages("EOF_IN_ACTION");
  public final static ErrorMessages EOF_IN_COMMENT = new ErrorMessages("EOF_IN_COMMENT");
  public final static ErrorMessages EOF_IN_STRING = new ErrorMessages("EOF_IN_STRING");
  public final static ErrorMessages EOF_IN_MACROS = new ErrorMessages("EOF_IN_MACROS");
  public final static ErrorMessages EOF_IN_STATES = new ErrorMessages("EOF_IN_STATES");
  public final static ErrorMessages EOF_IN_REGEXP = new ErrorMessages("EOF_IN_REGEXP");
  public final static ErrorMessages UNEXPECTED_EOF = new ErrorMessages("UNEXPECTED_EOF");
  public final static ErrorMessages NO_LEX_SPEC = new ErrorMessages("NO_LEX_SPEC");
  public final static ErrorMessages NO_LAST_ACTION = new ErrorMessages("NO_LAST_ACTION");
  public final static ErrorMessages LOOKAHEAD_ERROR = new ErrorMessages("LOOKAHEAD_ERROR");
  public final static ErrorMessages NO_DIRECTORY = new ErrorMessages("NO_DIRECTORY");
  public final static ErrorMessages NO_SKEL_FILE = new ErrorMessages("NO_SKEL_FILE");
  public final static ErrorMessages WRONG_SKELETON = new ErrorMessages("WRONG_SKELETON");
  public final static ErrorMessages OUT_OF_MEMORY = new ErrorMessages("OUT_OF_MEMORY");
  public final static ErrorMessages QUIL_INITTHROW = new ErrorMessages("QUIL_INITTHROW");
  public final static ErrorMessages QUIL_EOFTHROW = new ErrorMessages("QUIL_EOFTHROW");
  public final static ErrorMessages QUIL_YYLEXTHROW = new ErrorMessages("QUIL_YYLEXTHROW");
  public final static ErrorMessages ZERO_STATES = new ErrorMessages("ZERO_STATES");
  public final static ErrorMessages NO_BUFFER_SIZE = new ErrorMessages("NO_BUFFER_SIZE");
  public final static ErrorMessages NOT_READABLE = new ErrorMessages("NOT_READABLE");
  public final static ErrorMessages FILE_CYCLE = new ErrorMessages("FILE_CYCLE");
  public final static ErrorMessages FILE_WRITE = new ErrorMessages("FILE_WRITE");
  public final static ErrorMessages QUIL_SCANERROR = new ErrorMessages("QUIL_SCANERROR");
  public final static ErrorMessages NEVER_MATCH = new ErrorMessages("NEVER_MATCH");
  public final static ErrorMessages QUIL_THROW = new ErrorMessages("QUIL_THROW");
  public final static ErrorMessages EOL_IN_CHARCLASS = new ErrorMessages("EOL_IN_CHARCLASS");
  public final static ErrorMessages QUIL_CUPSYM = new ErrorMessages("QUIL_CUPSYM");
  public final static ErrorMessages CUPSYM_AFTER_CUP = new ErrorMessages("CUPSYM_AFTER_CUP");
  public final static ErrorMessages ALREADY_RUNNING = new ErrorMessages("ALREADY_RUNNING");
  public final static ErrorMessages SKEL_IO_ERROR_DEFAULT = new ErrorMessages("SKEL_IO_ERROR_DEFAULT");
  public final static ErrorMessages CANNOT_READ_SKEL = new ErrorMessages("CANNOT_READ_SKEL");
  public final static ErrorMessages READING_SKEL = new ErrorMessages("READING_SKEL");
  public final static ErrorMessages SKEL_IO_ERROR = new ErrorMessages("SKEL_IO_ERROR");
}
