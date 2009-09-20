%%

%unicode 3.0
%public
%class UnicodePropList_Currency_Symbol_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Currency Symbol} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
