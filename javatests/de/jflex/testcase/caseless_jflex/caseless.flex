package de.jflex.testcase.caseless_jflex;

%%

%public
%class CaselessScanner

%type State

%unicode

%ignorecase

NL = \r|\n|\r\n

%%

a          { return State.A; }
[a-z]+     { return State.WORD;  }
"hello"    { return State.HELLO;   }

{NL}       { return State.NEW_LINE; }
.          { return State.OTHER; }

<<EOF>>    { return State.END_OF_FILE; }
