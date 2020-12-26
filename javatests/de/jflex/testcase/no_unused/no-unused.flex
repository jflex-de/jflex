package de.jflex.testcase.no_unused;

%%

%public
%class NoUnused
%integer
%debug

UNUSED = "an" "unused"+ "macro"

%%

[^]         { }
