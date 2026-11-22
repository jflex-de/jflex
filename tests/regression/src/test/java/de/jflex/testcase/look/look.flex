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
%counter_methods

%unicode

%%

"ab"/"cde"  { return 2; }
"abcd"/"f"  { return 7; }

[^]         { return 0; }
