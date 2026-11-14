/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_pre;
%%
%16bit

Identifier = [\@]* [:jletter:] [:jletterdigit:]*

%public
%class Ccl

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

%%

{Identifier}  { return 1; }

. { return 0; }
