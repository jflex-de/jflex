/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_macros;

%%

%public
%class Cclmacros
%int

%line
%column

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

A = [A]
NCD = [^CD]
C = [BC--{NCD}]
X = [{A}{C}]

// Macros for the rules that start with a digit. The digit corresponds to the action.
M = [a-f]
N = [d-i]
NN = [^{N}]
DEEP1 = [j-l]
DEEP2 = [{DEEP1}--k]
DEEP3 = [^{DEEP2}]
DIGIT = [:digit:]
UPPER = \p{Lu}

%%

{X}                              { /* X */ return 10; }
[{NCD}]                          { /* NCD */ return 11; }

[^]                              { /* default */ return 12; }

// M before other rules split up [a-f] into multiple classes
"0" [{M}]                        { return 0; }
// the set operations on macros
"1" [{M}&&{N}]                   { return 1; }
"2" [{M}||{N}]                   { return 2; }
"3" [{M}~~{N}]                   { return 3; }
"4" [^{M}]                       { return 4; }
// difference with a negated class
"5" [{M}--{NN}]                  { return 5; }
// macro in macro in macro
"6" [{DEEP3}&&[j-m]]             { return 6; }
// predefined class and Unicode property in a macro
"7" [{DIGIT}&&[\u0000-\u06FF]]   { return 7; }
"8" [{UPPER}&&[\u0000-\u00FF]]   { return 8; }
// M after other rules have split up [a-f] into multiple classes
"9" {M}                          { return 9; }
