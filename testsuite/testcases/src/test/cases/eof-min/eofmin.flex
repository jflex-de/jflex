%%

%unicode
%standalone
%debug

%public
%class Eofmin

%state STATE2

%%

<YYINITIAL,STATE2>  {

  "a"  {  yybegin(STATE2);
          return 0;
       }

  "b"  {  yybegin(YYINITIAL);
          return 1;
       }
}

<YYINITIAL> {

<<EOF>>  { return 2; }

}

<STATE2> {

<<EOF>> { return 3; }

}
