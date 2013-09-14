
%%

%public
%class Charclass

%standalone

%unicode

%%

// Nested char classes and union
[[[[a]b]c][d[ef]]]+    { System.out.println("/[[[[a]b]c][d[ef]]]+/ matched \"" + yytext() + "\""); }
[gh||ij||[kl||[m]]ts]+ { System.out.println("/[gh||ij||[kl||[m]]ts]+/ matched \"" + yytext() + "\""); }
[[a-z][[47][][][]]]+   { System.out.println("/[\\p{L}[[\\d][][][]]]+/ matched \"" + yytext() + "\""); }

// Set subtraction
[A-G--CG-Z]+         { System.out.println("/[A-G--CG-Z]+/ matched \"" + yytext() + "\""); }
[[H-L]--K]+          { System.out.println("/[[H-L]--K]+/ matched \"" + yytext() + "\""); }
[^[^ABC--ABZ]]       { System.out.println("/[^[^ABC--ABZ]]/ matched \"" + yytext() + "\""); }
[^[^-KO--O]]+        { System.out.println("/[^[^-KO--O]]+/ matched \"" + yytext() + "\""); }
[^[^-GM---M]]+       { System.out.println("/[^[^-GM---M]]+/ matched \"" + yytext() + "\""); }
[-M-O--N]+           { System.out.println("/[-M-O--N]+/ matched \"" + yytext() + "\""); } 
[-N-P---P]+          { System.out.println("/[-N-P---P]+/ matched \"" + yytext() + "\""); }
[QR[-]---R]+         { System.out.println("/[QR[-]---R]+/ matched \"" + yytext() + "\""); }
[^[^R[-]---]]+       { System.out.println("/[^[^R[-]---]]+/ matched \"" + yytext() + "\""); }
[\p{Lu}--A-RT-Z]+    { System.out.println("/[\\p{Lu}--A-RT-Z]+/ matched \"" + yytext() + "\""); }

// Intersection
[\p{L}&&[T||\p{N}]]+ { System.out.println("/[\\p{L}&&[T||\\p{N}]]+/ matched \"" + yytext() + "\""); }
[-TUV&&-UA]+         { System.out.println("/[-TUV&&-UA]+/ matched \"" + yytext() + "\""); }
[-TUV&&AV]+          { System.out.println("/[-TUV&&VA]+/ matched \"" + yytext() + "\""); }
[VW&&-WA]+           { System.out.println("/[VW&&-WA]+/ matched \"" + yytext() + "\""); }
[XXX&&XYZ]+          { System.out.println("/[XXX&&XYZ]+/ matched \"" + yytext() + "\""); }
[^[^-AYZ&&-YBM]]+    { System.out.println("/[^[^-AYZ&&-YBM]]+/ matched \"" + yytext() + "\""); }
[^[^-ZR&&ABZ]]+      { System.out.println("/[^[^-ZR&&ABZ]]+/ matched \"" + yytext() + "\""); }
[^[^,.:&&-*(),]]+    { System.out.println("/[^[^,.:&&-*(),]]+/ matched \"" + yytext() + "\""); }
[^[^!&#&&\^~!]]+     { System.out.println("/[^[^!&#&&\\^~!]]+/ matched \"" + yytext() + "\""); }

// Symmetric difference
[[\p{L}||+]~~[\p{L}||&]]+ { System.out.println("/[[\\p{L}||+]~~[\\p{L}||&]]+/ matched \"" + yytext() + "\""); }
[-.~~-/]+                 { System.out.println("/[-.~~-/]+/ matched \"" + yytext() + "\""); }
[-()~~)]+                 { System.out.println("/[-()~~)]+/ matched \"" + yytext() + "\""); }
[\[\]~~\[-]+              { System.out.println("/[\\[\\]~~\\[-]+/ matched \"" + yytext() + "\""); }
[#!~~!#`$]+               { System.out.println("/[#!~~!#`$]+/ matched \"" + yytext() + "\""); }
[^[^-=%_|~~-%=!]]+        { System.out.println("/[^[^-=%_|~~-%=!]]+/ matched \"" + yytext() + "\""); }
[^[^-<>~~<@>]]+           { System.out.println("/[^[^-<>~~<@>]]+/ matched \"" + yytext() + "\""); }
[^[^{}~~-{\\}]]+          { System.out.println("/[^[^{}~~-{\\\\}]]+/ matched \"" + yytext() + "\""); }
[^[^;?/~~\"?/]]+          { System.out.println("/[^[^;?/~~\"?/]]+/ matched \"" + yytext() + "\""); }


[^]  { /* do nothing */ }

