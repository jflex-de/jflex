package de.jflex.testcase.negation;

%%

%public
%class Negation
%integer
%debug

%line
%column

%unicode

%%

([^]* [^a]) | !([^]* [^a])  { /* all */ }
aba   { /* should not match */ }
