import java.util.Vector;
import java.util.HashMap;

%%

%class Generics2

%type Vector<Integer>
%ctorarg HashMap<Vector<Integer>, String> arg1

%%

[^] { /* something */ }
