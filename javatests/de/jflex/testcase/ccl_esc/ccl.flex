package jflex.testcase.ccl_esc;

%%

%class Ccl
%type Token

%% 

"ab"  { return Token.AB;}
[^]+  { return Token.OTHER; }
