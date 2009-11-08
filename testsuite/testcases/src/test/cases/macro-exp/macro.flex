// file contributed by Stephen Ostermiller
import java.io.*;
%%
%class Macro
%type String
%{
	public static void main(String[] args) throws IOException {
		(new Macro(new StringReader("# #/ #/* comment */ #-- comment"))).yylex();
	}
%}
operatorChar=([\+\-\*\/\<\>\=\~\!\@\#\%\^\&\|\`\?\$])
illegalOperator=(({operatorChar}*("--"|"/*"){operatorChar}*)|({operatorChar}+[\-\/]))
operator=(!(!({operatorChar}+)|{illegalOperator}))
%%
<YYINITIAL> (("/*"~"*/")|("--"[^\n]*)) {
	System.out.println("Comment: " + yytext());
}
/* Separates the operators from the comments but
 * operators are only one character each 
<YYINITIAL> ({operatorChar}) {
	System.out.println("Operator: " + yytext());
} */
/* Operarators are a better length but 
 * comments are not found.
<YYINITIAL> ({operatorChar}+) {
	System.out.println("Operator: " + yytext());
} */
/* I get a macro error.*/
<YYINITIAL> ({operator}) {
	System.out.println("Operator: " + yytext());
}
<YYINITIAL> [^] {}
