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

// an alternative of char classes is not a char class, so should fail if included
// in a char class
M = [a] | [b]

%%

[{M}] { return 1; }
[^] { return 0; }
