package jflex.testing.action_pipe;


%%


%public
%class ActionPipe

%int

LineTerminator = \r|\n|\r\n

Identifier = [:jletter:][:jletterdigit:]*


%%


Identifier { System.out.println(yytext()); }

LineTerminator |
<<EOF>>      { System.out.println("⏎\n"); }

[^]  { /* no action */ }
