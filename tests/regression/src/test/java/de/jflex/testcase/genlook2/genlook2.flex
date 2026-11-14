/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.genlook2;

%%

%public
%class Genlook2
%integer

%char

%unicode

%buffer 10

%states YYINITIAL

%{
  /** Offset of the current match from the start of input, counted from 0. */
  public long offset() {
    return yychar;
  }
%}

%%

<YYINITIAL> {
  "a"+/"b"+    { /* match should never contain a 'b', which is lookahead only */ return 1; }

  [^]          { /* no lookahead */ return 0; }
}
