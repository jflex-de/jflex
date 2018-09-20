/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Central class for all kinds of JFlex messages.
 *
 * <p>[Is not yet used exclusively, but should]
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class ErrorMessages {
  private String key;

  /* not final static, because initializing here seems too early
   * for OS/2 JDK 1.1.8. See bug 1065521.
   */
  private static ResourceBundle resourceBundle = null;

  private ErrorMessages(String key) {
    this.key = key;
  }

  /**
   * Returns a localized representation of the error messages.
   *
   * @param msg a {@link jflex.ErrorMessages} object.
   * @return a {@link java.lang.String} representation of the errors.
   */
  public static String get(ErrorMessages msg) {
    if (resourceBundle == null) {
      resourceBundle = ResourceBundle.getBundle("jflex.Messages");
    }
    try {
      return resourceBundle.getString(msg.key);
    } catch (MissingResourceException e) {
      return '!' + msg.key + '!';
    }
  }

  /**
   * Returns an error message.
   *
   * @param msg a {@link jflex.ErrorMessages} containing the format string.
   * @return a {@link java.lang.String} object.
   */
  public static String get(ErrorMessages msg, Object... args) {
    return MessageFormat.format(get(msg), args);
  }

  // typesafe enumeration (generated, do not edit)
  /** Constant <code>UNTERMINATED_STR</code> */
  public static ErrorMessages UNTERMINATED_STR = new ErrorMessages("UNTERMINATED_STR");
  /** Constant <code>EOF_WO_ACTION</code> */
  public static ErrorMessages EOF_WO_ACTION = new ErrorMessages("EOF_WO_ACTION");
  /** Constant <code>UNKNOWN_OPTION</code> */
  public static ErrorMessages UNKNOWN_OPTION = new ErrorMessages("UNKNOWN_OPTION");
  /** Constant <code>UNEXPECTED_CHAR</code> */
  public static ErrorMessages UNEXPECTED_CHAR = new ErrorMessages("UNEXPECTED_CHAR");
  /** Constant <code>UNEXPECTED_NL</code> */
  public static ErrorMessages UNEXPECTED_NL = new ErrorMessages("UNEXPECTED_NL");
  /** Constant <code>LEXSTATE_UNDECL</code> */
  public static ErrorMessages LEXSTATE_UNDECL = new ErrorMessages("LEXSTATE_UNDECL");
  /** Constant <code>REPEAT_ZERO</code> */
  public static ErrorMessages REPEAT_ZERO = new ErrorMessages("REPEAT_ZERO");
  /** Constant <code>REPEAT_GREATER</code> */
  public static ErrorMessages REPEAT_GREATER = new ErrorMessages("REPEAT_GREATER");
  /** Constant <code>REGEXP_EXPECTED</code> */
  public static ErrorMessages REGEXP_EXPECTED = new ErrorMessages("REGEXP_EXPECTED");
  /** Constant <code>MACRO_UNDECL</code> */
  public static ErrorMessages MACRO_UNDECL = new ErrorMessages("MACRO_UNDECL");
  /** Constant <code>CHARSET_2_SMALL</code> */
  public static ErrorMessages CHARSET_2_SMALL = new ErrorMessages("CHARSET_2_SMALL");
  /** Constant <code>CS2SMALL_STRING</code> */
  public static ErrorMessages CS2SMALL_STRING = new ErrorMessages("CS2SMALL_STRING");
  /** Constant <code>CS2SMALL_CHAR</code> */
  public static ErrorMessages CS2SMALL_CHAR = new ErrorMessages("CS2SMALL_CHAR");
  /** Constant <code>CHARCLASS_MACRO</code> */
  public static ErrorMessages CHARCLASS_MACRO = new ErrorMessages("CHARCLASS_MACRO");
  /** Constant <code>UNKNOWN_SYNTAX</code> */
  public static ErrorMessages UNKNOWN_SYNTAX = new ErrorMessages("UNKNOWN_SYNTAX");
  /** Constant <code>SYNTAX_ERROR</code> */
  public static ErrorMessages SYNTAX_ERROR = new ErrorMessages("SYNTAX_ERROR");
  /** Constant <code>NOT_AT_BOL</code> */
  public static ErrorMessages NOT_AT_BOL = new ErrorMessages("NOT_AT_BOL");
  /** Constant <code>EOF_IN_ACTION</code> */
  public static ErrorMessages EOF_IN_ACTION = new ErrorMessages("EOF_IN_ACTION");
  /** Constant <code>EOF_IN_COMMENT</code> */
  public static ErrorMessages EOF_IN_COMMENT = new ErrorMessages("EOF_IN_COMMENT");
  /** Constant <code>EOF_IN_STRING</code> */
  public static ErrorMessages EOF_IN_STRING = new ErrorMessages("EOF_IN_STRING");
  /** Constant <code>EOF_IN_MACROS</code> */
  public static ErrorMessages EOF_IN_MACROS = new ErrorMessages("EOF_IN_MACROS");
  /** Constant <code>EOF_IN_STATES</code> */
  public static ErrorMessages EOF_IN_STATES = new ErrorMessages("EOF_IN_STATES");
  /** Constant <code>EOF_IN_REGEXP</code> */
  public static ErrorMessages EOF_IN_REGEXP = new ErrorMessages("EOF_IN_REGEXP");
  /** Constant <code>UNEXPECTED_EOF</code> */
  public static ErrorMessages UNEXPECTED_EOF = new ErrorMessages("UNEXPECTED_EOF");
  /** Constant <code>NO_LEX_SPEC</code> */
  public static ErrorMessages NO_LEX_SPEC = new ErrorMessages("NO_LEX_SPEC");
  /** Constant <code>NO_LAST_ACTION</code> */
  public static ErrorMessages NO_LAST_ACTION = new ErrorMessages("NO_LAST_ACTION");
  /** Constant <code>NO_DIRECTORY</code> */
  public static ErrorMessages NO_DIRECTORY = new ErrorMessages("NO_DIRECTORY");
  /** Constant <code>NO_SKEL_FILE</code> */
  public static ErrorMessages NO_SKEL_FILE = new ErrorMessages("NO_SKEL_FILE");
  /** Constant <code>WRONG_SKELETON</code> */
  public static ErrorMessages WRONG_SKELETON = new ErrorMessages("WRONG_SKELETON");
  /** Constant <code>OUT_OF_MEMORY</code> */
  public static ErrorMessages OUT_OF_MEMORY = new ErrorMessages("OUT_OF_MEMORY");
  /** Constant <code>QUIL_INITTHROW</code> */
  public static ErrorMessages QUIL_INITTHROW = new ErrorMessages("QUIL_INITTHROW");
  /** Constant <code>QUIL_EOFTHROW</code> */
  public static ErrorMessages QUIL_EOFTHROW = new ErrorMessages("QUIL_EOFTHROW");
  /** Constant <code>QUIL_YYLEXTHROW</code> */
  public static ErrorMessages QUIL_YYLEXTHROW = new ErrorMessages("QUIL_YYLEXTHROW");
  /** Constant <code>ZERO_STATES</code> */
  public static ErrorMessages ZERO_STATES = new ErrorMessages("ZERO_STATES");
  /** Constant <code>NO_BUFFER_SIZE</code> */
  public static ErrorMessages NO_BUFFER_SIZE = new ErrorMessages("NO_BUFFER_SIZE");
  /** Constant <code>NOT_READABLE</code> */
  public static ErrorMessages NOT_READABLE = new ErrorMessages("NOT_READABLE");
  /** Constant <code>FILE_CYCLE</code> */
  public static ErrorMessages FILE_CYCLE = new ErrorMessages("FILE_CYCLE");
  /** Constant <code>FILE_WRITE</code> */
  public static ErrorMessages FILE_WRITE = new ErrorMessages("FILE_WRITE");
  /** Constant <code>QUIL_SCANERROR</code> */
  public static ErrorMessages QUIL_SCANERROR = new ErrorMessages("QUIL_SCANERROR");
  /** Constant <code>NEVER_MATCH</code> */
  public static ErrorMessages NEVER_MATCH = new ErrorMessages("NEVER_MATCH");
  /** Constant <code>QUIL_THROW</code> */
  public static ErrorMessages QUIL_THROW = new ErrorMessages("QUIL_THROW");
  /** Constant <code>EOL_IN_CHARCLASS</code> */
  public static ErrorMessages EOL_IN_CHARCLASS = new ErrorMessages("EOL_IN_CHARCLASS");
  /** Constant <code>QUIL_CUPSYM</code> */
  public static ErrorMessages QUIL_CUPSYM = new ErrorMessages("QUIL_CUPSYM");
  /** Constant <code>CUPSYM_AFTER_CUP</code> */
  public static ErrorMessages CUPSYM_AFTER_CUP = new ErrorMessages("CUPSYM_AFTER_CUP");
  /** Constant <code>ALREADY_RUNNING</code> */
  public static ErrorMessages ALREADY_RUNNING = new ErrorMessages("ALREADY_RUNNING");
  /** Constant <code>CANNOT_READ_SKEL</code> */
  public static ErrorMessages CANNOT_READ_SKEL = new ErrorMessages("CANNOT_READ_SKEL");
  /** Constant <code>READING_SKEL</code> */
  public static ErrorMessages READING_SKEL = new ErrorMessages("READING_SKEL");
  /** Constant <code>SKEL_IO_ERROR</code> */
  public static ErrorMessages SKEL_IO_ERROR = new ErrorMessages("SKEL_IO_ERROR");
  /** Constant <code>SKEL_IO_ERROR_DEFAULT</code> */
  public static ErrorMessages SKEL_IO_ERROR_DEFAULT = new ErrorMessages("SKEL_IO_ERROR_DEFAULT");
  /** Constant <code>READING</code> */
  public static ErrorMessages READING = new ErrorMessages("READING");
  /** Constant <code>CANNOT_OPEN</code> */
  public static ErrorMessages CANNOT_OPEN = new ErrorMessages("CANNOT_OPEN");
  /** Constant <code>NFA_IS</code> */
  public static ErrorMessages NFA_IS = new ErrorMessages("NFA_IS");
  /** Constant <code>NFA_STATES</code> */
  public static ErrorMessages NFA_STATES = new ErrorMessages("NFA_STATES");
  /** Constant <code>DFA_TOOK</code> */
  public static ErrorMessages DFA_TOOK = new ErrorMessages("DFA_TOOK");
  /** Constant <code>DFA_IS</code> */
  public static ErrorMessages DFA_IS = new ErrorMessages("DFA_IS");
  /** Constant <code>MIN_TOOK</code> */
  public static ErrorMessages MIN_TOOK = new ErrorMessages("MIN_TOOK");
  /** Constant <code>MIN_DFA_IS</code> */
  public static ErrorMessages MIN_DFA_IS = new ErrorMessages("MIN_DFA_IS");
  /** Constant <code>WRITE_TOOK</code> */
  public static ErrorMessages WRITE_TOOK = new ErrorMessages("WRITE_TOOK");
  /** Constant <code>TOTAL_TIME</code> */
  public static ErrorMessages TOTAL_TIME = new ErrorMessages("TOTAL_TIME");
  /** Constant <code>IO_ERROR</code> */
  public static ErrorMessages IO_ERROR = new ErrorMessages("IO_ERROR");
  /** Constant <code>THIS_IS_JFLEX</code> */
  public static ErrorMessages THIS_IS_JFLEX = new ErrorMessages("THIS_IS_JFLEX");
  /** Constant <code>UNKNOWN_COMMANDLINE</code> */
  public static ErrorMessages UNKNOWN_COMMANDLINE = new ErrorMessages("UNKNOWN_COMMANDLINE");
  /** Constant <code>MACRO_CYCLE</code> */
  public static ErrorMessages MACRO_CYCLE = new ErrorMessages("MACRO_CYCLE");
  /** Constant <code>MACRO_DEF_MISSING</code> */
  public static ErrorMessages MACRO_DEF_MISSING = new ErrorMessages("MACRO_DEF_MISSING");
  /** Constant <code>PARSING_TOOK</code> */
  public static ErrorMessages PARSING_TOOK = new ErrorMessages("PARSING_TOOK");
  /** Constant <code>NFA_TOOK</code> */
  public static ErrorMessages NFA_TOOK = new ErrorMessages("NFA_TOOK");
  /** Constant <code>LOOKAHEAD_NEEDS_ACTION</code> */
  public static ErrorMessages LOOKAHEAD_NEEDS_ACTION = new ErrorMessages("LOOKAHEAD_NEEDS_ACTION");
  /** Constant <code>EMPTY_MATCH</code> */
  public static ErrorMessages EMPTY_MATCH = new ErrorMessages("EMPTY_MATCH");
  /** Constant <code>EMPTY_MATCH_LOOK</code> */
  public static ErrorMessages EMPTY_MATCH_LOOK = new ErrorMessages("EMPTY_MATCH_LOOK");
  /** Constant <code>CTOR_ARG</code> */
  public static ErrorMessages CTOR_ARG = new ErrorMessages("CTOR_ARG");
  /** Constant <code>CTOR_DEBUG</code> */
  public static ErrorMessages CTOR_DEBUG = new ErrorMessages("CTOR_DEBUG");
  /** Constant <code>INT_AND_TYPE</code> */
  public static ErrorMessages INT_AND_TYPE = new ErrorMessages("INT_AND_TYPE");
  /** Constant <code>UNSUPPORTED_UNICODE_VERSION</code> */
  public static ErrorMessages UNSUPPORTED_UNICODE_VERSION =
      new ErrorMessages("UNSUPPORTED_UNICODE_VERSION");
  /** Constant <code>UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE</code> */
  public static ErrorMessages UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE =
      new ErrorMessages("UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE");
  /** Constant <code>INVALID_UNICODE_PROPERTY</code> */
  public static ErrorMessages INVALID_UNICODE_PROPERTY =
      new ErrorMessages("INVALID_UNICODE_PROPERTY");
  /** Constant <code>DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS</code> */
  public static ErrorMessages DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS =
      new ErrorMessages("DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS");
  /** Constant <code>PROPS_ARG_REQUIRES_UNICODE_VERSION</code> */
  public static ErrorMessages PROPS_ARG_REQUIRES_UNICODE_VERSION =
      new ErrorMessages("PROPS_ARG_REQUIRES_UNICODE_VERSION");
  /** Constant <code>IMPOSSIBLE_CHARCLASS_RANGE</code> */
  public static ErrorMessages IMPOSSIBLE_CHARCLASS_RANGE =
      new ErrorMessages("IMPOSSIBLE_CHARCLASS_RANGE");
  /** Constant <code>CODEPOINT_OUT_OF_RANGE</code> */
  public static ErrorMessages CODEPOINT_OUT_OF_RANGE = new ErrorMessages("CODEPOINT_OUT_OF_RANGE");
  /** Constant <code>NO_ENCODING</code> */
  public static ErrorMessages NO_ENCODING = new ErrorMessages("NO_ENCODING");
  /** Constant <code>CHARSET_NOT_SUPPORTED</code> */
  public static ErrorMessages CHARSET_NOT_SUPPORTED = new ErrorMessages("CHARSET_NOT_SUPPORTED");
}
