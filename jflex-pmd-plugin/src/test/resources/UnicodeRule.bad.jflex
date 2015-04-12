%%

%public
%class Test

%int
%debug

TAKRI_DIGIT = [\u116C0-\u116C9]
LETTER = [a-zA-Z]

%%

{LETTER} ({LETTER}|{TAKRI_DIGIT})*    {  /* identifier */  return 1; }
[^]                                   {  /* single char */ return 2; }
