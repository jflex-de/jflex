
%%

%public
%class Encoding
%integer
%debug

%line
%column

%unicode

%%

"�"    { System.out.println("�"); return 0; }
"b"    { return 1; }


[^]         { }
