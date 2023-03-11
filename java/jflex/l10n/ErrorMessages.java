/*
 * Copyright (C) 1998-2023  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.l10n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Central enum for all kinds of JFlex messages.
 *
 * <p>The enum values are expected to be keys in the {@code Messages.properties} resource bundle.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public enum ErrorMessages {
  UNTERMINATED_STR,
  EOF_WO_ACTION,
  UNKNOWN_OPTION,
  UNEXPECTED_CHAR,
  UNEXPECTED_NL,
  LEXSTATE_UNDECL,
  REPEAT_ZERO,
  REPEAT_GREATER,
  REGEXP_EXPECTED,
  MACRO_UNDECL,
  CHARSET_2_SMALL,
  CS2SMALL_STRING,
  CS2SMALL_CHAR,
  CHARCLASS_MACRO,
  UNKNOWN_SYNTAX,
  SYNTAX_ERROR,
  NOT_AT_BOL,
  EOF_IN_ACTION,
  EOF_IN_COMMENT,
  EOF_IN_STRING,
  EOF_IN_MACROS,
  EOF_IN_STATES,
  EOF_IN_REGEXP,
  UNEXPECTED_EOF,
  NO_LEX_SPEC,
  NO_LAST_ACTION,
  NO_DIRECTORY,
  NO_SKEL_FILE,
  WRONG_SKELETON,
  OUT_OF_MEMORY,
  QUIL_INITTHROW,
  QUIL_EOFTHROW,
  QUIL_YYLEXTHROW,
  ZERO_STATES,
  NO_BUFFER_SIZE,
  NOT_READABLE,
  FILE_CYCLE,
  FILE_WRITE,
  QUIL_SCANERROR,
  NEVER_MATCH,
  QUIL_THROW,
  EOL_IN_CHARCLASS,
  QUIL_CUPSYM,
  CUPSYM_AFTER_CUP,
  ALREADY_RUNNING,
  CANNOT_READ_SKEL,
  READING_SKEL,
  SKEL_IO_ERROR,
  SKEL_IO_ERROR_DEFAULT,
  READING,
  CANNOT_OPEN,
  NFA_IS,
  NFA_STATES,
  DFA_TOOK,
  DFA_IS,
  MIN_TOOK,
  MIN_DFA_IS,
  WRITE_TOOK,
  TOTAL_TIME,
  IO_ERROR,
  THIS_IS_JFLEX,
  UNKNOWN_COMMANDLINE,
  MACRO_CYCLE,
  MACRO_DEF_MISSING,
  PARSING_TOOK,
  NFA_TOOK,
  LOOKAHEAD_NEEDS_ACTION,
  EMPTY_MATCH,
  EMPTY_MATCH_LOOK,
  CTOR_ARG,
  CTOR_DEBUG,
  INT_AND_TYPE,
  UNSUPPORTED_UNICODE_VERSION,
  UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE,
  INVALID_UNICODE_PROPERTY,
  DOT_BAR_NEWLINE_DOES_NOT_MATCH_ALL_CHARS,
  PROPS_ARG_REQUIRES_UNICODE_VERSION,
  IMPOSSIBLE_CHARCLASS_RANGE,
  CODEPOINT_OUT_OF_RANGE,
  NO_ENCODING,
  CHARSET_NOT_SUPPORTED,
  DOUBLE_CHARSET,
  NOT_CHARCLASS,
  MACRO_UNUSED,
  UNKNOWN_WARNING,
  NOT_A_WARNING_ID,
  UNICODE_TOO_LONG,
  TOKEN_SIZE_LIMIT;

  private static final Set<ErrorMessages> configurableWarnings =
      new HashSet<>(
          Arrays.asList(
              NEVER_MATCH,
              EMPTY_MATCH,
              CTOR_DEBUG,
              NOT_AT_BOL,
              MACRO_UNUSED,
              CUPSYM_AFTER_CUP,
              UNICODE_TOO_LONG));

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
  public static String get(ErrorMessages msg) {
    if (resourceBundle == null) {
      resourceBundle = ResourceBundle.getBundle("jflex.Messages");
    }
    // not catching MissingResourceException -- if this fails, we want the
    // exception to reach the top level and be reported to the user as a bug.
    return resourceBundle.getString(msg.toString());
  }

  /**
   * Returns an error message.
   *
   * @param msg a {@link ErrorMessages} containing the format string.
   * @return a {@link java.lang.String} object.
   */
  public static String get(ErrorMessages msg, Object... args) {
    return MessageFormat.format(get(msg), args);
  }

  /**
   * Check whether a warning is configurable.
   *
   * @param msg the warning to check
   */
  public static boolean isConfigurableWarning(ErrorMessages msg) {
    return configurableWarnings.contains(msg);
  }
}
