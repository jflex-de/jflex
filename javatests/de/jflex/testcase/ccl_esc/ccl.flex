/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_esc;

%%

%class Ccl
%type Token

%% 

"ab"  { return Token.AB;}
[^]+  { return Token.OTHER; }
