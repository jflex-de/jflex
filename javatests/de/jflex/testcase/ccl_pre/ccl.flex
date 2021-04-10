package de.jflex.testcase.ccl_pre;
%%
%16bit

Identifier = [\@]* [:jletter:] [:jletterdigit:]*

%public
%class Ccl

%debug
%int

%%

{Identifier}  { System.out.println("matched identifier ["+yytext()+"]"); }

. { System.out.println("Fallback ["+yytext()+"]"); }
