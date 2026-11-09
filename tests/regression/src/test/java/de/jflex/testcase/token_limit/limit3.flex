/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

%%

// should error -- illegal hex literal
%token_size_limit 0xABCDEFG

%%
