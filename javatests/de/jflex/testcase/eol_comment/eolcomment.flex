/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eol_comment;

%%

%public
%class EolComment

%standalone

%%

[^] { // should compile 
    }
