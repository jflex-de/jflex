/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

// Generated after suppress-all.flex. Check that %suppress does not apply and we get a warning.

%%

%class Warn
%int

%%

"a" { return 1; }
"a" { return 2; }
