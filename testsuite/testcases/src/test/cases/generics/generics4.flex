import java.util.Vector;
import java.util.HashMap;

%%

%public
%class Generics4<T>

%ctorarg HashMap<Vector<Integer>, ? extends String> arg1
%standalone

%%

[a-zA-Z] { /* print everything else */ }
