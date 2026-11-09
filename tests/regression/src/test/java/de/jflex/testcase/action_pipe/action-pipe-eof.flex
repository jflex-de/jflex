/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.action_pipe;

%%

%public
%class ActionPipeEof

%int

LineTerminator = \r|\n|\r\n

Identifier = [:jletter:][:jletterdigit:]*


%%

{Identifier}     { System.out.println(yytext()); return 1; }

{LineTerminator} |
<<EOF>>          { System.out.println("⏎"); return 0; }

[^]              { /* no action */ }
