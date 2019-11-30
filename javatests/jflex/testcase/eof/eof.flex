package jflex.testcase.eof;

%%

%public
%class Scanner
%type Token

%char
%line
%column

%%

[a-z]{2,3}  { return Token.AZ;    }
[^]         { return Token.OTHER; }
