/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import java.util.Vector;
import java.util.HashMap;

%%

%public
%class Generics4<T>

%ctorarg HashMap<Vector<Integer>, ? extends String> arg1
%standalone

%%

[a-zA-Z] { /* print everything else */ }
