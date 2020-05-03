/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.l10n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Central class for all kinds of JFlex messages.
 *
 * <p>[Is not yet used exclusively, but should]
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class ErrorMessages {

  private ErrorMessages() {}

  // typesafe enumeration (generated, do not edit)
  /** Constant {@code UNTERMINATED_STR} */
  public static ErrorMessage UNTERMINATED_STR = new ErrorMessage("UNTERMINATED_STR");
  /** Constant {@code EOF_WO_ACTION} */
  public static ErrorMessage EOF_WO_ACTION = new ErrorMessage("EOF_WO_ACTION");
  /** Constant {@code UNKNOWN_OPTION} */
  public static ErrorMessage UNKNOWN_OPTION = new ErrorMessage("UNKNOWN_OPTION");
  /** Constant {@code UNEXPECTED_CHAR} */
  public static ErrorMessage UNEXPECTED_CHAR = new ErrorMessage("UNEXPECTED_CHAR");
  /** Constant {@code UNEXPECTED_NL} */
  public static ErrorMessage UNEXPECTED_NL = new ErrorMessage("UNEXPECTED_NL");
  /** Constant {@code LEXSTATE_UNDECL} */
  public static ErrorMessage LEXSTATE_UNDECL = new ErrorMessage("LEXSTATE_UNDECL");
  /** Constant {@code REPEAT_ZERO} */
  public static ErrorMessage REPEAT_ZERO = new ErrorMessage("REPEAT_ZERO");
  /** Constant {@code REPEAT_GREATER} */
  public static ErrorMessage REPEAT_GREATER = new ErrorMessage("REPEAT_GREATER");
  /** Constant {@code REGEXP_EXPECTED} */
  public static ErrorMessage REGEXP_EXPECTED = new ErrorMessage("REGEXP_EXPECTED");
  /** Constant {@code MACRO_UNDECL} */
  public static ErrorMessage MACRO_UNDECL = new ErrorMessage("MACRO_UNDECL");
  /** Constant {@code CHARSET_2_SMALL} */
  public static ErrorMessage CHARSET_2_SMALL = new ErrorMessage("CHARSET_2_SMALL");
  /** Constant {@code CS2SMALL_STRING} */
  public static ErrorMessage CS2SMALL_STRING = new ErrorMessage("CS2SMALL_STRING");
  /** Constant {@code CS2SMALL_CHAR} */
  public static ErrorMessage CS2SMALL_CHAR = new ErrorMessage("CS2SMALL_CHAR");
  /** Constant {@code CHARCLASS_MACRO} */
  public static ErrorMessage CHARCLASS_MACRO = new ErrorMessage("CHARCLASS_MACRO");
  /** Constant {@code UNKNOWN_SYNTAX} */
  public static ErrorMessage UNKNOWN_SYNTAX = new ErrorMessage("UNKNOWN_SYNTAX");
  /** Constant {@code SYNTAX_ERROR} */
  public static ErrorMessage SYNTAX_ERROR = new ErrorMessage("SYNTAX_ERROR");
  /** Constant {@code NOT_AT_BOL} */
  public static ErrorMessage NOT_AT_BOL = new ErrorMessage("NOT_AT_BOL");
  /** Constant {@code EOF_IN_ACTION} */
  public static ErrorMessage EOF_IN_ACTION = new ErrorMessage("EOF_IN_ACTION");
  /** Constant {@code EOF_IN_COMMENT} */
  public static ErrorMessage EOF_IN_COMMENT = new ErrorMessage("EOF_IN_COMMENT");
  /** Constant {@code EOF_IN_STRING} */
  public static ErrorMessage EOF_IN_STRING = new ErrorMessage("EOF_IN_STRING");
  /** Constant {@code EOF_IN_MACROS} */
  public static ErrorMessage EOF_IN_MACROS = new ErrorMessage("EOF_IN_MACROS");
  /** Constant {@code EOF_IN_STATES} */
  public static ErrorMessage EOF_IN_STATES = new ErrorMessage("EOF_IN_STATES");
  /** Constant {@code EOF_IN_REGEXP} */
  public static ErrorMessage EOF_IN_REGEXP = new ErrorMessage("EOF_IN_REGEXP");
  /** Constant {@code UNEXPECTED_EOF} */
  public static ErrorMessage UNEXPECTED_EOF = new ErrorMessage("UNEXPECTED_EOF");
  /** Constant {@code NO_LEX_SPEC} */
  public static ErrorMessage NO_LEX_SPEC = new ErrorMessage("NO_LEX_SPEC");
  /** Constant {@code NO_LAST_ACTION} */
  public static ErrorMessage NO_LAST_ACTION = new ErrorMessage("NO_LAST_ACTION");
  /** Constant {@code NO_DIRECTORY} */
  public static ErrorMessage NO_DIRECTORY = new ErrorMessage("NO_DIRECTORY");
  /** Constant {@code NO_SKEL_FILE} */
  public static ErrorMessage NO_SKEL_FILE = new ErrorMessage("NO_SKEL_FILE");
  /** Constant {@code WRONG_SKELETON} */
  public static ErrorMessage WRONG_SKELETON = new ErrorMessage("WRONG_SKELETON");
  /** Constant {@code OUT_OF_MEMORY} */
  public static ErrorMessage OUT_OF_MEMORY = new ErrorMessage("OUT_OF_MEMORY");
  /** Constant {@code QUIL_INITTHROW} */
  public static ErrorMessage QUIL_INITTHROW = new ErrorMessage("QUIL_INITTHROW");
  /** Constant {@code QUIL_EOFTHROW} */
  public static ErrorMessage QUIL_EOFTHROW = new ErrorMessage("QUIL_EOFTHROW");
  /** Constant {@code QUIL_YYLEXTHROW} */
  public static ErrorMessage QUIL_YYLEXTHROW = new ErrorMessage("QUIL_YYLEXTHROW");
  /** Constant {@code ZERO_STATES} */
  public static ErrorMessage ZERO_STATES = new ErrorMessage("ZERO_STATES");
  /** Constant {@code NO_BUFFER_SIZE} */
  public static ErrorMessage NO_BUFFER_SIZE = new ErrorMessage("NO_BUFFER_SIZE");
  /** Constant {@code NOT_READABLE} */
  public static ErrorMessage NOT_READABLE = new ErrorMessage("NOT_READABLE");
  /** Constant {@code FILE_CYCLE} */
  public static ErrorMessage FILE_CYCLE = new ErrorMessage("FILE_CYCLE");
  /** Constant {@code FILE_WRITE} */
  public static ErrorMessage FILE_WRITE = new ErrorMessage("FILE_WRITE");
  /** Constant {@code QUIL_SCANERROR} */
  public static ErrorMessage QUIL_SCANERROR = new ErrorMessage("QUIL_SCANERROR");
  /** Constant {@code NEVER_MATCH} */
  public static ErrorMessage NEVER_MATCH = new ErrorMessage("NEVER_MATCH");
  /** Constant {@code QUIL_THROW} */
  public static ErrorMessage QUIL_THROW = new ErrorMessage("QUIL_THROW");
  /** Constant {@code EOL_IN_CHARCLASS} */
  public static ErrorMessage EOL_IN_CHARCLASS = new ErrorMessage("EOL_IN_CHARCLASS");
  /** Constant {@code QUIL_CUPSYM} */
  public static ErrorMessage QUIL_CUPSYM = new ErrorMessage("QUIL_CUPSYM");
  /** Constant {@code CUPSYM_AFTER_CUP} */
  public static ErrorMessage CUPSYM_AFTER_CUP = new ErrorMessage("CUPSYM_AFTER_CUP");
  /** Constant {@code ALREADY_RUNNING} */
  public static ErrorMessage ALREADY_RUNNING = new ErrorMessage("ALREADY_RUNNING");
  /** Constant {@code CANNOT_READ_SKEL} */
  public static ErrorMessage CANNOT_READ_SKEL = new ErrorMessage("CANNOT_READ_SKEL");
  /** Constant {@code READING_SKEL} */
  public static ErrorMessage READING_SKEL = new ErrorMessage("READING_SKEL");
  /** Constant {@code SKEL_IO_ERROR} */
  public static ErrorMessage SKEL_IO_ERROR = new ErrorMessage("SKEL_IO_ERROR");
  /** Constant {@code SKEL_IO_ERROR_DEFAULT} */
  public static ErrorMessage SKEL_IO_ERROR_DEFAULT = new ErrorMessage("SKEL_IO_ERROR_DEFAULT");
  /** Constant {@code READING} */
  public static ErrorMessage READING = new ErrorMessage("READING");
  /** Constant {@code CANNOT_OPEN} */
  public static ErrorMessage CANNOT_OPEN = new ErrorMessage("CANNOT_OPEN");
  /** Constant {@code NFA_IS} */
  public static ErrorMessage NFA_IS = new ErrorMessage("NFA_IS");
  /** Constant {@code NFA_STATES} */
  public static ErrorMessage NFA_STATES = new ErrorMessage("NFA_STATES");
  /** Constant {@code DFA_TOOK} */
  public static ErrorMessage DFA_TOOK = new ErrorMessage("DFA_TOOK");
  /** Constant {@code DFA_IS} */
  public static ErrorMessage DFA_IS = new ErrorMessage("DFA_IS");
  /** Constant {@code MIN_TOOK} */
  public static ErrorMessage MIN_TOOK = new ErrorMessage("MIN_TOOK");
  /** Constant {@code MIN_DFA_IS} */
  public static ErrorMessage MIN_DFA_IS = new ErrorMessage("MIN_DFA_IS");
  /** Constant {@code WRITE_TOOK} */
  public static ErrorMessage WRITE_TOOK = new ErrorMessage("WRITE_TOOK");
  /** Constant {@code TOTAL_TIME} */
  public static ErrorMessage TOTAL_TIME = new ErrorMessage("TOTAL_TIME");
  /** Constant {@code IO_ERROR} */
  public static ErrorMessage IO_ERROR = new ErrorMessage("IO_ERROR");
  /** Constant {@code THIS_IS_JFLEX} */
  public static ErrorMessage THIS_IS_JFLEX = new ErrorMessage("THIS_IS_JFLEX");
  /** Constant {@code UNKNOWN_COMMANDLINE} */
  public static ErrorMessage UNKNOWN_COMMANDLINE = new ErrorMessage("UNKNOWN_COMMANDLINE");
  /** Constant {@code MACRO_CYCLE} */
  public static ErrorMessage MACRO_CYCLE = new ErrorMessage("MACRO_CYCLE");
  /** Constant {@code MACRO_DEF_MISSING} */
  public static ErrorMessage MACRO_DEF_MISSING = new ErrorMessage("MACRO_DEF_MISSING");
  /** Constant {@code PARSING_TOOK} */
  public static ErrorMessage PARSING_TOOK = new ErrorMessage("PARSING_TOOK");
  /** Constant {@code NFA_TOOK} */
  public static ErrorMessage NFA_TOOK = new ErrorMessage("NFA_TOOK");
  /** Constant {@code LOOKAHEAD_NEEDS_ACTION} */
  public static ErrorMessage LOOKAHEAD_NEEDS_ACTION = new ErrorMessage("LOOKAHEAD_NEEDS_ACTION");
  /** Constant {@code EMPTY_MATCH} */
  public static ErrorMessage EMPTY_MATCH = new ErrorMessage("EMPTY_MATCH");
  /** Constant {@code EMPTY_MATCH_LOOK} */
  public static ErrorMessage EMPTY_MATCH_LOOK = new ErrorMessage("EMPTY_MATCH_LOOK");
  /** Constant {@code CTOR_ARG} */
  public static ErrorMessage CTOR_ARG = new ErrorMessage("CTOR_ARG");
  /** Constant {@code CTOR_DEBUG} */
  public static ErrorMessage CTOR_DEBUG = new ErrorMessage("CTOR_DEBUG");
  /** Constant {@code INT_AND_TYPE} */
  public static ErrorMessage INT_AND_TYPE = new ErrorMessage("INT_AND_TYPE");
  /** Constant {@code UNSUPPORTED_UNICODE_VERSION} */
  public static ErrorMessage UNSUPPORTED_UNICODE_VERSION =
      new ErrorMessage("UNSUPPORTED_UNICODE_VERSION");
  /** Constant {@code UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE} */
  public static ErrorMessage UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE =
      new ErrorMessage("UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE");
  /** Constant {@code INVALID_UNICODE_PROPERTY} */
  public static ErrorMessage INVALID_UNICODE_PROPERTY =
      new ErrorMessage("INVALID_UNICODE_PROPERTY");
  /** Constant {@code DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS} */
  public static ErrorMessage DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS =
      new ErrorMessage("DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS");
  /** Constant {@code PROPS_ARG_REQUIRES_UNICODE_VERSION} */
  public static ErrorMessage PROPS_ARG_REQUIRES_UNICODE_VERSION =
      new ErrorMessage("PROPS_ARG_REQUIRES_UNICODE_VERSION");
  /** Constant {@code IMPOSSIBLE_CHARCLASS_RANGE} */
  public static ErrorMessage IMPOSSIBLE_CHARCLASS_RANGE =
      new ErrorMessage("IMPOSSIBLE_CHARCLASS_RANGE");
  /** Constant {@code CODEPOINT_OUT_OF_RANGE} */
  public static ErrorMessage CODEPOINT_OUT_OF_RANGE = new ErrorMessage("CODEPOINT_OUT_OF_RANGE");
  /** Constant {@code NO_ENCODING} */
  public static ErrorMessage NO_ENCODING = new ErrorMessage("NO_ENCODING");
  /** Constant {@code CHARSET_NOT_SUPPORTED} */
  public static ErrorMessage CHARSET_NOT_SUPPORTED = new ErrorMessage("CHARSET_NOT_SUPPORTED");

  /* not final static, because initializing here seems too early
   * for OS/2 JDK 1.1.8. See bug 1065521.
   */
  private static ResourceBundle resourceBundle = null;

  /**
   * Returns a localized representation of the error messages.
   *
   * @param msg a {@link ErrorMessages} object.
   * @return a {@link java.lang.String} representation of the errors.
   */
  public static String get(ErrorMessage msg) {
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
   * @param msg a {@link ErrorMessages} containing the format string.
   * @return a {@link java.lang.String} object.
   */
  public static String get(ErrorMessages.ErrorMessage msg, Object... args) {
    return MessageFormat.format(get(msg), args);
  }

  public static class ErrorMessage {
    private final String key;

    private ErrorMessage(String key) {
      this.key = key;
    }
  }
}
