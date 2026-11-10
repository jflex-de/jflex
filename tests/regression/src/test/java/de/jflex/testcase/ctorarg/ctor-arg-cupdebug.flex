/*
 * Copyright 2026, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

%%

%class CtorargCupdebug

%cup
%cupdebug

%ctorarg int x
%ctorarg Object y

%%

"some rule" { /* without content */ }
