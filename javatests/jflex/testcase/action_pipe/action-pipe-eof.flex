package jflex.testcase.action_pipe;

%%

%public
%class ActionPipeEof

%int

LineTerminator = \r|\n|\r\n

Identifier = [:jletter:][:jletterdigit:]*


%%

{Identifier}     { System.out.println(yytext()); return 1; }

{LineTerminator} |
<<EOF>>          { System.out.println("⏎"); return 0; }

[^]              { /* no action */ }
