package de.jflex.testcase.action_pipe;

%%

%public
%class EofPipeAction

%int

LineTerminator = \r|\n|\r\n

Identifier = [:jletter:][:jletterdigit:]*


%%

{Identifier}     { System.out.println(yytext()); return 1; }

<<EOF>>          |
{LineTerminator} { System.out.println("⏎"); return 0; }

[^]              { /* no action */ }
