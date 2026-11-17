/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_esc;

/** The rules of {@code ccl.flex}. */
enum Token {
  /** {@code "<"} in {@code YYINITIAL} */
  FIRST_TAG_OPEN,
  /** {@code "![CDATA["} */
  CDATA_START,
  /** <code>({ALPHA_NUMERIC}|["-_."])+</code> */
  NAME,
  /** {@code "="} */
  EQ,
  /** {@code "\""} in {@code IN_TAG} */
  QUOTE_OPEN,
  /** {@code "/"} */
  SLASH,
  /** {@code ">"} */
  TAG_CLOSE,
  /** {@code ([^\"])*} */
  VALUE,
  /** {@code "\""} in {@code IN_ATTRIBUTE} */
  QUOTE_CLOSE,
  /** {@code ([^<])*} */
  DATA,
  /** {@code "<"} in {@code IN_DATA} */
  TAG_OPEN,
  /** {@code "]]>"} */
  CDATA_END,
  /** {@code ([^"]>"]|"]"[^"]"]|[^"]"]">"|"]]"[^">"]|[^"]"]"]>")*} */
  CDATA,
  /** {@code [^]} */
  OTHER,
}
