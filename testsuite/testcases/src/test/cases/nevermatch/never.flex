
%%

%class Never

%states A B

%%

<A,B> .    { /* action 1 */ }
a|b      { /* action 2 */ }

<A> .    { /* action */ }

<<EOF>>  { /* action 3 */ }

<A,B> <<EOF>> {  /* action 4 */ }

<A> <<EOF>>  { /* action 4 */ }