package de.jflex.testcase.sevenbit;

%%

%public
%class SevenBit

%7bit
%int

WhiteSpace = [ \t\r\n]

%%

{WhiteSpace}+   { return 0; }
[^]             { return 1; }
<<EOF>>         { return -1;}
