package jflex.testcase.action_pipe;


%%


%public
%class ActionPipe

%int

LineTerminator = \r|\n|\r\n

Identifier = [:jletter:][:jletterdigit:]*


%%


Identifier { System.out.println(yytext()); }

<<EOF>>      |
LineTerminator      { System.out.println("⏎\n"); }

[^]  { /* no action */ }
