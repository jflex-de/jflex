
%%

%public
%class Bol2
%integer
%debug

%unicode

%%

^"hello"   { System.out.println("hello at BOL"); }
"hello"    { System.out.println("hello no BOL");   }

[^]        { System.out.println("other"); }
