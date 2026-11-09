/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testsuite;

import java.util.ArrayList;
import java.util.List;

/** The contents of a {@code .test} file, as read by {@link TestLoader}. */
class SuiteCase {

  /** Base name of the {@code .test}, {@code .flex} and golden files. */
  String name;

  String description;

  /** Command line options for JFlex, without the spec file. */
  List<String> jflexArgs = new ArrayList<>();

  boolean expectJFlexFail;

  String inputFileEncoding = "UTF-8";

  String outputFileEncoding = "UTF-8";

  /**
   * Input file for the scanner, relative to the test directory. If {@code null}, the scanner runs
   * on each {@code <name>-*.input} file instead.
   */
  String commonInputFile;
}
