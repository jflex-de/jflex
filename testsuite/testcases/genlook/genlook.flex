
%%

%public
%class Genlook
%integer
%debug

%line
%column

%unicode

%%

/* normal case */
"aa"|"a"/"a"+    { /* normal */ }

/* lookahead may be empty */
"bb"|"b"/"b"*    { /* empty-look */ }

"c"? { /* blah */ }

[^]         { }
