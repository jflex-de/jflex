
%%

%public
%class Eol
%integer
%debug

%line
%column

%unicode

%%

// should produce only three copies of the action
"aa"|"a" / "x"|"xy"|"zzz"|"zy"    { /* normal */ }

"bb"|"z" $   { /* eol */ }
"bb"$        { /* should produce nevermatch warning *}

"c" { /* blah */ }

[^]         { }
