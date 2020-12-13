package jflex.testcase.sevenbit;

%%

%public
%class Yylexthrow

%int
%yylexthrow Exception

WhiteSpace = [ \t\r\n]

%%

{WhiteSpace}+   { return 0; }
[^]             { return 1; }
<<EOF>>         { return -1;}
