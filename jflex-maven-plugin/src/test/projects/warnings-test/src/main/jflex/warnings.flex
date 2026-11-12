/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

// Produces each warning that the plugin can switch off.

%%

%class Warnings
%cup
%cupsym sym
%debug
%ctorarg int x
 %line

UNUSED = "u"

%%

"a"     { }
"a"     { int neverMatched; }
""      { }
\u00302 { }
