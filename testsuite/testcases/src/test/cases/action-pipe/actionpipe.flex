
%%

%public
%class Actionpipe

%int
%standalone

LineTerminator = \r|\n|\r\n

Identifier = [:jletter:][:jletterdigit:]*


%%

{Identifier}     { System.out.println(yytext()); return 1; }

<<EOF>>          |
{LineTerminator} { System.out.println("⏎"); return 0; }

[^]              { /* no action */ }
