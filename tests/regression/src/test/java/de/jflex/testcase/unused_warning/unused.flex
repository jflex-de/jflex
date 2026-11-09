/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unused_warning;

%%

%public
%class Unused
%integer
%debug

UNUSED = "an" "unused"+ "macro"

%%

[^]         { }
