
%%

%public
%class Look
%integer
%debug

%line
%column

%unicode

%%

"ab"/"cde"  { System.out.println(yytext()); return 2; }
"abcd"/"f"  { System.out.println(yytext()); return 7; }

[^]         { }
