/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eol_look;

%%

%public
%class Eol
%integer

%line
%column

%unicode

%{
  public int line() {
    return yyline;
  }

  public int column() {
    return yycolumn;
  }
%}

%%

// should produce only three copies of the action
"aa"|"a" / "x"|"xy"|"zzz"|"zy"    { /* normal */ return 1; }

"bb"|"z" $   { /* eol */ return 2; }
"bb"$        { /* should produce nevermatch warning */ return 3; }

"c" { /* blah */ return 4; }

[^]         { return 0; }
