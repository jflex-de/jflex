%%

%unicode 2.1
%public
%class UnicodePropList_Numeric_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Numeric} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
