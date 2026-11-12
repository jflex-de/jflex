/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look;

%%

%public
%class Look
%integer
%debug

%line
%column

%unicode

%{
  /** Line of the current match, counted from 0. */
  public int line() {
    return yyline;
  }

  /** Column of the current match, counted from 0. */
  public int column() {
    return yycolumn;
  }
%}

%%

"ab"/"cde"  { return 2; }
"abcd"/"f"  { return 7; }

[^]         { return 0; }
