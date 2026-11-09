/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import java.util.Vector;
import java.util.Map;

%%

%class Generics2

%type Vector<Integer>
%ctorarg Map<Vector<Integer>, String> arg1

%%

[^] { /* something */ }
