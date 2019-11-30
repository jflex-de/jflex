package jflex.testcase.generics;

import java.util.Vector;
import java.util.Map;

%%

%class Generics2

%type Vector<Integer>
%ctorarg Map<Vector<Integer>, String> arg1

%%

[^] { /* something */ }
