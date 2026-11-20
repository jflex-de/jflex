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

{Identifier}     { return 1; }

{LineTerminator} |
<<EOF>>          { return 0; }

[^]              { /* no action */ }
