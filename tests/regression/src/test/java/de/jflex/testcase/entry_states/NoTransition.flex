/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.entry_states;

%%

%public
%class NoTransition
%integer

%unicode

%%

// this rule never matches because of []
a [] { return 1; }
