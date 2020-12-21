package jflex.testcase.bol;

%%

%public
%class Bol2Scanner
%type State
%debug

%unicode

%%

^"hello"   { return State.HELLO_AT_BOL; }
"hello"    { return State.HELLO_SIMPLY; }

[^]        { return State.OTHER; }

<<EOF>>    { return State.END_OF_FILE; }
