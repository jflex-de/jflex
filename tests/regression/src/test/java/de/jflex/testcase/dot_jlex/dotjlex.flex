/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot_jlex;

%%

%public
%class Dotjlex
%integer
%unicode

%%

.     { return 1; }

\n    { return 2; }

[^]   { /* unpaired surrogate */ return 3; }
