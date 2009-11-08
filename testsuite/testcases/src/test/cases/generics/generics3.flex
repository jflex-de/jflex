import java.util.Vector;
import java.util.HashMap;

%%

%class Generics3<T>

%type <A extends String & Readable> Vector<A>
%ctorarg HashMap<Vector<Integer>, T> arg1

%%

[^] { /* something */ }
