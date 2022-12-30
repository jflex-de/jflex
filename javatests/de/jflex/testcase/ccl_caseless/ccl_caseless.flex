package de.jflex.testcase.ccl_caseless;

%%

%public
%class Ccl_caseless
%int
%debug

%full
%caseless

%%

sb { return 1; }

[^] { /* default */ }
