/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.no_warn;

%%

%class NoSuppress
%debug
%int

%warn all
%suppress never-match

%%

a|b  { return 1; }
a    { /* never-match warning */ }
[^]  { return 0; }