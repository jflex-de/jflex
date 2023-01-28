/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.token_limit;

%%

%public
%class Limit_Success
%int

// size 8 (oct literal)
%token_size_limit 010

%%

// longest token that can be matched; should be matched even if buffer is not empty
"a"{8}       { return 0; }

// something to fill the buffer with
"b"          { return 1; }

// should result in EOFException when encountered in inptu
"c"{8} "c"+  { return 2; }

// fallback
[^]          { return 3; }
