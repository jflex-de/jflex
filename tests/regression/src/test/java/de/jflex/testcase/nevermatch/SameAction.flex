/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.nevermatch;

%%

%public
%class SameAction
%integer

%unicode

%%

"a"      { return 1; }
"b"      { return 1; }

// never matches. We want a warning even though action text is equal to above.
"a"      { return 1; }

[^]      { return 0; }
