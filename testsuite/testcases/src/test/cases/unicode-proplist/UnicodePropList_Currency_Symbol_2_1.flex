%%

%unicode 2.1
%public
%class UnicodePropList_Currency_Symbol_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Currency Symbol} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
