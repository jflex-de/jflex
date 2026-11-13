/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.fixed_look;

%%

%public
%class Fixedlook
%integer

%line
%column

%unicode

%{
  /** Set by the test class. */
  public boolean pushback;

  /** Action of the lookahead rules, optionally with pushing back one character. */
  private int look(int rule) {
    if (pushback) {
      yypushback(1);
    }
    return rule;
  }

  public int line() {
    return yyline;
  }

  public int column() {
    return yycolumn;
  }
%}

%%

"ab"+/"cde"  { return look(2); }
"abcd"+/"f"  { return look(7); }
"a"+ / "aa"  { return look(1); }
"bb" / "b"+  { return look(3); }
"ab"/"cde"+  { return look(4); }

[^]         { return 0; }
