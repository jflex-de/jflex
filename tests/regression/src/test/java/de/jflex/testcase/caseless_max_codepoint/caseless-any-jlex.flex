/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_max_codepoint;

%%

%public
%class CaselessAnyJlex

%integer

// the maximum code point of Unicode 2.0 is U+FFFF
%unicode 2.0

%ignorecase

%%

k      { return 1; }

// all code points up to U+10FFFF
[^]    { return 0; }
