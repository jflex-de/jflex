
%%

%public
%class Sevenbit

%7bit

%int
%debug

WhiteSpace = [ \t\r\n]

%%

{WhiteSpace}+   { return 0; }
[^]             { return 1; }
<<EOF>>         { return -1;}
