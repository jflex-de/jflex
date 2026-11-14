/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_macro;
%%
%public
%class Lookmacro
%int

%line
%column

%{
  public int line() {
    return yyline;
  }

  public int column() {
    return yycolumn;
  }
%}

foo="<foo>"|":"
bar=([:letter:])+

D=[0-9]
D2=[{D}--[5-9]]

/* a regular expression without lookahead, for use with $ */
tail="#"|"!!"

%%

{bar}/{foo} { return 1; }
{D}+/{D2}   { return 2; }
{tail}$     { return 3; }
(.) { return 0; }
\R  { return 4; }
