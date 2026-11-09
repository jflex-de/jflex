/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import java.util.Vector;
import java.util.Map;

%%

%class Generics3<T>

%type <A extends String & Readable> Vector<A>
%ctorarg Map<Vector<Integer>, T> arg1

%%

[^] { /* something */ }
