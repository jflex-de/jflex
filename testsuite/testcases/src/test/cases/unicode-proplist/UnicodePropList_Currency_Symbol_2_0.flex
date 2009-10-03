%%

%unicode 2.0
%public
%class UnicodePropList_Currency_Symbol_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Currency Symbol} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
