/*
 * Copyright 2023, Gerwin Klein
 * Copyright 2022, https://github.com/hellotommmy
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_table;

// Creates an exponentially large scanner (79k states minimised)
// Should still not lead to an exception in emitter phase.
// see https://github.com/jflex-de/jflex/issues/952

%%

%public
%class Large
%int

%%

((a|b)*b.{10}){3} { return 1; }

[^] { return 0; }
