/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * Collects the characters and strings that a spec uses in regular expressions and macros, and
 * checks that they are in the character set of the spec.
 *
 * <p>The check is separate from parsing, because the character set may be declared after a macro
 * that uses the character.
 *
 * <p>Only characters that are written in the spec are checked. Classes such as {@code [^a]}, are
 * allowed to be larger than the character set.
 */
final class SpecCharCheck {

  /** A character or string in the spec. */
  private static final class Use {
    final File file;
    final int line;
    final int column;
    final ErrorMessages message;

    /** The largest code point of the character or string. */
    final int maxCodePoint;

    Use(File file, int line, int column, ErrorMessages message, int maxCodePoint) {
      this.file = file;
      this.line = line;
      this.column = column;
      this.message = message;
      this.maxCodePoint = maxCodePoint;
    }
  }

  private final List<Use> uses = new ArrayList<>();

  /** Records a character at the given position of the spec. */
  void addChar(File file, int line, int column, int codePoint) {
    uses.add(new Use(file, line, column, ErrorMessages.CS2SMALL_CHAR, codePoint));
  }

  /** Records a string at the given position of the spec. */
  void addString(File file, int line, int column, String string) {
    int max = string.codePoints().max().orElse(0);
    uses.add(new Use(file, line, column, ErrorMessages.CS2SMALL_STRING, max));
  }

  /**
   * Reports an error for each recorded character or string that is not in the character set.
   *
   * @param maxCharCode the last character of the character set of the spec
   * @param out where to report the errors
   */
  void check(int maxCharCode, Out out) {
    for (Use use : uses) {
      if (use.maxCodePoint > maxCharCode) {
        out.error(use.file, use.message, use.line, use.column);
      }
    }
  }
}
