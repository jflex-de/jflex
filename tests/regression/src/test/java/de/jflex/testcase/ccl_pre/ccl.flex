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

%debug
%int

%%

{Identifier}  { System.out.println("matched identifier ["+yytext()+"]"); }

. { System.out.println("Fallback ["+yytext()+"]"); }
