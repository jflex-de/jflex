package jflex.testcase.sevenbit; // comment on same line allowed

%%

%public                       // comment on same line allowed
%class Yylexthrow             // comment on same line allowed

%int                          // comment on same line allowed
%yylexthrow RuntimeException  // comment on same line allowed
%yylexthrow SecurityException /* multi-line comment
                               * allowed
                               */

WhiteSpace = [ \t\r\n]        // comment on same line allowed

%%

{WhiteSpace}+   { return 0; }
[^]             { return 1; }
<<EOF>>         { return -1;}
