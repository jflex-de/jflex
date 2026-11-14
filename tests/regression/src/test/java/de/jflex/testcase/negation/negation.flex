/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

%%

%public
%class Negation
%integer

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

([^]* [^a]) | !([^]* [^a])  { /* all */ return 1; }
aba   { /* should not match */ return 2; }
