package de.jflex.testcase.include_in_rules;

%%

%unicode
%public
%class IncludeInRulesScanner
%type int

%debug 

%%

"abc" { }
%include extra-jflex-rules.inc.jflex
"def" { }

[^] { }

<<EOF>> { return -1; }
