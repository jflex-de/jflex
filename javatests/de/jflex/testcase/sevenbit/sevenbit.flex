/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.sevenbit;

%%

%public
%class SevenBit

%7bit
%int

WhiteSpace = [ \t\r\n]

%%

{WhiteSpace}+   { return 0; }
[^]             { return 1; }
<<EOF>>         { return -1;}
