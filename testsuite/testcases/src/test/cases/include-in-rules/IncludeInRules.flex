
%%

%unicode
%public
%class IncludeInRules
%type int

%debug 

%%

"abc" { }
%include extra.jflex-rules
"def" { }

[^] { }

<<EOF>> { return -1; }
