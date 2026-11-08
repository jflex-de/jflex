/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

// Suppresses all warnings, so the second rule produces no "never matched" warning.

%%

%class SuppressAll
%int
%suppress all

%%

"a" { return 1; }
"a" { return 2; }
