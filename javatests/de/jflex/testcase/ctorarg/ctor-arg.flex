/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

%%

%class Ctorarg

%function test
%int

%ctorarg int x
%ctorarg Object y

%standalone

%%

"some rule" { /* without content */ }
