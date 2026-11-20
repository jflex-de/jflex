/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_16;

%%

%public
%class CclScanner

%int

%16bit

%%

[^] { return 1; }
