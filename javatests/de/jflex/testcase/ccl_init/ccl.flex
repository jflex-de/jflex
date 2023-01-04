/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_init;

// see https://github.com/jflex-de/jflex/issues/986

%%

%public
%class Ccl
%int

x = ab
%unicode

%%

{x} { return 0; }
[^] { return 1; }
