/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

"abc" { /* never matches */ return 2; }
[ad][be]{LETTER} { return 3; }
