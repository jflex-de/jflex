%%
%16bit

Identifier = [\@]* [:jletter:] [:jletterdigit:]*

%public
%class Ccl3

%debug
%int

%%

{Identifier}  { System.out.println("matched identifier ["+yytext()+"]"); }

. { System.out.println("Fallback ["+yytext()+"]"); }
