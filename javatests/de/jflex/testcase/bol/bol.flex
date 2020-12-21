package jflex.testcase.bol;

%%

%public
%class BolScanner
%type State
%debug

%line
%column

%unicode

%char
%{
  State state = State.INITIAL;
%}

%%

^"hello"$  { return State.HELLO_AT_BOL_AND_EOL; }
^"hello"   { return State.HELLO_AT_BOL; }
"hello"$   { return State.HELLO_AT_EOL; }
"hello"    { return State.HELLO_SIMPLY; }

\n         { return State.LINE_FEED; }

" "        { return State.SPACE; }
[^]        { return State.OTHER; }

<<EOF>>    { return State.END_OF_FILE; }
