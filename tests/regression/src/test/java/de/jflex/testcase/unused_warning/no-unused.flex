/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.no_unused;

%%

%public
%class NoUnused
%integer
%debug

UNUSED = "an" "unused"+ "macro"

%%

[^]         { }
