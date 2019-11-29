
%%

%public
%class Caseless

%standalone

%unicode

%ignorecase

NL = \r|\n|\r\n

%%

a          { System.out.println("--a--"+yytext()+"--"); }
"hello"    { System.out.println("--"+yytext()+"--");   }
[a-z]+     { System.out.println("--[a-z]+--"+yytext()+"--");  }

{NL}       { System.out.println("--newline--"); }
.          { System.out.println( "--"+yytext()+"--" ); }
