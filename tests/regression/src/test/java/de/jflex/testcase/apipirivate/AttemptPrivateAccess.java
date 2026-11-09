/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.apipirivate;

import java.io.InputStreamReader;

public final class AttemptPrivateAccess {

  public static void main(String[] argv) {
    PrivateScanner s = new PrivateScanner(new InputStreamReader(System.in));
    s.yylex();
  }

  private AttemptPrivateAccess() {}
}
