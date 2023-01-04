/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_caseless;

%%

%public
%class Ccl_caseless
%int
%debug

%full
%caseless

%%

sb { return 1; }

[^] { /* default */ }
