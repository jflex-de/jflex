
%%

%public
%class Count

%standalone

%char
%line
%column

%%

[^] | [a-z]{2,3}  { System.out.println("line: "+yyline+", column: "+yycolumn+", char: "+yychar); 
	                System.out.println("match ["+yytext()+"]"); }

