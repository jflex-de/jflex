/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;

%%

%class MacroCycle

A = "a" {B}
B = {A} "b"

%%

{A}  { /* some action */ }
