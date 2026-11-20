/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot;

%%

%public
%class Dot
%int

%%

"a" "b"*  { return 1; }
[^]       { return 0; }
