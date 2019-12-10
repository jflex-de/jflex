
%%

%public
%class Bol
%integer
%debug

%line
%column

%unicode

%char

%%

// "hello" / [ \n]* "hello" { System.out.println("hello followed by hello"); }
^"hello"$  { System.out.println("hello at BOL and EOL"); }
^"hello"   { System.out.println("hello at BOL"); }
"hello"$   { System.out.println("hello at EOL"); }
"hello"    { System.out.println("just hello");   }

\n         { System.out.println("\\n"); }

" "        { System.out.println( "\" \"" ); }
[^]        { System.out.println( yytext() ); }
