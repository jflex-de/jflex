/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.count;

%%

%public
%class Count

%standalone

%char
%line
%column

%%

[^] | [a-z]{2,3}  { System.out.println("line: "+yyline+", column: "+yycolumn+", char: "+yychar); 
	                System.out.println("match ["+yytext()+"]"); }

