/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.count;

%%

%public
%class Count
%integer

%char
%line
%column

%{
  /** Number of characters that the next action pushes back. Set by the test class. */
  public int pushback;

  /** Line of the current match, counted from 0. */
  public int line() {
    return yyline;
  }

  /** Column of the current match, counted from 0. */
  public int column() {
    return yycolumn;
  }

  /** Number of characters before the current match. */
  public long offset() {
    return yychar;
  }
%}

%%

[^] | [a-z]{2,3}  { yypushback(pushback); pushback = 0; return 1; }
