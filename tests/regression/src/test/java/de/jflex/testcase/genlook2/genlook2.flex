/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.genlook2;

%%

%public
%class Genlook2
%integer
%debug

%unicode

%buffer 10

%states YYINITIAL

%%

<YYINITIAL> {    
  "a"+/"b"+    { /* match should never contain a 'b', which is lookahead only */ }

  [^]          { /* no lookahead */ }
}
