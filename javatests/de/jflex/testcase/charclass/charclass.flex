package jflex.testcase.charclass;

%%

%public
%class CharclassScanner
%type State

%unicode

%%

// Nested char classes and union
[[[[a]b]c][d[ef]]]+    { return State.NESTED_ABCDEF; }
[gh||ij||[kl||[m]]ts]+ { return State.NESTED_GHIJKMTS; }
[[a-z][[47][][][]]]+   { return State.NESTED_AZ47; }

// Set subtraction
[A-G--CG-Z]+         { return State.SUBSTRACT_AGCGZ; }
[[H-L]--K]+          { return State.SUBSTRACT_HLK; }
[^[^ABC--ABZ]]       { return State.NOT_ABC_ABZ; }
[^[^-KO--O]]+        { return State.NOT_KOO; }
[^[^-GM---M]]+       { return State.NOT_GMM; }
[-M-O--N]+           { return State.SUBSTRACT_MON; }
[-N-P---P]+          { return State.SUBSTRACT_NPP; }
[QR[-]---R]+         { return State.SUBSTRACT_QRR; }
[^[^R[-]---]]+       { return State.NOT_R; }
[\p{Lu}--A-RT-Z]+    { return State.SUBSTRACT_PLU; }

// Intersection
[\p{L}&&[T||\p{N}]]+ { return State.INTER_PLN; }
[-TUV&&-UA]+         { return State.INTER_TUV_UA; }
[-TUV&&AV]+          { return State.INTER_TUV_AV; }
[VW&&-WA]+           { return State.INTER_VW_WA; }
[XXX&&XYZ]+          { return State.INTER_XXX_XYZ; }
[^[^-AYZ&&-YBM]]+    { return State.INTER_AYZ_YMB; }
[^[^-ZR&&ABZ]]+      { return State.INTER_ZR_ABZ; }
[^[^,.:&&-*(),]]+    { return State.INTER_PUNCTUATION1; }
[^[^!&#&&\^~!]]+     { return State.INTER_PUNCTUATION2; }

// Symmetric difference
[[\p{L}||+]~~[\p{L}||&]]+ { return State.SYM_1; }
[-.~~-/]+                 { return State.SYM_2; }
[-()~~)]+                 { return State.SYM_3; }
[\[\]~~\[-]+              { return State.SYM_4; }
[#!~~!#`$]+               { return State.SYM_5; }
[^[^-=%_|~~-%=!]]+        { return State.SYM_6; }
[^[^-<>~~<@>]]+           { return State.SYM_7; }
[^[^{}~~-{\\}]]+          { return State.SYM_8; }
[^[^;?/~~\"?/]]+          { return State.SYM_9; }


[^]  { /* do nothing */ }

<<EOF>>    { return State.END_OF_FILE; }
