package jflex.testcase.ccl_macros;

%%

%public
%class Cclmacros
%int
%debug

A = [A]
NCD = [^CD]
C = [BC--{NCD}]
X = [{A}{C}]

%%

{X} { /* X */ }
[{NCD}] { /* NCD */ }

[^] { /* default */ }
