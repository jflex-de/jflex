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

%%

"ab"/"cde"  { System.out.println(yytext()); return 2; }
"abcd"/"f"  { System.out.println(yytext()); return 7; }

[^]         { }
