package de.jflex.testcase.generics;

import java.util.Vector;
import java.util.Map;

%%

%class Generics3<T>

%type <A extends String & Readable> Vector<A>
%ctorarg Map<Vector<Integer>, T> arg1

%%

[^] { /* something */ }
