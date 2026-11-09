/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.no_suppress_warnings;

// will lead to compile error if JFlex emits its own @SuppressWarnings
@SuppressWarnings("all")
%%

%no_suppress_warnings
%class NoSuppress
%debug
%int

%%

a|b  { return 1; }
[^]  { return 0; }