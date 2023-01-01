/*
 * Copyright 2023, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

// Negative test for char class macros. Should fail with an error message,
// not with an exception.

// See https://github.com/jflex-de/jflex/issues/888
// See https://github.com/jflex-de/jflex/issues/939

%%

%public
%class CclNeg
%int

// these are not a char classes, so should fail if included in char classes
M1 = a
M2 = "a"
M3 = [a] | [b]

%%

[{M1}] { return 1; }
[{M2}] { return 2; }
[{M3}] { return 3; }
[^] { return 0; }
