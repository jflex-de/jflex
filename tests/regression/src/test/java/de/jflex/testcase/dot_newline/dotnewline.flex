/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot_newline;

%%

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

%public
%class Dotnewline
%integer
%line
%column
%unicode

%%

.   { return 1; }

\R  { return 2; }

<<EOF>> { return YYEOF; }

[\uD800-\uDFFF] { /* unpaired surrogate */ return 3; }

[^] { /* should never get here: every char is matched by dot (.), (\R) or the surrogates */ return 0; }
