
%%

%public
%class Genlook2
%integer
%debug

%unicode

%buffer 10

%states YYINITIAL

%%

<YYINITIAL> {    
  "a"+/"b"+    { /* match should never contain a 'b', which is lookahead only */ }

  [^]          { /* no lookahead */ }
}
