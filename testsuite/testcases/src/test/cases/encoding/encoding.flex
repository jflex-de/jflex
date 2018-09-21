
%%

%public
%class Encoding
%integer
%debug

%line
%column

%unicode

%%

"ä"    { System.out.println("ä"); return 0; }
"b"    { return 1; }


[^]         { }
