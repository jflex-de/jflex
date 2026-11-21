/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dead_end;

%%

%public
%class DeadEnd
%integer

%unicode

%%

// [] does match anything, so this rule never matches
"ab" [] "c"         { return 1; }

"a"                 { return 2; }

[^]                 { return 0; }
