package jflex.testcase.caseless_jflex;

%%

%public
%class Caseless

%standalone

%unicode

%ignorecase

NL = \r|\n|\r\n

%%

a          { System.out.println("--a--"+yytext()+"--"); }
[a-z]+     { System.out.println("--[a-z]+--"+yytext()+"--");  }
"hello"    { System.out.println("--"+yytext()+"--");   }

{NL}       { System.out.println("--newline--"); }
.          { System.out.println( "--"+yytext()+"--" ); }
