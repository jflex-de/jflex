/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jlex;

%%

%public
%class Caseless

%standalone

%unicode

%ignorecase

NL = \r|\n|\r\n

%%

a          { System.out.println("--a--"+yytext()+"--"); }
[a-z]+     { System.out.println("--[a-z]+--"+yytext()+"--");  }
"hello"    { System.out.println("--"+yytext()+"--");   }

{NL}       { System.out.println("--newline--"); }
.          { System.out.println( "--"+yytext()+"--" ); }
