/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.entry_states;

%%

%public
%class EntryTarget
%integer

%unicode

// no rules
%xstate X

%%

a*                  { return 1; }
